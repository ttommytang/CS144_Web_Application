package edu.ucla.cs.cs144;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

public class ItemServlet extends HttpServlet implements Servlet {
       
    public ItemServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // your codes here
        // AuctionSearch as = new AuctionSearch();
        String itemId = request.getParameter("id");
        String xmlStr = AuctionSearch.getXMLDataForItemId(itemId);
        // String[] attributeArray = result.split("\n");
        /*
        PrintWriter out = response.getWriter();

        out.println("<html>");
        out.println("<head><title>Servlet Example</title></head>");
        out.println("<body>" + result.length() + "</body>");
        out.println("</html>");
        out.close();
        */
        ItemInfo item = new ItemInfo();
        try {
            if(!item.xmlParser(xmlStr))
                System.out.print("Error: Parsing!");
        } catch (SAXException | ParserConfigurationException e) {
            e.printStackTrace();
        }

        request.setAttribute("ItemID", item.getItemId());
        request.setAttribute("Name", item.getName());
        request.setAttribute("Category", item.getCategory());
        request.setAttribute("Currently", item.getCurrently());
        request.setAttribute("Buy_Price", item.getBuyPrice());
        request.setAttribute("First_Bid", item.getFirstBid());
        request.setAttribute("Num_Of_Bids", item.getNumOfBids());
        request.setAttribute("BiddersRating", item.getBiddersRating());
        request.setAttribute("BiddersID", item.getBiddersId());
        request.setAttribute("BiddersLoc", item.getBiddersLoc());
        request.setAttribute("BiddersCountry", item.getBiddersCountry());
        request.setAttribute("BidsTime", item.getBidsTime());
        request.setAttribute("BidsAmount", item.getBidsAmount());
        request.setAttribute("Location", item.getLocation());
        request.setAttribute("Latitude", item.getLatitude());
        request.setAttribute("Longitude", item.getLongitude());
        request.setAttribute("Country", item.getCountry());
        request.setAttribute("Started", item.getStarted());
        request.setAttribute("Ends", item.getEnds());
        request.setAttribute("SellerID", item.getSellerId());
        request.setAttribute("SellerRating", item.getSellerRating());
        request.setAttribute("Description", item.getDescription());

        //request.setAttribute("attributes", attributeArray);
        request.getRequestDispatcher("/ItemInfo.jsp").forward(request, response);
    }
}
