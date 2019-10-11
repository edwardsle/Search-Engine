package dao.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import model.Word;

public class WordDAO extends BasicDAO {
	
	private PostingDAO postingDao;

	public WordDAO() throws SQLException {
		super();
		postingDao = new PostingDAO();
	}

	public void addWords(Collection<Word> words) throws SQLException {
		connection.setAutoCommit(false);
		
		
		PreparedStatement statement = connection.prepareStatement("INSERT INTO words (word, idf) VALUES (?, ?)");
		for (Word word : words)
		{
			statement.setString(1, word.getTerm());
			statement.setDouble(2, word.getIdf());
			statement.addBatch();
		}
		statement.executeBatch();
		statement.close();
		connection.commit();
		
		int i = 0;
		for (Word word : words)
		{
			i++;
//			int id = getGeneratedId(word);
			postingDao.addPostingsToWord(word.getTerm(), word.getPostings());
//			System.out.println("Add postings list to word: " + i);
			if (i % 1000 == 0)
			{
				connection.commit();
				System.out.println("Commited adding postings to " + i + " word(s)");
			}
		}
		
		connection.setAutoCommit(true);
	}
	


	private int addOrGet(Word word) throws SQLException {
		int id = getGeneratedId(word);

		if (id > 0)
			return id;

		PreparedStatement statement = connection.prepareStatement("INSERT INTO words (word, idf) VALUES (?, ?)");
		statement.setString(1, word.getTerm());
		statement.setDouble(2, word.getIdf());
		int updateCount = statement.executeUpdate();
		statement.close();

		if (updateCount > 0)
			return getGeneratedId(word);

		return -1;
	}

	private int getGeneratedId(Word word) throws SQLException {
		PreparedStatement statement = connection.prepareStatement("SELECT id FROM words WHERE word = ?");
		statement.setString(1, word.getTerm());
		ResultSet rs = statement.executeQuery();

		if (rs.first()) {
			int id = rs.getInt("id");
			statement.close();
			return id;
		}

		statement.close();
		return -1;
	}
}
