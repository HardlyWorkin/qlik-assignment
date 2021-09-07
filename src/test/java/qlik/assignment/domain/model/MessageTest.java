package qlik.assignment.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import qlik.assignment.domain.model.Message.MandatoryFieldException;

public class MessageTest {

	private static final Integer DEFAULT_MSG_ID = 1;
	private static final String DEFAULT_MSG_CONTENT = "default content";

	@Test
	void aMessageMustHaveAnId() throws MandatoryFieldException {
		final Integer nullId = null;
		Assertions.assertThrows(MandatoryFieldException.class, () -> new Message(nullId, DEFAULT_MSG_CONTENT));

		assertEquals(1, new Message(1, DEFAULT_MSG_CONTENT).getId());
		assertEquals(2, new Message(2, DEFAULT_MSG_CONTENT).getId());
	}

	@Test
	void aMessageMustHaveContent() throws MandatoryFieldException {
		final String nullContent = null;

		Assertions.assertThrows(MandatoryFieldException.class, () -> new Message(DEFAULT_MSG_ID, nullContent));

		assertEquals("", new Message(DEFAULT_MSG_ID, "").getContent(), "Empty content is allowed");
		assertEquals(" ", new Message(DEFAULT_MSG_ID, " ").getContent(), "Only whitespace is allowed");
		assertEquals("abc", new Message(DEFAULT_MSG_ID, "abc").getContent());

		final Message existingMessage = new Message(DEFAULT_MSG_ID, "oldcontent");
		Assertions.assertThrows(MandatoryFieldException.class, () -> {
			existingMessage.changeContent(null);
		});
	}

	@Test
	void messageContentCanBeChangedToNonNullValue() throws MandatoryFieldException {
		final Message existingMessage = new Message(1, "existing content");

		existingMessage.changeContent("new content");
		assertEquals("new content", existingMessage.getContent());
	}

	@Test
	void aNewMessageStartsAtVersion0() throws MandatoryFieldException {
		assertEquals(0, new Message(DEFAULT_MSG_ID, DEFAULT_MSG_CONTENT).getVersion());
	}

	@Test
	void messageVersionCanBeIncremented() throws MandatoryFieldException {
		final Message msg = new Message(DEFAULT_MSG_ID, DEFAULT_MSG_CONTENT);

		msg.incrementVersion();
		assertEquals(1, msg.getVersion());

		msg.incrementVersion();
		assertEquals(2, msg.getVersion());
	}
	
	@Test
	void messageVersionCanBeChanged() throws MandatoryFieldException {
		final Message msg = new Message(1, DEFAULT_MSG_CONTENT);
		
		msg.changeVersion(10);
		assertEquals(10, msg.getVersion());
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"", //Empty
			" ", //space
			"a", //single character
			"aa",
			"aA", //case insensitive			
			"aaa",
			"aaA",
			"aba",
			"Aba",
			"aaaa",
			"aabaa",
			"aabbAa"
			})
	void aMessageIsAPalindrome_whenItsContentReadsSameBackwardAsForwardDisregardingCaseSensitivity(String content) throws MandatoryFieldException {
		final Message palindromeMsg = new Message(DEFAULT_MSG_ID, content);
		assertTrue(palindromeMsg.isPalindrome());

		final Message existingMsg = new Message(DEFAULT_MSG_ID, DEFAULT_MSG_CONTENT);
		existingMsg.changeContent(content);
		assertTrue(existingMsg.isPalindrome());
	}
	
	@ParameterizedTest
	@ValueSource(strings = {
			"ab",
			"aab",
			"baa",
			"bba",
			"abbc",
			"cbba",
			"cccaa"
			})
	void aMessageIsNotAPalindrome_whenItsContentDoesNotReadSameBackwardAsForward(String content) throws MandatoryFieldException {
		final Message msg = new Message(DEFAULT_MSG_ID, content);
		assertFalse(msg.isPalindrome());

		final Message existingMsg = new Message(DEFAULT_MSG_ID, DEFAULT_MSG_CONTENT);
		existingMsg.changeContent(content);
		assertFalse(existingMsg.isPalindrome());
	}
	
	@Test
	void aClonedMessageHasTheSame_id_content_version() throws Exception {
		final Message message = new Message(999,"msg999content");
		message.incrementVersion();
		
		final Message clonedMessage = message.cloneMsg();
		
		assertNotSame(message,clonedMessage);
		assertEquals(message.getId(),clonedMessage.getId());
		assertEquals(message.getContent(),clonedMessage.getContent());
		assertEquals(message.getVersion(),clonedMessage.getVersion());
	}
}
