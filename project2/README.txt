1. Parsed the XML files using JDK's XML parser, then implemented two java classes to process the DOM tree and transformed it and wrote into the dat files.
2. Designed a relational database schema to store the data, and wrote MySQL script to create and load the data tables with the consideration of the database integrity.
3. Wrote a bash script to automotive the procedure and tested the database with several queries.


Relational Scheme:
Items(*ItemID*, name, currently, buy-price, first-bid, # of bids, Location, Country, Started, Ends, SellerID, Description);
Bids(*itemID*, *bidderID*, *time*, amount);
Bidders(*userID*, rating, Location, Country)
Sellers(*userID*, rating);
Categories(*itemID*, *category*);
# '*attr*' means attribute attr as primary key(s)

Functional dependencies: 
itemID -> name, currently, buy-price, first-bid, # of bids, Latitude, Longitude, Country, Started, Ends, UserID(seller), description
itemID, userID(buyer), time -> amount
And we also have Multivalued dependency: itemID ->-> Category

The relations above are  all in BCNF and 4NF form. 

At first I considered to combine the Sellers and Bidders relations into just one single relation called Users. And set "" if User has only information as seller or bidder. 

Why not? Cause we have to do the de-duplication in the algorithm itself, we need to keep a HashMap so that the information won’t be added twice or even multiple times. Once we got a seller information, there will be only userID and rating, but when we got this userID again but as a bidder, then we need to update his/her information as a bidder, which means the new rating, location, and country information will need to be updated. And in the same manner, if we get a new seller information, we need to check whether there is already his / her information as a bidder, if the answer is yes, then we just need to update his / her rating as bidder. 

Obviously, in this process, we can just use the userID as the key of the HashMap because it should be unique. But as for the value in the HashMap, we need to implement a special structure that contains the rating as bidder/seller and also the location and country information. And if any information is absent, we can just set it to "" as when we load the information into the final table. 

And what we will be doing during the procession of the user information is not simply extract the information and write it into the buffer, we just maintain our hashMap instead. And after all the items have been traversed, then we traverse the whole HashMap and do the write then. The time complexity should still be linear, but there will be extra space needed for the HashMap. And for a big data set, this space requirement would be considerably large. So I still choose to keep the seller and bidder information in separate relations, just to reduce the space complexity for the whole algorithm.