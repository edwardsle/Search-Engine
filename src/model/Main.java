package model;

import java.sql.SQLException;
import java.util.ArrayList;

import dao.sql.SearchDAO;

public class Main {

	public static void main(String[] args) {
		try {
			SearchDAO search = new SearchDAO();
			ArrayList<SearchResult> results = search.search("Trending jobs in artificial intelligent", 20);
			
			for (SearchResult result : results)
			{
				System.out.println(result.getDocId() + " - " + result.getScore() + " - " + result.getMatchedKeywords() + " - " + result.getUrl());
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}

}
