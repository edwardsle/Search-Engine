package dao.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DataSource {
//    private static HikariConfig config = new HikariConfig();
//    private static HikariDataSource ds;
//    
//    static {
//        config.setJdbcUrl( "jdbc:mysql://localhost:3306/searchengine" );
//        config.setUsername( "root" );
//        config.setPassword( "fantasy" );
//        config.addDataSourceProperty( "cachePrepStmts" , "true" );
//        config.addDataSourceProperty( "prepStmtCacheSize" , "250" );
//        config.addDataSourceProperty( "prepStmtCacheSqlLimit" , "2048" );
//        ds = new HikariDataSource( config );
//    }
//    
//    private DataSource() {}
//    
//    public static Connection getConnection() throws SQLException {
//        return ds.getConnection();
//    }
	
    private static String dbUser = "root";
    private static String dbPassword = "fantasy";
    private static String loginUrl = "jdbc:mysql://localhost:3306/searchengine4";
    private static Connection cn;
    private Connection instanceConnection;
    
    static {
    	try {
			cn = new DataSource().getInstanceConnection();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    private DataSource() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
    	Class.forName("com.mysql.jdbc.Driver").newInstance();
    	// Connect database
    	instanceConnection = DriverManager.getConnection(loginUrl, dbUser, dbPassword);
    }
	
    
    private Connection getInstanceConnection() {
    	return instanceConnection;
    }
    
    public static Connection getConnection() {
    	return cn;
    }
    
    public static void close() {
    	try {
			cn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
}
