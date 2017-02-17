package edu.ucla.cs.cs144;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.sql.*;
import java.util.*;
import java.text.SimpleDateFormat;

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
			searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File("/var/lib/lucene/index/index2"))));

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

			//System.out.println("Matching result has " + hits.length + " of records");
			for (int i = numResultsToSkip; i < totalNum && i < hits.length; i++) {
			    try {
                    Document doc = se.getDocument(hits[i].doc);
                    //System.out.println(doc.get("itemId") + " " + doc.get("name"));
                    result.add(new SearchResult(doc.get("itemId"), doc.get("name")));
                } catch (Exception e) {
			        break;
                }
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
			ex.printStackTrace();
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

    /**
     * This helper function take in the date String in the format of yyyy-MM-dd HH:mm:ss
     * and produce a new date string in the format of MMM-dd-yy HH:mm:ss.
     * @param input date string retrived from the table.
     * @return return the date string in the format of MMM-dd-yy HH:mm:ss.
     */
	private static String dateFormat(String input) {
		try {
			SimpleDateFormat outDateFormat = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
			SimpleDateFormat inDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			java.util.Date parsedInDate = inDateFormat.parse(input);
            //System.out.println(formatedDate);
			return "" + outDateFormat.format(parsedInDate);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		return "";
	}

	public String getXMLDataForItemId(String itemId){
		// TODO: Your code here!
		Connection conn = null;

		// create a connection to the database to retrieve Items from MySQL
		try {
			conn = DbManager.getConnection(true);
		} catch (SQLException ex) {
			ex.printStackTrace();

		}
		String xmlout = "<Item";

		try{
			String ItemQuery = "SELECT * FROM ItemInfo WHERE ItemId = " + itemId;
			String categoryQuery = "SELECT Category FROM CategoryInfo WHERE ItemId = " + itemId;
			String bidsQuery = "SELECT * FROM BidsInfo WHERE ItemId = " + itemId;
			String userQuery = "SELECT * FROM UserInfo WHERE UserId = ?";
			String sellerQuery = "SELECT Rating from SellerInfo WHERE SellerId = ?";

			String[] fields = new String[]{"Name", "Category", "Currently", "Buy_Price", "First_Bid", "Number_of_Bids", "Bids",
			"Location", "Country", "Started", "Ends", "Seller", "Description"};

			PreparedStatement ps_item = conn.prepareStatement(ItemQuery);
			PreparedStatement ps_bids = conn.prepareStatement(bidsQuery);
			PreparedStatement ps_cate = conn.prepareStatement(categoryQuery);
			PreparedStatement ps_user = conn.prepareStatement(userQuery);
			PreparedStatement ps_seller = conn.prepareStatement(sellerQuery);

			//
			ResultSet rs = ps_item.executeQuery();
			// If there is no match item with the input ItemId, just return an empty xml tag.
			if(!rs.next()) {
				return xmlout + " />";
				// return "<Item />";
			}

			xmlout = xmlout + " ItemID=\"" + itemId + "\">\n";

			for(String attr : fields) {
                switch (attr) {
                    case "Name":
                    case "Number_of_Bids":
                    case "Country":
                    case "Description":
                        xmlout += "  <" + attr + ">" + rs.getString(attr) + "</" + attr + ">\n";
                        break;
                    case "Currently":
                    case "First_Bid":
                        xmlout += "  <" + attr + ">$" + rs.getString(attr) + "</" + attr + ">\n";
                        break;
                    case "Buy_Price":
                        if (!rs.getString(attr).equals("0.00")) {
                            xmlout += "  <" + attr + ">$" + rs.getString(attr) + "</" + attr + ">\n";
                        }
                        break;
                    case "Category":
                        ResultSet rs_cate = ps_cate.executeQuery();
                        while (rs_cate.next()) {
                            xmlout += "  <Category>" + rs_cate.getString(attr) + "</Category>\n";
                        }
                        break;
                    case "Location":
                        xmlout += "  <Location";
                        if ((!rs.getString("Latitude").equals("")) && (!rs.getString("Longitude").equals(""))) {
                            xmlout += " Latitude=\"" + rs.getString("Latitude") + "\" Longitude=\"" +
                                    rs.getString("Longitude") + "\">";
                        }
                        xmlout += rs.getString("Location") + "</Location>\n";
                        break;
                    case "Started":
                    case "Ends":
                        xmlout += "  <" + attr + ">" + dateFormat(rs.getString(attr)) + "</" + attr + ">\n";
                        break;
                    case "Seller":
                        String SellerId = rs.getString("SellerId");
                        ps_seller.setString(1, SellerId);
                        ResultSet rs_seller = ps_seller.executeQuery();
                        if(rs_seller.next()) {
                            xmlout += "  <Seller Rating=\"" + rs_seller.getString("Rating") + "\" UserID=\"" +
                                    SellerId + "\" />\n";
                        }
                        break;
                    case "Bids":
                        xmlout = writeBids(xmlout, ps_bids, ps_user);
                        break;
                }
			}
			xmlout += "</Item>";

		} catch (SQLException se) {
			se.printStackTrace();
		}
		return xmlout;
	}

    /** This helper function writes to xml-representative string the bids information retrieved using the two
     * passed in query handler for BidsInfo and UserInfo tables.
     * @param xmlStr current xml-representative string.
     * @param ps_bids query handler for BidsInfo table.
     * @param ps_user query handler for UserInfo table.
     */
	private String writeBids(String xmlStr, PreparedStatement ps_bids, PreparedStatement ps_user) {
	    xmlStr += "  <Bids";
	    try {
            ResultSet bids = ps_bids.executeQuery();

            // No bids information, just write empty and return.
            if(!bids.next()) {
                xmlStr += " />\n";
                return xmlStr;
            }
            bids.beforeFirst();

            // Traver all the bids in the ResultSet, write information into the string.
            xmlStr += ">\n";
            while(bids.next()) {
                xmlStr += "    <Bid>\n";
                String bidderId = bids.getString("UserId");
                ps_user.setString(1, bidderId);
                ResultSet rs_user = ps_user.executeQuery();
                if(rs_user.next()) {
                    xmlStr += "      <Bidder Rating=\"" + rs_user.getString("Rating") + "\" UserID=\"" +
                            bidderId + "\">\n";
                    xmlStr += "        <Location>" + rs_user.getString("Location") + "</Location>\n";
                    xmlStr += "        <Country>" + rs_user.getString("Country") + "</Country>\n";
                    xmlStr += "      </Bidder>\n";
                }
                xmlStr += "      <Time>" + dateFormat(bids.getString("Time")) + "</Time>\n";
                xmlStr += "      <Amount>$" + bids.getString("Amount") + "</Amount>\n";
                xmlStr += "    </Bid>\n";
            }
            xmlStr += "  </Bids>\n";

        } catch (SQLException se) {
	        se.printStackTrace();
        }
        return xmlStr;

    }
	
	public String echo(String message) {
		return message;
	}

}
