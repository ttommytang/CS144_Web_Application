package edu.ucla.cs.cs144;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import edu.ucla.cs.cs144.AuctionSearch;
import edu.ucla.cs.cs144.SearchRegion;
import edu.ucla.cs.cs144.SearchResult;

public class AuctionSearchTest {
	public static void main(String[] args1) throws IOException {
		AuctionSearch as = new AuctionSearch();
//
		String message = "Test message";
		String reply = as.echo(message);
		System.out.println("Reply: " + reply);

		String query = "camera";
		SearchResult[] basicResults = as.basicSearch(query, 0, 20);
		//System.out.println("Length is " + basicResults.length);
		System.out.println("Basic Seacrh Query: " + query);
		System.out.println("Received " + basicResults.length + " results");

		java.io.FileWriter fw_basic = new java.io.FileWriter("Test_basic.xml");
		fw_basic.write("Query = \"" + query + "\"\n");
		fw_basic.write("Received " + basicResults.length + " results");

		for(SearchResult result : basicResults) {
			//if (result == null) {
				//break;
			//}
			System.out.println(result.getItemId() + ": " + result.getName());
			//System.out.print(as.getXMLDataForItemId(result.getItemId()));
			fw_basic.write(as.getXMLDataForItemId(result.getItemId()));
			//System.out.println(count++);
			//count++;
		}

		fw_basic.close();

		System.out.println("=================================================\n");
		System.out.println("=================================================\n");

		java.io.FileWriter fw_spatial = new java.io.FileWriter("Test_spatial.xml");

		SearchRegion region =
		    new SearchRegion(13.774, -88.63, 44.201, -120.38);
		SearchResult[] spatialResults = as.spatialSearch(query, region, 0, 20);
		System.out.println("Spatial Search");
		fw_spatial.write("Basic Seacrh Query = \"" + query + "\"\n");
		fw_spatial.write("Received " + spatialResults.length + " results");
		System.out.println("Basic Seacrh Query: " + query);
		System.out.println("Received " + spatialResults.length + " results");
		for(SearchResult result : spatialResults) {
			System.out.println(result.getItemId() + ": " + result.getName());
			fw_spatial.write(as.getXMLDataForItemId(result.getItemId()));
		}

//		String testId = "1045310980";
//		System.out.print(as.getXMLDataForItemId(testId));
		/*
		String itemId = "1497595357";
		String item = as.getXMLDataForItemId(itemId);
		System.out.println("XML data for ItemId: " + itemId);
		System.out.println(item);
		*/
		// Add your own test here
	}

}
