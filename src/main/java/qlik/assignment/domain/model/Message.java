package qlik.assignment.domain.model;

import java.util.Objects;

public class Message {

	private Integer id;
	private String content;
	
	//Facilitates optimistic locking
	private Integer version;

	private Message() {
		//Deserialization requires default constructor
	}

	public Message(Integer id, String content) throws MandatoryFieldException {
		if (id == null || content == null) {
			throw new MandatoryFieldException();
		}

		this.id = id;
		this.content = content;
		this.version = 0;
	}

	public boolean isPalindrome() {
		for (int p1 = 0, p2 = content.length() - 1; p1 <= p2; p1++, p2--) {
			if (Character.toLowerCase(content.charAt(p1)) != Character.toLowerCase(content.charAt(p2))) {
				return false;
			}
		}
		return true;
	}

	public Integer getId() {
		return id;
	}

	public String getContent() {
		return content;
	}

	public void changeContent(String content) throws MandatoryFieldException {
		if (content == null) {
			throw new MandatoryFieldException();
		}
		this.content = content;
	}

	public Integer getVersion() {
		return version;
	}
	
	public void incrementVersion() {
		this.version++;
	}
	
	public void changeVersion(int newVersion) {
		this.version = newVersion;
	}

	public Message cloneMsg() {
		try {
			final Message clone = new Message(this.getId(), this.getContent());
			clone.version = this.version;
			return clone;
		} catch (MandatoryFieldException e) {
			throw new RuntimeException("this should never happen");
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(content, id, version);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Message other = (Message) obj;
		return Objects.equals(content, other.content) && Objects.equals(id, other.id) && Objects.equals(version, other.version);
	}
	
	public static class MandatoryFieldException extends Exception {
		private static final long serialVersionUID = 1L;
	}
}
