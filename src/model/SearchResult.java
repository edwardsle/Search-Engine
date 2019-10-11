package model;

import java.util.ArrayList;

public class SearchResult {

	private String docId;
	private double score;
	private ArrayList<String> matchedKeywords;
	private String url;
		
	public SearchResult(String docId, double score, ArrayList<String> matchedKeywords, String url) {
		super();
		this.docId = docId;
		this.score = score;
		this.matchedKeywords = matchedKeywords;
		this.url = url;
	}

	public SearchResult() {
		// TODO Auto-generated constructor stub
	}

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}
	
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	

	public ArrayList<String> getMatchedKeywords() {
		return matchedKeywords;
	}

	public void setMatchedKeywords(ArrayList<String> matchedKeywords) {
		this.matchedKeywords = matchedKeywords;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return (this.docId.equals(((SearchResult) obj).docId));
	}
}
