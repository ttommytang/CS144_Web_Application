
SELECT COUNT(*) FROM (
	SELECT SellerId FROM SellerInfo UNION 
	SELECT UserId FROM UserInfo)T ;


SELECT COUNT(*) FROM ItemInfo WHERE BINARY Location = 'New York';


SELECT COUNT(*) FROM (SELECT COUNT(*) as count FROM CategoryInfo
	GROUP BY ItemId HAVING count = 4) as b;


SELECT ItemId FROM (SELECT ItemId, MAX(Currently) FROM ItemInfo WHERE Ends > '2001-12-20 12:00:01') HighestBid;


SELECT COUNT(*) FROM SellerInfo WHERE Rating > 1000;


select count(*) from SellerInfo where SellerId in (
		select UserId from UserInfo);


SELECT COUNT(DISTINCT Category) FROM CategoryInfo WHERE ItemId IN (
	SELECT ItemId FROM BidsInfo WHERE Amount > 100.00);