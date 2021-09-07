package qlik.assignment.domain.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MessageIdGeneratorTest {

	@Test
	void idStartsAt1_andIncrementsBy1ForEachSubsequentIdGenerated() throws MaxMessageIdReachedException {
		final MessageIdGenerator idGenerator = new MessageIdGenerator(Integer.MAX_VALUE);

		for (int x = 1; x < 100; x++) {
			assertEquals(x, idGenerator.nextId());
		}
	}

	@Test
	void throwsExceptionWhenNextIdGreaterThanConfiguredMaxId() throws MaxMessageIdReachedException {
		final Integer maxId = 10;
		final MessageIdGenerator idGenerator = new MessageIdGenerator(maxId);

		for (int x = 1; x <= maxId; x++) {
			idGenerator.nextId();
		}

		Assertions.assertThrows(MaxMessageIdReachedException.class, () -> {
			idGenerator.nextId();
		});
	}
}
