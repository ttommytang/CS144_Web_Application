package edu.ucla.cs.cs144;

import java.util.Calendar;
import java.util.Date;

import edu.ucla.cs.cs144.AuctionSearch;
import edu.ucla.cs.cs144.SearchRegion;
import edu.ucla.cs.cs144.SearchResult;

public class AuctionSearchTest {
	public static void main(String[] args1)
	{
		AuctionSearch as = new AuctionSearch();

		String message = "Test message";
		String reply = as.echo(message);
		System.out.println("Reply: " + reply);
		
		String query = "star trek";
		SearchResult[] basicResults = as.basicSearch(query, 0, 1600);
		//System.out.println("Length is " + basicResults.length);
		System.out.println("Basic Seacrh Query: " + query);
		System.out.println("Received " + basicResults.length + " results");
		int count = 0;
		for(SearchResult result : basicResults) {
			//if (result == null) {
				//break;
			//}
			System.out.println(result.getItemId() + ": " + result.getName());
			//System.out.println(count++);
			//count++;
		}

		//System.out.println("Total number is : " + count);

		SearchRegion region =
		    new SearchRegion(33.774, -118.63, 34.201, -117.38); 
		SearchResult[] spatialResults = as.spatialSearch("camera", region, 0, 20);
		System.out.println("Spatial Search");
		System.out.println("Received " + spatialResults.length + " results");
		for(SearchResult result : spatialResults) {
			System.out.println(result.getItemId() + ": " + result.getName());
		}
		/*
		String itemId = "1497595357";
		String item = as.getXMLDataForItemId(itemId);
		System.out.println("XML data for ItemId: " + itemId);
		System.out.println(item);
		*/
		// Add your own test here
	}
}
