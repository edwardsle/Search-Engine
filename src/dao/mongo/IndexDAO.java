package dao.mongo;

import java.util.ArrayList;

import model.Word;
import model.Posting;

public class IndexDAO extends BasicDAO {
	
	public IndexDAO() {
		super("Index");
	}

	public ArrayList<Posting> getTopResult(String term, int limit)
	{
		return null;
		
	}
	
	public void addIndex(Word idx)
	{
		
	}
	
	public void getIndex(String term)
	{
		
	}
	
	public void updateIndex(Word idx)
	{
		
	}
	
	public void addPostingToIndex(String term) {
		
	}
}
