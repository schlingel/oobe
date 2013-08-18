package at.fundev.oobe.model;

public class PostEntry {
	private String name;
	
	private String author;
	
	private String date;
	
	private String filePath;
	
	private String[] tags;
	
	private String description;

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String[] getTags() {
		return tags;
	}

	public void setTags(String[] tags) {
		this.tags = tags;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj != null && obj instanceof PostEntry) {
			PostEntry entry = (PostEntry)obj;
			return entry.getAuthor().equals(author) && entry.getName().equals(name);
		}
		
		return false;
	}
}
