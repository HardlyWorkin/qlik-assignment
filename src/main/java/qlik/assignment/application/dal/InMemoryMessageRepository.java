package qlik.assignment.application.dal;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import qlik.assignment.domain.model.Message;
import qlik.assignment.domain.repository.OptimisticLockException;
import qlik.assignment.domain.repository.MessageNotFoundException;
import qlik.assignment.domain.repository.MessageRepository;

/**
 * In memory repository for messages
 */
@Component
public class InMemoryMessageRepository implements MessageRepository {

	private ConcurrentHashMap<Integer, Message> idToMessageMap;

	public InMemoryMessageRepository() {
		idToMessageMap = new ConcurrentHashMap<>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Message> getAll() {
		return idToMessageMap.values().stream().map(Message::cloneMsg).collect(Collectors.toList());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Message findById(Integer id) throws MessageNotFoundException {
		final Message foundMessage = idToMessageMap.get(id);

		if (foundMessage == null) {
			throw new MessageNotFoundException(id);
		}
		return foundMessage.cloneMsg();
	}
 
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Message save(Message message) throws OptimisticLockException {
		final Message storedMessage = idToMessageMap.get(message.getId());

		if (versionMismatch(message, storedMessage)) {
			throw new OptimisticLockException(message.getId());
		}

		message.incrementVersion();
		final Message msgToBeSaved = message.cloneMsg();

		idToMessageMap.put(msgToBeSaved.getId(), msgToBeSaved);

		return message;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete(int id) throws MessageNotFoundException {
		if (idToMessageMap.remove(id) == null) {
			throw new MessageNotFoundException(id);
		}
	}

	private boolean versionMismatch(Message message, final Message storedMessage) {
		return storedMessage != null && storedMessage.getVersion() != message.getVersion();
	}
}
