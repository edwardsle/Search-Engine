package model;

public class Posting {

	private String docId;
	private double score;
	private int tf;
	
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

	public int getTf() {
		return tf;
	}

	public void setTf(int tf) {
		this.tf = tf;
	}
	
	public double getTfScore() {
		return 1.0 + Math.log10(tf);
	}
	
	
	public Posting(String docId, double score, int tf) {
		super();
		this.docId = docId;
		this.score = score;
		this.tf = tf;
	}

	public Posting(String docId, double score) {
		super();
		this.docId = docId;
		this.score = score;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return (this.docId.equals(((Posting) obj).docId));
	}
}
