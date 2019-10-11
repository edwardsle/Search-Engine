package dao.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import model.Doc;

public class DocDAO extends BasicDAO {

	public DocDAO() throws SQLException {
		super();
	}
	
	public void addDocs(Collection<Doc> docs) throws SQLException {
		connection.setAutoCommit(false);
		PreparedStatement statement = connection.prepareStatement("INSERT INTO docs (docId, url) VALUES (?, ?)");
		for (Doc doc : docs)
		{
			statement.setString(1, doc.getDocId());
			statement.setString(2, doc.getUrl());
			statement.addBatch();
		}
		statement.executeBatch();
		statement.close();
		connection.commit();
		connection.setAutoCommit(true);
	}

	private void addDoc(Doc doc) throws SQLException {
		
	}

	public int getOrAdd(String docId, String url) throws SQLException {
		int id = getGeneratedId(docId);
				
		if (id > 0)
				return id;
		
		if (url == null)
			url = "";
	
		PreparedStatement statement = connection.prepareStatement("INSERT INTO docs (docId, url) VALUES (?, ?)");
		statement.setString(1, docId);
		statement.setString(2, url);
		int updateCount = statement.executeUpdate();
		statement.close();
		
		if (updateCount > 0)
			return getGeneratedId(docId);
		
		return -1;
	}
	
	public int getGeneratedId(String docId) throws SQLException {
		PreparedStatement statement = connection.prepareStatement("SELECT id FROM docs WHERE docId = ? LIMIT 1");
		statement.setString(1, docId);
		ResultSet rs = statement.executeQuery();
		
		if (rs.first())
		{
			int id = rs.getInt("id");
			statement.close();
			return id;
		}
		
		return -1;
	}
}
