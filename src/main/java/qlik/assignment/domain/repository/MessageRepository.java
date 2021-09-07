package qlik.assignment.domain.repository;

import java.util.List;

import qlik.assignment.domain.model.Message;

public interface MessageRepository {

	/**
	 * @return all messages in repository
	 */
	List<Message> getAll();
	
	/**
	 * Returns message with provided ID
	 * @param id id of the message to return
	 * @return message with provided ID
	 * @throws MessageNotFoundException when message with provided ID does not exist
	 */
	Message findById(Integer id) throws MessageNotFoundException;
	
	/**
	 * Persists the provided message.<br>
	 * Once persisted, the message is available to be retrieved using {@link #getAll() getAll} 
	 * and {@link #findById(Integer) findById}
	 * 
	 * @param message
	 * @return The persisted Message
	 * @throws OptimisticLockException when the message has been changed by another user since it was retrieved
	 */
	Message save(Message message) throws OptimisticLockException;

	/**
	 * Deletes a message with the provided ID
	 * @param id of the message to be deleted
	 * @throws MessageNotFoundException when message with provided ID does not exist
	 */
	void delete(int id) throws MessageNotFoundException;
}
