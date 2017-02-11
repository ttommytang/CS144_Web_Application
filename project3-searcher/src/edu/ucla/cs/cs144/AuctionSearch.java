package edu.ucla.cs.cs144;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
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
			// searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File("/var/lib/lucene/index"))));
			searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(Paths.get("/var/lib/lucene/index"))));
			parser = new QueryParser("content", new StandardAnalyzer());
		}

		public TopDocs performSearch(String queryString, int n)
				throws IOException, ParseException {
			Query query = parser.parse(queryString);
			return searcher.search(query, n);
		}

		public Document getDocument(int docId)
				throws IOException {
			return searcher.doc(docId);
		}

	}
	public SearchResult[] basicSearch(String query, int numResultsToSkip, 
			int numResultsToReturn) {
		// TODO: Your code here!
		SearchResult[] result = new SearchResult[numResultsToReturn];
		if (numResultsToReturn == 0) {
			return result;
		}
		try {
			SearchEngine se = new SearchEngine();
			int totalNum = numResultsToReturn + numResultsToSkip;
			TopDocs topDocs = se.performSearch(query, totalNum);
			ScoreDoc[] hits = topDocs.scoreDocs;
			int pos = 0;
			for (int i = numResultsToSkip; i < totalNum; i++) {
				Document doc = se.getDocument(hits[i].doc);
				result[pos++] = new SearchResult(doc.get("itemId"), doc.get("name"));
			}

		} catch (Exception e) {
			System.out.println("Exception caught.\n");
			e.printStackTrace();
		}
		return result;
    }

	public SearchResult[] spatialSearch(String query, SearchRegion region,
			int numResultsToSkip, int numResultsToReturn) {
		// TODO: Your code here!
		return new SearchResult[0];
	}

	public String getXMLDataForItemId(String itemId) {
		// TODO: Your code here!
		return "";
	}
	
	public String echo(String message) {
		return message;
	}

}
