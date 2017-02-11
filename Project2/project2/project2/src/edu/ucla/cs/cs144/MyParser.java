/* CS144
 *
 * Parser skeleton for processing item-???.xml files. Must be compiled in
 * JDK 1.5 or above.
 *
 * Instructions:
 *
 * This program processes all files passed on the command line (to parse
 * an entire diectory, type "java MyParser myFiles/*.xml" at the shell).
 *
 * At the point noted below, an individual XML file has been parsed into a
 * DOM Document node. You should fill in code to process the node. Java's
 * interface for the Document Object Model (DOM) is in package
 * org.w3c.dom. The documentation is available online at
 *
 * http://java.sun.com/j2se/1.5.0/docs/api/index.html
 *
 * A tutorial of Java's XML Parsing can be found at:
 *
 * http://java.sun.com/webservices/jaxp/
 *
 * Some auxiliary methods have been written for you. You may find them
 * useful.
 */

package edu.ucla.cs.cs144;

import java.io.*;
import java.text.*;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.*;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;


class MyParser {
    
    static final String columnSeparator = "|*|";
    static DocumentBuilder builder;
    
    static final String[] typeName = {
	"none",
	"Element",
	"Attr",
	"Text",
	"CDATA",
	"EntityRef",
	"Entity",
	"ProcInstr",
	"Comment",
	"Document",
	"DocType",
	"DocFragment",
	"Notation",
    };

    private static BufferedWriter ItemInfo;
    private static BufferedWriter CategoryInfo;
    private static BufferedWriter BidsInfo;
    private static BufferedWriter UserInfo;
    private static BufferedWriter SellerInfo;
    
    static class MyErrorHandler implements ErrorHandler {
        
        public void warning(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void error(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void fatalError(SAXParseException exception)
        throws SAXException {
            exception.printStackTrace();
            System.out.println("There should be no errors " +
                               "in the supplied XML files.");
            System.exit(3);
        }
        
    }
    
    /* Non-recursive (NR) version of Node.getElementsByTagName(...)
     */
    static Element[] getElementsByTagNameNR(Element e, String tagName) {
        Vector< Element > elements = new Vector< Element >();
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
            {
                elements.add( (Element)child );
            }
            child = child.getNextSibling();
        }
        Element[] result = new Element[elements.size()];
        elements.copyInto(result);
        return result;
    }
    
    /* Returns the first subelement of e matching the given tagName, or
     * null if one does not exist. NR means Non-Recursive.
     */
    static Element getElementByTagNameNR(Element e, String tagName) {
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
                return (Element) child;
            child = child.getNextSibling();
        }
        return null;
    }
    
    /* Returns the text associated with the given element (which must have
     * type #PCDATA) as child, or "" if it contains no text.
     */
    static String getElementText(Element e) {
        if (e.getChildNodes().getLength() == 1) {
            Text elementText = (Text) e.getFirstChild();
            return elementText.getNodeValue();
        }
        else
            return "";
    }
    
    /* Returns the text (#PCDATA) associated with the first subelement X
     * of e with the given tagName. If no such X exists or X contains no
     * text, "" is returned. NR means Non-Recursive.
     */
    static String getElementTextByTagNameNR(Element e, String tagName) {
        Element elem = getElementByTagNameNR(e, tagName);
        if (elem != null)
            return getElementText(elem);
        else
            return "";
    }
    
    /* Returns the amount (in XXXXX.xx format) denoted by a money-string
     * like $3,453.23. Returns the input if the input is an empty string.
     */
    static String strip(String money) {
        if (money.equals(""))
            return money;
        else {
            double am = 0.0;
            NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
            try { am = nf.parse(money).doubleValue(); }
            catch (ParseException e) {
                System.out.println("This method should work for all " +
                                   "money values you find in our data.");
                System.exit(20);
            }
            nf.setGroupingUsed(false);
            return nf.format(am).substring(1);
        }
    }
    // Convert input date into the form of YYYY-MM-dd HH:mm:ss

    private static String dateFormat(String input) {
        try {
            SimpleDateFormat inDateFormat = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
            SimpleDateFormat outDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date parsedInDate = inDateFormat.parse(input);
            String formatedDate = "" + outDateFormat.format(parsedInDate);
            //System.out.println(formatedDate);
            return formatedDate;
        }
        catch (ParseException pe) {
            System.out.println("Cannot parse input");
            System.exit(-1);
        }
        return "";
    }

    //Get the information of current item
    private static String getItemInfo(Element item) {
        StringBuilder sb = new StringBuilder();
        //ItemId
        String itemId = item.getAttribute("ItemID");
        sb.append(itemId);
        sb.append(columnSeparator);

        // Name
        String name = getElementTextByTagNameNR(item, "Name");
        sb.append(name);
        sb.append(columnSeparator);

        //Currently
        String currently = strip(MyParser.getElementTextByTagNameNR(item, "Currently"));
        sb.append(currently);
        sb.append(columnSeparator);

        //Buy_Price
        String buyPrice = "";
        Element isBought = getElementByTagNameNR(item, "Buy_Price");
        if (isBought != null) {
            buyPrice = strip(MyParser.getElementText(isBought));

        }
        sb.append(buyPrice);
        sb.append(columnSeparator);

        //First_Bid
        String firstBid = strip(MyParser.getElementTextByTagNameNR(item, "First_Bid"));
        sb.append(firstBid);
        sb.append(columnSeparator);

        //Number_of_Bids
        String numberOfBids = getElementTextByTagNameNR(item, "Number_of_Bids");
        sb.append(numberOfBids);
        sb.append(columnSeparator);


        //System.out.println(description);

        //Started output as yyyy-mm-dd hh:mm:ss
        String startDate = getElementTextByTagNameNR(item, "Started");
        String started = dateFormat(startDate);
        sb.append(started);
        sb.append(columnSeparator);

        // Ends output as yyyy-mm-dd hh:mm:ss
        String endDate = getElementTextByTagNameNR(item, "Ends");
        String ends = dateFormat(endDate);
        //System.out.println(ends);
        sb.append(ends);
        sb.append(columnSeparator);

        //Country
        String country = getElementTextByTagNameNR(item, "Country");
        sb.append(country);
        sb.append(columnSeparator);

        //Location with longitude and latitude
        String location = getElementTextByTagNameNR(item, "Location");
        sb.append(location);
        sb.append(columnSeparator);
        Element loc = getElementByTagNameNR(item, "Location");
        String[] locInfo = new String[2];
        locInfo[0] = loc.getAttribute("Latitude");
        if (locInfo[0] == null) {
            locInfo[0] = "";
        }
        locInfo[1] = loc.getAttribute("Longitude");
        if (locInfo[1] == null) {
            locInfo[1] = "";
        }
        sb.append(locInfo[0]).append(columnSeparator);
        sb.append(locInfo[1]).append(columnSeparator);
        //System.out.println(locInfo[0] + " " + locInfo[1]);

        //Seller ID
        Element seller = getElementByTagNameNR(item, "Seller");
        String sellId = seller.getAttribute("UserID");
        sb.append(sellId);
        sb.append(columnSeparator);


        //Description
        String description = getElementTextByTagNameNR(item, "Description");
        if (description.length() > 4000) {
            description = description.substring(0, 4000);
        }
        sb.append(description);
        sb.append("\n");

        return sb.toString();
    }

    private static List<String> getCategoryInfo(Element item) {
        //ItemID
        String itemId = item.getAttribute("ItemID");
        //Categories
        Element[] categories = getElementsByTagNameNR(item, "Category");
        List<String> categoriesList = new ArrayList<>();
        for (Element category : categories) {
            StringBuilder sb = new StringBuilder();
            sb.append(itemId).append(columnSeparator);
            sb.append(getElementText(category)).append("\n");
            categoriesList.add(sb.toString());
            //System.out.print(sb.toString());
        }
        return categoriesList;
    }

    private static List<String> getBidsInfo(Element item) {
        List<String> result = new ArrayList<>();

        //ItemID
        String itemId = item.getAttribute("ItemID");

        //All Bids of the Item
        Element bids = getElementByTagNameNR(item, "Bids");
        Element[] bid = getElementsByTagNameNR(bids, "Bid");
        for (Element currentBid : bid) {
            //Get the UserId of the current bid
            Element currentBidder = getElementByTagNameNR(currentBid, "Bidder");
            String currentUserId = currentBidder.getAttribute("UserID");
            String bidTime = getElementTextByTagNameNR(currentBid, "Time");
            String bidTimeFormated = dateFormat(bidTime);
            String bidAmount = getElementTextByTagNameNR(currentBid, "Amount");
            bidAmount = strip(bidAmount);

            //Build the String
            StringBuilder sb = new StringBuilder();
            sb.append(itemId).append(columnSeparator);
            sb.append(currentUserId).append(columnSeparator);
            sb.append(bidTimeFormated).append(columnSeparator);
            sb.append(bidAmount).append("\n");
            result.add(sb.toString());
            //System.out.print(sb.toString());
        }
        return result;

    }

    private static List<String> getUserId(Element item) {
        List<String> result = new ArrayList<>();
        Element bids = getElementByTagNameNR(item, "Bids");
        Element[] bid = getElementsByTagNameNR(bids, "Bid");
        for (Element currentBid : bid) {
            Element currentBidder = getElementByTagNameNR(currentBid, "Bidder");
            String currentUserId = currentBidder.getAttribute("UserID");
            result.add(currentUserId);
        }
        return result;
    }

    private static String getUserInfo(Element item, String id) {
        StringBuilder sb = new StringBuilder();
        Element bids = getElementByTagNameNR(item, "Bids");
        Element[] bid = getElementsByTagNameNR(bids, "Bid");
        for (Element currentBid : bid) {
            Element currentBidder = getElementByTagNameNR(currentBid, "Bidder");
            String currentUserId = currentBidder.getAttribute("UserID");
            if (id.equals(currentUserId)) {
                //Get User Info
                String rating = currentBidder.getAttribute("Rating");
                String location = getElementTextByTagNameNR(currentBidder, "Location");
                String country = getElementTextByTagNameNR(currentBidder, "Country");
                //Build User Info String
                sb.append(id).append(columnSeparator);
                sb.append(rating).append(columnSeparator);
                sb.append(location).append(columnSeparator);
                sb.append(country).append("\n");
                break;
            }
        }
        //System.out.print(sb.toString());
        return sb.toString();
    }

    private static String getSellerId(Element item) {
        Element seller = getElementByTagNameNR(item, "Seller");
        return seller.getAttribute("UserID");

    }

    private static String getSellerInfo(Element item) {
        Element seller = getElementByTagNameNR(item, "Seller");
        String id = seller.getAttribute("UserID");
        String rating = seller.getAttribute("Rating");
        StringBuilder sb = new StringBuilder();
        sb.append(id).append(columnSeparator);
        sb.append(rating).append("\n");
        return sb.toString();
    }



    
    /* Process one items-???.xml file.
     */
    static void processFile(File xmlFile) {
        Document doc = null;
        try {
            doc = builder.parse(xmlFile);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(3);
        }
        catch (SAXException e) {
            System.out.println("Parsing error on file " + xmlFile);
            System.out.println("  (not supposed to happen with supplied XML files)");
            e.printStackTrace();
            System.exit(3);
        }
        
        /* At this point 'doc' contains a DOM representation of an 'Items' XML
         * file. Use doc.getDocumentElement() to get the root Element. */
        System.out.println("Successfully parsed - " + xmlFile);
        Element root = doc.getDocumentElement();
        //System.out.println("Root element : " + root.getNodeName());
        /* Fill in code here (you will probably need to write auxiliary
            methods). */



        Element[] items = getElementsByTagNameNR(root, "Item");
        //System.out.println(items.length);
        //int count = 1;

        Set<String> userIdSet = new HashSet<>();
        Set<String> sellerIdSet = new HashSet<>();
        for (Element item : items) {
            // Build ItemInfo Table
            String currentItemInfo = getItemInfo(item);
            try {
                ItemInfo.write(currentItemInfo);




            }
            catch (IOException ioe) {
                ioe.printStackTrace();
            }

            //Build CategoryInfo table

            List<String> currentCategoryInfo = getCategoryInfo(item);
            try {
                for (String oneCategory : currentCategoryInfo) {
                    CategoryInfo.write(oneCategory);

                }
            }
            catch (IOException ioe) {
                ioe.printStackTrace();
            }

            //Build BidInfo table
            List<String> currentBidsInfo = getBidsInfo(item);
            try {
                for (String oneBidInfo : currentBidsInfo) {
                    BidsInfo.write(oneBidInfo);
                }
            }
            catch (IOException ioe) {
                ioe.printStackTrace();
            }

            //Build UseInfo table, use Hash Set to avoid duplicates

            //get the userIDs of bids on the current item
            List<String> IDs = getUserId(item);
            for (String id : IDs) {
                if (userIdSet.contains(id)) {
                    continue;
                }
                userIdSet.add(id);
                String currentUserInfo = getUserInfo(item, id);
                try {
                    UserInfo.write(currentUserInfo);
                }
                catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }

            //Build SellerInfo table, Use Hash Set to eliminate dups
            String currentSellerId = getSellerId(item);
            if (!sellerIdSet.contains(currentSellerId)) {
                sellerIdSet.add(currentSellerId);
                String currentSellerInfo = getSellerInfo(item);
                try {
                    //System.out.print(currentSellerInfo);
                    SellerInfo.write(currentSellerInfo);
                }
                catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }


        }



        /**************************************************************/
        
    }



    public static void main (String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java MyParser [file] [file] ...");
            System.exit(1);
        }
        
        /* Initialize parser. */
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setIgnoringElementContentWhitespace(true);      
            builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new MyErrorHandler());
        }
        catch (FactoryConfigurationError e) {
            System.out.println("unable to get a document builder factory");
            System.exit(2);
        } 
        catch (ParserConfigurationException e) {
            System.out.println("parser was unable to be configured");
            System.exit(2);
        }
        
        /* Process all files listed on command line. */
        try {
            ItemInfo = new BufferedWriter(new FileWriter("ItemInfo.dat"));
            CategoryInfo = new BufferedWriter( new FileWriter("CategoryInfo.dat"));
            BidsInfo = new BufferedWriter(new FileWriter("BidsInfo.dat"));
            UserInfo = new BufferedWriter(new FileWriter("UserInfo.dat"));
            SellerInfo = new BufferedWriter(new FileWriter("SellerInfo.dat"));
            for (int i = 0; i < args.length; i++) {
                File currentFile = new File(args[i]);
                processFile(currentFile);
            }
            ItemInfo.close();
            CategoryInfo.close();
            BidsInfo.close();
            UserInfo.close();
            SellerInfo.close();
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }


}
