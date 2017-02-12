package edu.ucla.cs.cs144;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.util.*;
import java.text.SimpleDateFormat;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.lucene.document.Document;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import edu.ucla.cs.cs144.DbManager;
import edu.ucla.cs.cs144.SearchRegion;
import edu.ucla.cs.cs144.SearchResult;

public class AuctionSearch implements IAuctionSearch {

	/* 
         * You will probably have to use JDBC to access MySQL data
         * Lucene IndexSearcher class to lookup Lucene index.
         * Read the corresponding tutorial to learn about how to use these.
         *
	 * You may create helper functions or classes to simplify writing these
	 * methods. Make sure that your helper functions are not public,
         * so that they are not exposed to outside of this class.
         *
         * Any new classes that you create should be part of
         * edu.ucla.cs.cs144 package and their source files should be
         * placed at src/edu/ucla/cs/cs144.
         *
         */
	private class SearchEngine {

		private IndexSearcher searcher = null;
		private QueryParser parser = null;

		public SearchEngine() throws IOException {
			//System.out.println("im here");
			searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File("/var/lib/lucene/index5"))));

			parser = new QueryParser("content", new StandardAnalyzer());
		}

		public TopDocs performSearch(String queryString, int n) throws IOException, ParseException {
			Query query = parser.parse(queryString);
			return searcher.search(query, n);
		}

		public Document getDocument(int docId) throws IOException {
			return searcher.doc(docId);
		}

	}
	public SearchResult[] basicSearch(String query, int numResultsToSkip, 
			int numResultsToReturn) {
		// TODO: Your code here!

		//SearchResult[] result = new SearchResult[numResultsToReturn];
		List<SearchResult> result = new ArrayList<>();
		if (numResultsToReturn == 0) {
			return result.toArray(new SearchResult[0]);
		}

		try {

			SearchEngine se = new SearchEngine();


			int totalNum = numResultsToReturn + numResultsToSkip;
			TopDocs topDocs = se.performSearch(query, totalNum);
			ScoreDoc[] hits = topDocs.scoreDocs;

			//int pos = 0;
			//System.out.println("Matching result has " + hits.length + " of records");
			for (int i = numResultsToSkip; i < totalNum && i < hits.length; i++) {
				Document doc = se.getDocument(hits[i].doc);
				//System.out.println(doc.get("itemId") + " " + doc.get("name"));
				result.add(new SearchResult(doc.get("itemId"), doc.get("name")));
			}
			//System.out.println("Size of result is " + result.size());



		} catch (Exception e) {
			System.out.println("Exception caught.\n");
			e.printStackTrace();
		}
		return result.toArray(new SearchResult[0]);

	}

	public SearchResult[] spatialSearch(String query, SearchRegion region,
			int numResultsToSkip, int numResultsToReturn) {
		// TODO: Your code here!
		if (numResultsToReturn == 0) {
			return new SearchResult[0];
		}

		List<SearchResult> result = new ArrayList<>();
		//convert search region into coordinates representation
		double leftDownX = region.getLx();
		double leftDownY = region.getLy();
		double rightDownX = region.getLx();
		double rightDownY = region.getRy();
		double rightUpX = region.getRx();
		double rightUpY = region.getRy();
		double leftUpX = region.getRx();
		double leftUpY = region.getLy();
		StringBuilder sb = new StringBuilder();
		sb.append(leftDownX + " " + leftDownY + ", ");
		sb.append(rightDownX + " " + rightDownY + ", ");
		sb.append(rightUpX + " " + rightUpY + ", ");
		sb.append(leftUpX + " " + leftUpY + ", ");
		sb.append(leftDownX + " " + leftDownY);

		try {
			Set<String> set = getItemWithinRegion(sb.toString());
			System.out.println(set.size());
			SearchEngine se = new SearchEngine();
			int totalNum = numResultsToReturn + numResultsToSkip;
			TopDocs topDocs = se.performSearch(query, totalNum);
			ScoreDoc[] hits = topDocs.scoreDocs;
			for (int i = numResultsToSkip; i < totalNum && i < hits.length; i++) {
				Document doc = se.getDocument(hits[i].doc);
				//System.out.println(doc.get("itemId") + " " + doc.get("name"));
				String id = doc.get("itemId");
				String name = doc.get("name");
				if (set.contains(id)) {
					result.add(new SearchResult(id, name));
				}
			}

		} catch (Exception e) {
			System.out.println("Exception caught.\n");
			e.printStackTrace();
		}
		return result.toArray(new SearchResult[0]);


	}
	// filter the tuples and keep those which are within the region defined as s, return the set of Itemid of the tuples
	private Set<String> getItemWithinRegion(String s) {
		Set<String> result = new HashSet<>();
		String query = "SELECT ItemId \n FROM LocationInfo \n WHERE MBRContains(GeomFromText ('Polygon((" + s + "))'), Location)";
		//System.out.println(query);
		Connection conn = null;

		// create a connection to the database to retrieve Items from MySQL
		try {
			conn = DbManager.getConnection(true);
		} catch (SQLException ex) {
			System.out.println(ex);

		}
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {

				String id = rs.getString("ItemId");
				result.add(id);
			}
			conn.close();
			rs.close();
		} catch (SQLException se) {
			se.printStackTrace();
		}
		return result;
	}

	public String getXMLDataForItemId(String itemId) {
		// TODO: Your code here!
		return "";
	}
	
	public String echo(String message) {
		return message;
	}

}
