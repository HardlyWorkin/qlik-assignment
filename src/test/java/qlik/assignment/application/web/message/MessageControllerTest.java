package qlik.assignment.application.web.message;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import qlik.assignment.application.web.message.ListResponse;
import qlik.assignment.application.web.message.PostMessageInput;
import qlik.assignment.application.web.message.PutMessageInput;
import qlik.assignment.domain.model.Message;

/**
 * Component tests
 * 
 */
@SpringBootTest
@AutoConfigureMockMvc
public class MessageControllerTest {

	private static ObjectMapper objectMapper;

	@Autowired
	private MockMvc mvc;

	@BeforeAll
	static void beforeAll() {
		objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	@Test
	void postingAMessage() throws JsonProcessingException, Exception {
		final PostMessageInput input = new PostMessageInput();
		input.setContent("msgcontent");

		final ResultActions postActions = mvc.perform(MockMvcRequestBuilders.post("/api/messages").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(input))).andExpect(status().isOk());

		final Message postedMessage = objectMapper.readValue(postActions.andReturn().getResponse().getContentAsString(), Message.class);

		// Verify response
		assertEquals(1, postedMessage.getId());
		assertEquals("msgcontent", postedMessage.getContent());
		assertEquals(1, postedMessage.getVersion());
	}

	@Nested
	class GivenAMessageHasBeenPosted {

		Message postedMessage;

		@BeforeEach
		void beforeEach() throws JsonProcessingException, Exception {
			final PostMessageInput input = new PostMessageInput();
			input.setContent("msgcontent");

			final ResultActions postResultActions = mvc.perform(MockMvcRequestBuilders.post("/api/messages").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(input))).andExpect(status().isOk());

			postedMessage = objectMapper.readValue(postResultActions.andReturn().getResponse().getContentAsString(), Message.class);
		}

		@Test
		void theMessageCanBeRetrievedById() throws Exception {
			final ResultActions getResultActions = mvc.perform(MockMvcRequestBuilders.get("/api/messages/{id}", postedMessage.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

			final Message retrievedMessage = objectMapper.readValue(getResultActions.andReturn().getResponse().getContentAsString(), Message.class);
			assertEquals(postedMessage.getId(), retrievedMessage.getId());
			assertEquals(postedMessage.getContent(), retrievedMessage.getContent());
			assertEquals(postedMessage.getVersion(), retrievedMessage.getVersion());
		}

		@Test
		void theMessageCanBeUpdated() throws JsonProcessingException, Exception {
			final PutMessageInput input = new PutMessageInput();
			input.setVersion(postedMessage.getVersion());
			input.setContent("updatedcontent");

			// Send Put request
			final ResultActions putResultActions = mvc.perform(MockMvcRequestBuilders.put("/api/messages/{id}", postedMessage.getId()).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(input))).andExpect(status().isOk());

			// verify response
			final Message responseMsg = objectMapper.readValue(putResultActions.andReturn().getResponse().getContentAsString(), Message.class);
			assertEquals(postedMessage.getId(), responseMsg.getId());
			assertEquals("updatedcontent", responseMsg.getContent());
			assertEquals(2, responseMsg.getVersion());

			// Verify the message has really been updated by GET request
			final ResultActions getActions = mvc.perform(MockMvcRequestBuilders.get("/api/messages/{id}", postedMessage.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
			final Message retrievedMessage = objectMapper.readValue(getActions.andReturn().getResponse().getContentAsString(), Message.class);

			assertEquals("updatedcontent", retrievedMessage.getContent());
			assertEquals(postedMessage.getVersion() + 1, retrievedMessage.getVersion(), "Version should be incremented after an update");
		}

		@Test
		void theMessageIsIncludedInListResponse() throws Exception {
			final ResultActions getActions = mvc.perform(MockMvcRequestBuilders.get("/api/messages").param("page", "1").param("per_page", "1").param("sort_by", "id")).andExpect(status().isOk());

			final ListResponse listResponse = objectMapper.readValue(getActions.andReturn().getResponse().getContentAsString(), ListResponse.class);

			assertThat(listResponse.getMessages(), hasItem(postedMessage));
		}
	}
}
