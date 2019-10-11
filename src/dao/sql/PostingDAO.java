package dao.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Posting;

public class PostingDAO extends BasicDAO {
	

	private DocDAO docDao;

	public PostingDAO() throws SQLException {
		super();
		docDao = new DocDAO();
	}

	public void addPostingsToWord(String term, ArrayList<Posting> postings) throws SQLException {
		if (!postings.isEmpty())
		{
			PreparedStatement postStatement = connection.prepareStatement("INSERT INTO postings (word_id, doc_id, score, tf) VALUES (?,?,?,?)");
			for (Posting posting : postings)
			{
				postStatement.setString(1, term);
				postStatement.setString(2, posting.getDocId());
				postStatement.setDouble(3, posting.getScore());
				postStatement.setInt(4, posting.getTf());
				postStatement.addBatch();
			}
			postStatement.executeBatch();
			postStatement.close();
		}
	}
}
