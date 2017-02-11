-- 1.find the number of users in the database
SELECT COUNT(*) FROM (
	SELECT userID FROM Sellers UNION 
	SELECT userID FROM Bidders)T ;

-- 2. Find the number of items in "New York"
SELECT COUNT(*) FROM Items WHERE BINARY location = 'New York';

-- 3. Find the number of auctions belonging to exactly four categories.
SELECT COUNT(*) FROM (SELECT COUNT(*) as count FROM Categories
	GROUP BY itemID HAVING count = 4) as b;

-- 4. Find the ID(s) of current (unsold) auction(s) with the highest bid.
SELECT Bids.itemID FROM Bids INNER JOIN Items ON Bids.itemID = Items.itemID
	WHERE Items.ends > '2001-12-20 00:00:01'
	AND Bids.amount = (
		SELECT MAX(amount) FROM Bids);

-- 5. Find the number of sellers whose rating is higher than 1000.
SELECT COUNT(*) FROM Sellers WHERE rating > 1000;

-- 6. Find the number of users who are both sellers and bidders.
SELECT COUNT(*) FROM Sellers WHERE userID IN (
		SELECT userID FROM Bidders);

-- 7. Find the number of categories that include at least one item with a bid of more than $100.
SELECT COUNT(DISTINCT category) FROM Categories WHERE itemID IN (
SELECT itemID FROM Bids WHERE amount > 100.00);