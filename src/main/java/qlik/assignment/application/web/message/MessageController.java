package qlik.assignment.application.web.message;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import qlik.assignment.domain.factory.MaxMessageIdReachedException;
import qlik.assignment.domain.factory.MessageFactory;
import qlik.assignment.domain.model.Message;
import qlik.assignment.domain.model.Message.MandatoryFieldException;
import qlik.assignment.domain.repository.OptimisticLockException;
import qlik.assignment.domain.repository.MessageNotFoundException;
import qlik.assignment.domain.repository.MessageRepository;

@RestController
@Validated
@RequestMapping("/api/messages")
public class MessageController {

	private InputWhitelistValidator inputWhitelistValidator;
	private MessageRepository messageRepository;
	private MessageFactory factory;

	@Autowired
	public MessageController(InputWhitelistValidator inputValidator, MessageFactory messageFactory, MessageRepository messageRepository) {
		this.inputWhitelistValidator = inputValidator;
		this.messageRepository = messageRepository;
		this.factory = messageFactory;
	}

	@GetMapping("/{id}")
	public Message get(@PathVariable int id) throws MessageNotFoundException {
		return messageRepository.findById(id);
	}

	@GetMapping("")
	public ListResponse listMessages(
			@RequestParam(name = "page", required = false) int page, // Pagination currently not implemented
			@RequestParam(name = "per_page", required = false) Integer perPage, // Pagination currently not implemented
			@RequestParam(name = "sort_by", required = false) String sortBy // Sorting not currently implemented
	) {
		final ListResponse response = new ListResponse();

		final List<Message> allMessages = messageRepository.getAll();
		response.addMessages(messageRepository.getAll());
		response.setTotal(allMessages.size());

		return response;
	}

	@PutMapping("/{id}")
	public Message putMessage(@PathVariable("id") Integer id, @Valid @RequestBody PutMessageInput input) throws MessageNotFoundException, MandatoryFieldException, OptimisticLockException {
		inputWhitelistValidator.validate(input.getContent());

		final Message message = messageRepository.findById(id);
		message.changeContent(input.getContent());
		message.changeVersion(input.getVersion());
		
		return messageRepository.save(message);
	}

	@PostMapping
	public Message postMessage(@Valid @RequestBody PostMessageInput input) throws OptimisticLockException, MandatoryFieldException, MaxMessageIdReachedException {
		inputWhitelistValidator.validate(input.getContent());

		return messageRepository.save(factory.create(input.getContent()));
	}

	@DeleteMapping("/{id}")
	public void deleteMessage(@NotNull @PathVariable("id") int id) throws MessageNotFoundException {
		messageRepository.delete(id);
	}
}
