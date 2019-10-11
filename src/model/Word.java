package model;

import java.util.ArrayList;

public class Word {
	
	private String term;
	private ArrayList<Posting> postings;
	private double idf;
	
	public String getTerm() {
		return term;
	}
	
	public Word(String term) {
		super();
		this.term = term;
		this.postings = new ArrayList<Posting>();
	}

	public void computeIdf(int numOfCorpusDocs) {
		idf = Math.log10(numOfCorpusDocs/postings.size());
	}
	
	public double getIdf() {
		return idf;
	}

	public void setTerm(String term) {
		this.term = term;
	}
	
	public ArrayList<Posting> getPostings() {
		return postings;
	}
	
	public void setPostings(ArrayList<Posting> postings) {
		this.postings = postings;
	}
	
	public Word(String term, ArrayList<Posting> postings, double idf) {
		super();
		this.term = term;
		this.postings = postings;
		this.idf = idf;
	}

	public void addOrUpdate(Posting posting) {
		if (postings.contains(posting))
		{
			Posting foundPosting = postings.get(postings.indexOf(posting));
			int tf = foundPosting.getTf() + 1;
			foundPosting.setTf(tf);
		}
		else
		{
			postings.add(posting);
		}
	}
	
	public void UpdatePostingScore()
	{
		for (Posting posting : postings)
		{
			posting.setScore(posting.getTfScore() * idf);
		}
	}
	
}
