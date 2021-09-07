package qlik.assignment.domain.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import qlik.assignment.domain.model.Message;
import qlik.assignment.domain.model.Message.MandatoryFieldException;

@ExtendWith(MockitoExtension.class)
public class MessageFactoryTest {

	@Mock
	private MessageIdGenerator idGenerator;

	private MessageFactory factory;

	@BeforeEach
	void beforeEach() {
		factory = new MessageFactory(idGenerator);
	}

	@Test
	void createdMessageContainsGeneratedIdAndProvidedContent_whenContentIsValid() throws Exception {
		final Integer nextId = 10;
		Mockito.when(idGenerator.nextId()).thenReturn(nextId);

		final Message msg = factory.create("msgcontent");
		assertEquals("msgcontent", msg.getContent());
		assertEquals(nextId, msg.getId());
	}

	@Test
	void throwsExceptionWhenContentIsNull() throws Exception {
		Mockito.when(idGenerator.nextId()).thenReturn(1);

		Assertions.assertThrows(MandatoryFieldException.class, () -> {
			factory.create(null);
		});
	}
}
