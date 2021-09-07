package qlik.assignment.application.web.message;

import javax.validation.constraints.NotNull;
 

public class PostMessageInput {

	@NotNull(message = "content cannot be null")
	private String content;
 
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
