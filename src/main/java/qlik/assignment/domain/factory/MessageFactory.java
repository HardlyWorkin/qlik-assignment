package qlik.assignment.domain.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import qlik.assignment.domain.model.Message;
import qlik.assignment.domain.model.Message.MandatoryFieldException;

@Component
public class MessageFactory {

	private MessageIdGenerator idGenerator;

	@Autowired
	public MessageFactory(MessageIdGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}

	public Message create(String content) throws MandatoryFieldException, MaxMessageIdReachedException {
		return new Message(idGenerator.nextId(), content);
	}
}
