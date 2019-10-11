package model;

public class Doc {
	private String docId;
	private String url;
	public String getDocId() {
		return docId;
	}
	public void setDocId(String docId) {
		this.docId = docId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Doc(String docId, String url) {
		super();
		this.docId = docId;
		this.url = url;
	}
	
	
}
