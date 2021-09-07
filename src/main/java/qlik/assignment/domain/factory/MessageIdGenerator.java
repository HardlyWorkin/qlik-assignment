package qlik.assignment.domain.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Generates IDs for messages <br>
 * Generated ID starts at 1 and each new ID is the previous ID incremented by 1. <br>
 * The maximum ID value is configurable and an exception is thrown if requested ID is greater than the max
 */
@Component
public class MessageIdGenerator {

	private Integer currentId;
	private int maxId;

	/**
	 * @param maxId the maximum ID allowed
	 */
	@Autowired
	public MessageIdGenerator(@Value("${maxMessages}") int maxId) {
		currentId = 0;
		this.maxId = maxId;
	}

	/**
	 * @return next available ID for a new message
	 * @throws MaxMessageIdReachedException when the max ID has been reached
	 */
	public synchronized Integer nextId() throws MaxMessageIdReachedException {
		if (currentId + 1 > maxId) {
			throw new MaxMessageIdReachedException();
		}
		return ++currentId;
	}
}
