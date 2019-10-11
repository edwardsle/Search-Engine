package dao.sql;

import java.sql.Connection;
import java.sql.SQLException;

public class BasicDAO {
    protected Connection connection = null;
    
    public BasicDAO() throws SQLException {
    	connection = DataSource.getConnection();
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}
}
