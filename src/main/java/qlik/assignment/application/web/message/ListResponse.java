package qlik.assignment.application.web.message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import qlik.assignment.domain.model.Message;

public class ListResponse {

	private List<Message> messages;

	// Total number of messsages in the repo
	private Integer total;

	// Current page returned
	private Integer page;

	// Relevant references
	private HashMap<String, String> links;

	public ListResponse() {
		// TODO: currently hardcoding some values because pagination has not been
		// implemented
		page = 1;
		links = new HashMap<>();
		links.put("next", "http://linktonextpage");

		messages = new ArrayList<>();
	}

	public void addMessages(List<Message> messages) {
		this.messages.addAll(messages);
	}

	public List<Message> getMessages() {
		return messages;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer getTotal() {
		return total;
	}

	public Integer getPage() {
		return page;
	}

	public HashMap<String, String> getLinks() {
		return links;
	}
}
