package edu.ucla.cs.cs144;

/**
 * Created by tommy on 2/22/17.
 */

import java.io.*;
import java.util.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.*;

/**
 * Item class: defined to handle the input xml-formatted string and parse into the
 * instance variables. Getters provided for external use.
 */
public class ItemInfo {
    private String itemId;
    private String name;
    private List<String> category;
    private String currently;
    private String buyPrice;
    private String firstBid;
    private String numOfBids;
    private List<String> biddersRating;
    private List<String> biddersId;
    private List<String> biddersLoc;
    private List<String> biddersCountry;
    private List<String> bidsTime;
    private List<String> bidsAmount;
    private String location;
    private String latitude;
    private String longitude;
    private String country;
    private String started;
    private String ends;
    private String sellerRating;
    private String sellerId;
    private String description;

    /**************************************
    Setters and Getters for the Item class.
    ***************************************/
    public String getItemId() {
        return this.itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }

    public String getCurrently() {
        return currently;
    }

    public void setCurrently(String currently) {
        this.currently = currently;
    }

    public String getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(String buyPrice) {
        this.buyPrice = buyPrice;
    }

    public String getFirstBid() {
        return firstBid;
    }

    public void setFirstBid(String firstBid) {
        this.firstBid = firstBid;
    }

    public String getNumOfBids() {
        return numOfBids;
    }

    public void setNumOfBids(String numOfBids) {
        this.numOfBids = numOfBids;
    }

    public List<String> getBiddersRating() {
        return biddersRating;
    }

    public void setBiddersRating(List<String> biddersRating) {
        this.biddersRating = biddersRating;
    }

    public List<String> getBiddersId() {
        return biddersId;
    }

    public void setBiddersId(List<String> biddersId) {
        this.biddersId = biddersId;
    }

    public List<String> getBiddersLoc() {
        return biddersLoc;
    }

    public void setBiddersLoc(List<String> biddersLoc) {
        this.biddersLoc = biddersLoc;
    }

    public List<String> getBiddersCountry() {
        return biddersCountry;
    }

    public void setBiddersCountry(List<String> biddersCountry) {
        this.biddersCountry = biddersCountry;
    }

    public List<String> getBidsTime() {
        return bidsTime;
    }

    public void setBidsTime(List<String> bidsTime) {
        this.bidsTime = bidsTime;
    }

    public List<String> getBidsAmount() {
        return bidsAmount;
    }

    public void setBidsAmount(List<String> bidsAmount) {
        this.bidsAmount = bidsAmount;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStarted() {
        return started;
    }

    public void setStarted(String started) {
        this.started = started;
    }

    public String getEnds() {
        return ends;
    }

    public void setEnds(String ends) {
        this.ends = ends;
    }

    public String getSellerRating() {
        return sellerRating;
    }

    public void setSellerRating(String sellerRating) {
        this.sellerRating = sellerRating;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**************************************************************************************************************
     In this part, the input xml-formatted string is firstly converted into the DOM-tree, then use the same methods
     in project-2 to parse and store the fields into the instance variables of the item object.
     **************************************************************************************************************/

    /**
     * Convert the xml-formatted string into the DOM tree.
     * @param xmlStr input string
     * @return DOM
     */
    private static Document strToDom(String xmlStr) throws SAXException, ParserConfigurationException, IOException{
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(new InputSource(new StringReader(xmlStr)));
    }

    /**
     * This function parse the xml-string, parse the fields and set the instance variables.
     * @param xmlstr input xml string
     * @return return true if successfully parsed the string, false if the string is null.
     */
    public boolean xmlParser(String xmlstr) throws SAXException, ParserConfigurationException, IOException{
        if(xmlstr == null) {
            System.out.println("Failed to get item information!");
            return false;
        }
        Document xmlDoc;
        try {
            xmlDoc = strToDom(xmlstr);
            Element root = xmlDoc.getDocumentElement();

            setItemId(root.getAttribute("ItemID"));
            setName(DomParser.getElementTextByTagNameNR(root, "Name"));
            setCurrently(DomParser.getElementTextByTagNameNR(root, "Currently"));
            setFirstBid(DomParser.getElementTextByTagNameNR(root, "First_Bid"));
            setNumOfBids(DomParser.getElementTextByTagNameNR(root, "Number_of_Bids"));
            setLocation(DomParser.getElementTextByTagNameNR(root, "Location"));
            setCountry(DomParser.getElementTextByTagNameNR(root, "Country"));
            setStarted(DomParser.getElementTextByTagNameNR(root, "Started"));
            setEnds(DomParser.getElementTextByTagNameNR(root, "Ends"));
            setDescription(DomParser.getElementTextByTagNameNR(root, "Description"));

            Element isBought = DomParser.getElementByTagNameNR(root, "Buy_Price");
            if (isBought != null) {
                setBuyPrice(DomParser.getElementText(isBought));
            }

            Element loc = DomParser.getElementByTagNameNR(root, "Location");
            if(loc.getAttribute("Latitude") != null && loc.getAttribute("Longitude") != null) {
                setLatitude(loc.getAttribute("Latitude"));
                setLongitude(loc.getAttribute("Longitude"));
            }

            setCategory(getCategoryInfo(root));

            Element seller = DomParser.getElementByTagNameNR(root, "Seller");
            setSellerId(seller.getAttribute("UserID"));
            setSellerRating(seller.getAttribute("Rating"));

            List<String> biddersRating = new ArrayList<>();
            List<String> biddersId = new ArrayList<>();
            List<String> biddersLoc = new ArrayList<>();
            List<String> biddersCountry = new ArrayList<>();
            List<String> bidsTime = new ArrayList<>();
            List<String> bidsAmount = new ArrayList<>();

            processBids(root, biddersRating, biddersId, biddersLoc, biddersCountry, bidsTime, bidsAmount);
            setBiddersId(biddersId);
            setBiddersRating(biddersRating);
            setBiddersLoc(biddersLoc);
            setBiddersCountry(biddersCountry);
            setBidsTime(bidsTime);
            setBidsAmount(bidsAmount);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;

    }

    /**
     * Helper function: Parse the categories information and set the responsible instance variable.
     * @param item DOM tree root.
     * @return List of categories.
     */
    private List<String> getCategoryInfo(Element item) {
        Element[] categories = DomParser.getElementsByTagNameNR(item, "Category");
        List<String> categoriesList = new ArrayList<>();
        for (Element category : categories) {
            categoriesList.add(DomParser.getElementText(category));
        }
        return categoriesList;
    }

    /**
     * Helper function: Parse the bids information, and store the 6 different fields into the separated
     * List.
     * @param item DOM tree root.
     * @param biddersRating List of Rating of bidders(same order as the bids).
     * @param biddersId List of Id of bidders(same order as the bids)
     * @param biddersLoc List of Location of bidders(same order as the bids)
     * @param biddersCountry List of Country of bidders(same order as the bids)
     * @param bidsTime List of Time of bids(same order as the bids)
     * @param bidsAmount List of Amount of bids(same order as the bids)
     */
    private void processBids(Element item, List<String> biddersRating, List<String> biddersId, List<String> biddersLoc,
                             List<String> biddersCountry, List<String> bidsTime, List<String> bidsAmount) {
        try {
            Element bids = DomParser.getElementByTagNameNR(item, "Bids");
            Element[] bid = DomParser.getElementsByTagNameNR(bids, "Bid");
            for (Element cur : bid) {
                Element bidder = DomParser.getElementByTagNameNR(cur, "Bidder");
                biddersId.add(bidder.getAttribute("UserID"));
                biddersRating.add(bidder.getAttribute("Rating"));
                biddersLoc.add(DomParser.getElementTextByTagNameNR(bidder, "Location"));
                biddersCountry.add(DomParser.getElementTextByTagNameNR(bidder, "Country"));
                bidsTime.add(DomParser.getElementTextByTagNameNR(cur, "Time"));
                bidsAmount.add(DomParser.getElementTextByTagNameNR(cur, "Amount"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
