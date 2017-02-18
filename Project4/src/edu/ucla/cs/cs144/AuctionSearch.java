package edu.ucla.cs.cs144;

import javax.xml.namespace.QName;

import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.rpc.client.RPCServiceClient;

import edu.ucla.cs.cs144.SearchRegion;
import edu.ucla.cs.cs144.SearchResult;

public class AuctionSearch {
	
	private static final String ENDPOINT_URL =
		"http://oak.cs.ucla.edu/axis2/services/AuctionSearchService";
	private static final String TARGET_NAMESPACE =
		"http://cs144.cs.ucla.edu";
	
	@SuppressWarnings("unused")
	public static SearchResult[] basicSearch(String query, int numResultsToSkip, 
			int numResultsToReturn) {
		try {
			RPCServiceClient rpcClient = new RPCServiceClient();
			Options options = rpcClient.getOptions();
			EndpointReference targetEndpoint = new EndpointReference(ENDPOINT_URL);
			options.setTo(targetEndpoint);
			QName basicSearchOp = new QName(TARGET_NAMESPACE, "basicSearch");
			Object[] parameters = new Object[] { query, new Integer(numResultsToSkip),
					new Integer(numResultsToReturn) };
			Class[] returnTypes = new Class[] { SearchResult[].class };
			Object[] reply = rpcClient.invokeBlocking(basicSearchOp, parameters, returnTypes);
			return (SearchResult[])reply[0];
		} catch(AxisFault e) {
			e.printStackTrace();
		}
		return new SearchResult[0];
	}
	
	@SuppressWarnings("unused")
	public static SearchResult[] spatialSearch(String query, SearchRegion region, 
			int numResultsToSkip, int numResultsToReturn) {
		try {
			RPCServiceClient rpcClient = new RPCServiceClient();
			Options options = rpcClient.getOptions();
			EndpointReference targetEndpoint = new EndpointReference(ENDPOINT_URL);
			options.setTo(targetEndpoint);
			QName spatialSearchOp = new QName(TARGET_NAMESPACE, "spatialSearch");
			Object[] parameters = new Object[] { query, region, new Integer(numResultsToSkip),
					new Integer(numResultsToReturn) };
			Class[] returnTypes = new Class[] { SearchResult[].class };
			Object[] reply = rpcClient.invokeBlocking(spatialSearchOp, parameters, returnTypes);
			return (SearchResult[])reply[0];
		} catch(AxisFault e) {
			e.printStackTrace();
		}
		return new SearchResult[0];
	}
	
	@SuppressWarnings("unused")
	public static String getXMLDataForItemId(String itemId) {
		try {
			RPCServiceClient rpcClient = new RPCServiceClient();
			Options options = rpcClient.getOptions();
			EndpointReference targetEndpoint = new EndpointReference(ENDPOINT_URL);
			options.setTo(targetEndpoint);
			QName getXmlOp = new QName(TARGET_NAMESPACE, "getXMLDataForItemId");
			Object[] parameters = new Object[] { itemId };
			Class[] returnTypes = new Class[] { String.class };
			Object[] reply = rpcClient.invokeBlocking(getXmlOp, parameters, returnTypes);
			return (String)reply[0];
		} catch(AxisFault e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("unused")
	public static String echo(String message) {
		try {
			RPCServiceClient rpcClient = new RPCServiceClient();
			Options options = rpcClient.getOptions();
			EndpointReference targetEndpoint = new EndpointReference(ENDPOINT_URL);
			options.setTo(targetEndpoint);
			QName echoOp = new QName(TARGET_NAMESPACE, "echo");
			Object[] parameters = new Object[] { message };
			Class[] returnTypes = new Class[] { String.class };
			Object[] reply = rpcClient.invokeBlocking(echoOp, parameters, returnTypes);
			return (String)reply[0];
		} catch(AxisFault e) {
			e.printStackTrace();
		}
		return null;
	}

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

		/*
		SearchRegion region =
		    new SearchRegion(33.774, -118.63, 34.201, -117.38);
		SearchResult[] spatialResults = as.spatialSearch("camera", region, 0, 20);
		System.out.println("Spatial Search");
		System.out.println("Received " + spatialResults.length + " results");
		for(SearchResult result : spatialResults) {
			System.out.println(result.getItemId() + ": " + result.getName());
		}
		*/
		/*
		String itemId = "1043397459";
		String item = as.getXMLDataForItemId(itemId);
		System.out.println("XML data for ItemId: " + itemId);
		System.out.println(item);
		*/
		// Add your own test here
	}
}
