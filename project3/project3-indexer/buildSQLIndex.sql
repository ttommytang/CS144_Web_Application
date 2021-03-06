DROP TABLE IF EXISTS LocationInfo;
CREATE TABLE LocationInfo 
(
        ItemId VARCHAR(150),
        Location POINT NOT NULL,
        primary key(ItemId)
)ENGINE=MyISAM;

INSERT INTO LocationInfo(ItemId, Location)
SELECT ItemId, PointFromText(CONCAT('POINT(',ItemInfo.Latitude,' ',ItemInfo.Longitude,')'))
FROM ItemInfo
WHERE Latitude != "" AND Longitude != "";

ALTER table LocationInfo ADD SPATIAL INDEX(Location);

SET @poly = 'POLYGON((33.774 -118.63, 
					  33.774 -117.38,
				      34.201 -117.38,
				      34.201 -118.63,
				      33.774 -118.63))';

SELECT count(ItemId)
FROM LocationInfo
WHERE MBRCONTAINS(GEOMFROMTEXT(@poly), Location);

