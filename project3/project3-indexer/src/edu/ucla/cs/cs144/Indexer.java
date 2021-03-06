package edu.ucla.cs.cs144;

import java.util.*;
import java.io.IOException;
import java.io.StringReader;
import java.io.File;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

//mport static com.sun.tools.classfile.AccessFlags.Kind.Field;
//import static sun.rmi.transport.TransportConstants.Version;

public class Indexer {
    
    /** Creates a new instance of Indexer */
    public Indexer() {
    }

    private IndexWriter indexWriter = null;

    public IndexWriter getIndexWriter() throws IOException {
    	if (indexWriter == null) {
    		Directory indexDir = FSDirectory.open(new File("/var/lib/lucene/index/index3"));
    		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_2, new StandardAnalyzer());
    		indexWriter = new IndexWriter(indexDir,config);
		}
		return indexWriter;
	}

	public void closeIndexWriter() throws IOException {
		if (indexWriter != null) {
			indexWriter.close();
		}
	}
 
    public void rebuildIndexes() {

        Connection conn = null;

        // create a connection to the database to retrieve Items from MySQL
		try {
	    conn = DbManager.getConnection(true);
		} catch (SQLException ex) {
	    	ex.printStackTrace();

		}


	/*
	 * Add your code here to retrieve Items using the connection
	 * and add corresponding entries to your Lucene inverted indexes.
         *
         * You will have to use JDBC API to retrieve MySQL data from Java.
         * Read our tutorial on JDBC if you do not know how to use JDBC.
         *
         * You will also have to use Lucene IndexWriter and Document
         * classes to create an index and populate it with Items data.
         * Read our tutorial on Lucene as well if you don't know how.
         *
         * As part of this development, you may want to add 
         * new methods and create additional Java classes. 
         * If you create new classes, make sure that
         * the classes become part of "edu.ucla.cs.cs144" package
         * and place your class source files at src/edu/ucla/cs/cs144/.
	 * 
	 */
		// acquire Id, Name and Description from DB
		try {
			String ItemQuery = "select ItemId, Name, Description from ItemInfo";
			// acquire Category using Id attribute
			String CategoryQuery = "select Category from CategoryInfo where ItemId = ?";
			Statement stmt = conn.createStatement();
			PreparedStatement ps = conn.prepareStatement(CategoryQuery);
			ResultSet rs = stmt.executeQuery(ItemQuery);


			//getIndexWriter();
			while (rs.next()) {

				String itemId = rs.getString("ItemId");
				String category = getCategory(ps, itemId, CategoryQuery);
				CharSequence cs = "kitchenware";
				if (category.contains(cs)) {
					System.out.println(itemId + " " + category);
				}
				String name = rs.getString("Name");
				String description = rs.getString("Description");
				indexItem(itemId, category, name, description);
			}


			// close the database connection
			conn.close();
			rs.close();
			stmt.close();
			ps.close();
			closeIndexWriter();

		}
		catch (IOException | SQLException ioe) {
			ioe.printStackTrace();
		}


	}

    private void indexItem(String itemId, String category, String name, String description) throws IOException {
    	IndexWriter writer = getIndexWriter();
    	Document doc = new Document();
    	doc.add(new StringField("itemId", itemId, Field.Store.YES));
    	doc.add(new TextField("category", category, Field.Store.YES));
		doc.add(new TextField("name", name, Field.Store.YES));
		doc.add(new TextField("description", description, Field.Store.YES));
		//doc.add(new TextField("category", category, Field.Store.YES));
		String fullText = itemId + " " + category + " " +
				name + " " + description;
		doc.add(new TextField("content", fullText, Field.Store.NO));
		writer.addDocument(doc);
	}

	//use ps and ItemId to retrieve the corresponding Categories for the certain Item
    private String getCategory(PreparedStatement ps, String ItemId, String query) {
		StringBuilder sb = new StringBuilder();
    	try {
			ps.setString(1, ItemId);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				sb.append(rs.getString("Category")).append(" ");
			}

		} catch (SQLException se) {
    		se.printStackTrace();
		}
		return sb.toString();
	}

    public static void main(String args[]) {
        Indexer idx = new Indexer();
        idx.rebuildIndexes();
    }   
}
