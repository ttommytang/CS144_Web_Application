CREATE TABLE IF NOT EXISTS Bidders(
	userID VARCHAR(80) NOT NULL,
	rating INT NOT NULL,
	location VARCHAR(100),
	country VARCHAR(40),
	PRIMARY KEY(userID),
	CHECK(rating >= 0)
);

CREATE TABLE IF NOT EXISTS Sellers(
	userID VARCHAR(80) NOT NULL,
	rating INT NOT NULL,
	PRIMARY KEY(userID),
	CHECK(rating >= 0)
);

CREATE TABLE IF NOT EXISTS Items(
	itemID INT NOT NULL,
	name VARCHAR(100) NOT NULL,
	currently DECIMAL(8, 2) NOT NULL,
	buy_price DECIMAL(8, 2),
	first_bid DECIMAL(8, 2) NOT NULL,
	num_of_bids INT NOT NULL,
	location VARCHAR(100) NOT NULL,
	latitude DECIMAL(8, 6),
	longitude DECIMAL(8, 6),
	country VARCHAR(40) NOT NULL,
	started TIMESTAMP NOT NULL,
	ends TIMESTAMP NOT NULL,
	sellerID VARCHAR(80) NOT NULL,
	description VARCHAR(4000),

	PRIMARY KEY(itemID),
	FOREIGN KEY (sellerID) REFERENCES Sellers(userID),
	CHECK(itemID > 0 AND
		ends > started)
	);

CREATE TABLE IF NOT EXISTS Bids(
	itemID INT NOT NULL,
	bidderID VARCHAR(80) NOT NULL,
	time TIMESTAMP NOT NULL,
	amount DECIMAL(8, 2) NOT NULL,
	
	PRIMARY KEY(itemID, bidderID, time),
	FOREIGN KEY (itemID) REFERENCES Items(itemID),
	FOREIGN KEY (bidderID) REFERENCES Bidders(userID),
	CHECK(amount >= 0)
);

CREATE TABLE IF NOT EXISTS Categories(
	itemID INT NOT NULL,
	category VARCHAR(80) NOT NULL,
	PRIMARY KEY(itemID, category),
	FOREIGN KEY (itemID) REFERENCES Items(itemID)
);
