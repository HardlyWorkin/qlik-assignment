package qlik.assignment.application.dal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import qlik.assignment.domain.model.Message;
import qlik.assignment.domain.model.Message.MandatoryFieldException;
import qlik.assignment.domain.repository.OptimisticLockException;
import qlik.assignment.domain.repository.MessageNotFoundException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;

public class InMemoryMessageRepositoryTest {

	private InMemoryMessageRepository repository;

	@BeforeEach
	void beforeEach() {
		repository = new InMemoryMessageRepository();
	}

	@Nested
	class FindingById {

		@Test
		void returnsACopyOfTheMessageWhenMessageIdExists() throws Exception {
			final Message savedMessage1 = new Message(1, "msgcontent1");
			final Message savedMessage2 = new Message(2, "msgcontent2");
			repository.save(savedMessage1);
			repository.save(savedMessage2);

			final Message retrievedMessage1 = repository.findById(1);
			assertNotSame(savedMessage1, retrievedMessage1, "Repo should only expose clones");
			assertEquals(savedMessage1, retrievedMessage1);

			final Message retrievedMessage2 = repository.findById(2);
			assertNotSame(savedMessage2, retrievedMessage2, "Repo should only expose clones");
			assertEquals(savedMessage2, retrievedMessage2);
		}

		@Test
		void throwsExceptionWhenIdDoesNotExist() {
			Assertions.assertThrows(MessageNotFoundException.class, () -> {
				repository.findById(999);
			});
		}
	}

	@Nested
	class SavingAMessage {

		@Test
		void changesToAMessageMustBeSavedBeforeVisibleFromARetrieval() throws Exception {
			final Message message = new Message(10, "original content");

			repository.save(message);

			message.changeContent("new content");
			assertNotEquals("new content", repository.findById(10).getContent());

			repository.save(message);
			assertEquals("new content", repository.findById(10).getContent());
		}

		@Test
		void savingAMessageIncrementsItsVersion_whenSuccessful() throws Exception {
			final Message message = repository.save(new Message(1, "msgcontent1"));
			assertEquals(1, message.getVersion());

			final Message msgAfter2ndSave = repository.save(message);
			assertEquals(2, msgAfter2ndSave.getVersion());
		}

		/**
		 * Validate we have optimistic locking
		 */
		@Test
		void attemptingToSaveMessageThrowsException_whenVersionDoesNotMatchExistingMessageVersion() throws Exception {
			final Message message = new Message(1, "msgcontent1");
			// version 1 after statement
			repository.save(message);

			// version 2 after statement
			repository.save(message);

			// Try saving same ID with different version 0
			Assertions.assertThrows(OptimisticLockException.class, () -> {
				repository.save(new Message(1, "msgcontent1"));
			});
		}
	}

	@Nested
	class GettingAllMessages {
		@Test
		void returnsAnEmptyListWhenNoMessagesHaveBeenSaved() {
			assertThat(repository.getAll(), hasSize(0));
		}

		@Test
		void returnsClonesOfAllSavedMessages() throws MandatoryFieldException, OptimisticLockException {

			final Message message1 = repository.save(new Message(1, "msgcontent1"));
			assertThat(repository.getAll(), containsInAnyOrder(cloneOf(message1)));

			final Message message2 = repository.save(new Message(2, "msgcontent2"));
			assertThat(repository.getAll(), containsInAnyOrder(cloneOf(message1), cloneOf(message2)));

			final Message message3 = repository.save(new Message(3, "msgcontent3"));
			assertThat(repository.getAll(), containsInAnyOrder(cloneOf(message1), cloneOf(message2), cloneOf(message3)));
		}

	}

	@Nested
	class DeletingAMessage {
		@Test
		void aMessageCannotBeRetrievedAfterDeletion() throws Exception {
			final Message message = new Message(1, "msgcontent");
			repository.save(message);

			repository.delete(1);

			// Verify msg cannot be found by ID
			Assertions.assertThrows(MessageNotFoundException.class, () -> {
				repository.findById(1);
			});

			// Verify msg is not included when getting all messages
			assertThat(repository.getAll(), not(hasItem(message)));
		}

		@Test
		void nonDeletedMessagesCanStillBeRetrieved() throws Exception {
			final Message msgToBeDeleted = new Message(1, "msgcontent1");
			final Message otherMessage = new Message(2, "msgcontent2");

			repository.save(msgToBeDeleted);
			repository.save(otherMessage);

			repository.delete(1);

			assertNotNull(repository.findById(2));
			assertThat(repository.getAll(), hasItem(otherMessage));
		}

		@Test
		void throwsExceptionWhenMessageDoesNotExist() {
			final int idOfNonExistentMessage = 10;

			Assertions.assertThrows(MessageNotFoundException.class, () -> {
				repository.delete(idOfNonExistentMessage);
			});
		}
	}

	public Matcher<Message> cloneOf(Message originalMsg) {

		return new TypeSafeMatcher<Message>() {

			@Override
			public void describeTo(Description description) {
			}

			@Override
			protected boolean matchesSafely(Message item) {
				return originalMsg != item && originalMsg.equals(item);
			}
		};
	}
}
