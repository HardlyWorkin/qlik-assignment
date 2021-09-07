package qlik.assignment.application.web.message;

import javax.validation.constraints.NotNull;
 

public class PutMessageInput {

	@NotNull(message = "content cannot be null")
	private String content;
	
	@NotNull(message = "version cannot be null")
	private Integer version;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
}
