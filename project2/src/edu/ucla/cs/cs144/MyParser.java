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
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;


class MyParser {
    
    static final String columnSeparator = "|*|";
    static DocumentBuilder builder;
    private static BufferedWriter Items;
    private static BufferedWriter Bids;
    private static BufferedWriter Sellers;
    private static BufferedWriter Bidders;
    private static BufferedWriter Categories;
    
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

    /*
    Returns the formated date(in yyyy-MM-dd HH:mm:ss format) denoted by a
    date string in the format of MMM-dd-yy HH:mm:ss. Returns the input if
    the input is empty String;
     */
    static String formatDate(String dateRaw) {
        if(dateRaw.equals("")) {
            return dateRaw;
        } else {
            Date parsed = null;
            SimpleDateFormat xmlFormat = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
            SimpleDateFormat tsFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            try {
                parsed = xmlFormat.parse(dateRaw);
            } catch (ParseException pe) {
                System.out.println("ERROR: Failed parsing date \"" + dateRaw + "\"");
            }

            return tsFormat.format(parsed);
        }
    }

    /*
    Helper function to process the location element and returns the location String
    (location_name + latitude + longitude), where latitude and longitude will be left
    as "" if not specified.
     */
    static String formatLocation(Element location) throws IOException{
        if(location == null) {
            return columnSeparator+columnSeparator;
        }

        String loc_name = getElementText(location);
        String latitude = location.getAttribute("Latitude");
        if(latitude == null) {
            latitude = "";
        }
        String longitude = location.getAttribute("Longitude");
        if(longitude == null) {
            longitude = "";
        }

        return concatString(loc_name, latitude, longitude);
    }

    /*
    Auxiliary function: Process the bids in the passed in item, and transfer into
    load information for the 'Items' relation:
    Bid(*itemID*, *bidderID*, time, amount)
     */
    static void processBids(Element item) throws IOException{
        String itemID = item.getAttribute("ItemID");
        Element[] bids = getElementsByTagNameNR(getElementByTagNameNR(item, "Bids"), "Bid");
        for(Element bid : bids) {
            Element bidder = getElementByTagNameNR(bid, "Bidder");
            String bidderID = bidder == null ? "" : bidder.getAttribute("UserID");
            String time = getElementTextByTagNameNR(bid, "Time");
            String formatedTime = formatDate(time);

            String amountRaw = getElementTextByTagNameNR(bid, "Amount");
            String amount = strip(amountRaw);

            String concated = concatString(itemID, bidderID, formatedTime, amount);
            writeFile(Bids, concated);

        }

    }

    /*
    Auxiliary function: Process the seller information in the passed in item, and transfer
    into the load information for the 'Sellers' relation:
    Sellers(*UserID*, rating)
     */
    static void processSeller(Element item) throws IOException {
        Element seller = getElementByTagNameNR(item, "Seller");

        // Extract the ID and rating attributes and write into the buffer.
        String sellerID = seller == null ? "" : seller.getAttribute("UserID");
        String sellerRating = seller == null ? "" : seller.getAttribute("Rating");

        String concated = concatString(sellerID, sellerRating);
        writeFile(Sellers, concated);
    }

    /*
    Auxiliary function: Process the bidder information in the passed in item, and transfer
    into the load information for the 'Bidders' relation:
    Bidders(*UserID*, rating, Location, Country)
     */
    static void processBidders(Element item) throws IOException {
        Element[] bids = getElementsByTagNameNR(getElementByTagNameNR(item, "Bids"), "Bid");
        for(Element bid : bids) {
            Element bidder = getElementByTagNameNR(bid, "Bidder");
            String bidderID = bidder == null ? "" : bidder.getAttribute("UserID");
            String bidderRating = bidder == null ? "" : bidder.getAttribute("Rating");

            String location = getElementTextByTagNameNR(bidder, "Location");
            if(location == null) {
                location = "";
            }
            String country = getElementTextByTagNameNR(bidder, "Country");
            if(country == null) {
                country = "";
            }

            String concated = concatString(bidderID, bidderRating, location, country);
            writeFile(Bidders, concated);
        }
    }


    /* Auxiliary function: Process the Category information in the passed in item, and transfer
    into the load information for the 'Category' relation:
    Category(*itemID*, *category*)
     */
    static void processCategory(Element item) throws IOException {
        Element[] categories = getElementsByTagNameNR(item, "Category");
        String ItemID = item.getAttribute("ItemID");
        for(Element e : categories) {
            String category = getElementText(e);
            String concated = concatString(ItemID, category);
            writeFile(Categories, concated);
        }

    }

    /*
    Auxiliary function: Process the basic information in the passed in item, which should transfer
     into the load information for the 'Items' relation:
     Items(*ItemID*, name, currently, buy-price, first-bid, # of bids, Location, Latitude, Longitude,
     Country, Started, Ends, SellerID, Description)
     */
    static void processItem(Element item) throws IOException{
        try {
            String itemID = item.getAttribute("ItemID");
            String name = getElementTextByTagNameNR(item, "Name");
            String currently = strip(getElementTextByTagNameNR(item, "Currently"));
            String buy_price = strip(getElementTextByTagNameNR(item, "Buy_price"));
            String first_bid = strip(getElementTextByTagNameNR(item, "First_bid"));
            String numofbids = getElementTextByTagNameNR(item, "Number_of_Bids");
            Element location = getElementByTagNameNR(item, "Location");
            String locationInfo = formatLocation(location);
            String country = getElementTextByTagNameNR(item, "Country");
            String started = formatDate(getElementTextByTagNameNR(item, "Started"));
            String ends = formatDate(getElementTextByTagNameNR(item, "Ends"));
            Element seller = getElementByTagNameNR(item, "Seller");
            String sellerID = seller == null ? "" : seller.getAttribute("UserID");
            String descipt = getElementTextByTagNameNR(item, "Description");
            // Trim the desciption and append the eplisis if longer than 4000 chars.
            if (descipt.length() > 4000) {
                descipt = descipt.substring(0, 3997);
                descipt += "...";
            }

            String concated = concatString(itemID, name, currently, buy_price, first_bid, numofbids, locationInfo, country, started, ends, sellerID, descipt);
            writeFile(Items, concated);
        }
        catch (IOException error2) {
            error2.printStackTrace();
        }
    }

    /*
    Helper function: Process the passed in strings, and concat them into a long string, separated
    with comma.
     */
    static String concatString(String... list) throws IOException {
        String concated = "";
        for(int i = 0; i < list.length; i++) {
            if(i != list.length - 1) {
                concated += (list[i] + columnSeparator);
            } else {
                concated += list[i];
            }
        }
        return concated;
    }

    /*
    Helper function: Write the formatted string into the Buffered Writer.
     */
    static void writeFile(BufferedWriter filewriter, String concated) throws IOException {
        filewriter.write(concated);
        filewriter.newLine();
    }

    
    /* Process one items-???.xml file.
     */
    static void processFile(File xmlFile) throws IOException{
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
        
        /* Fill in code here (you will probably need to write auxiliary
            methods). */
        Node root = doc.getDocumentElement();
        Element[] items = getElementsByTagNameNR((Element) root, "Item");

        try {
            for(Element item : items) {
                processItem(item);
                processCategory(item);
                processBids(item);
                processBidders(item);
                processSeller(item);
            }
        }
        catch (IOException error1) {
            System.out.println("Error: Failed processing the Transformation");
            error1.printStackTrace();
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
            Items = new BufferedWriter(new FileWriter("items.dat", true));
            Categories = new BufferedWriter(new FileWriter("categories.dat", true));
            Bids = new BufferedWriter(new FileWriter("bids.dat", true));
            Bidders = new BufferedWriter(new FileWriter("bidders.dat", true));
            Sellers = new BufferedWriter(new FileWriter("sellers.dat", true));


            for (String arg : args) {
                File currentFile = new File(arg);
                processFile(currentFile);
            }

            Items.close();
            Categories.close();
            Bids.close();
            Bidders.close();
            Sellers.close();

        }
        catch (IOException error3) {
            System.out.println("ERROR: Failed Writing files");
            error3.printStackTrace();
        }
    }
}
