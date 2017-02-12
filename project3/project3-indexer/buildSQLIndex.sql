create  table if not exists LocationInfo (
	ItemId VARCHAR(50) NOT NULL,
	Location GEOMETRY NOT NULL,
	SPATIAL INDEX(Location)
) ENGINE=MyISAM;

insert into LocationInfo(ItemId, Location)
	select ItemId, Point(Latitude, Longtitude)
	from ItemInfo
	where Latitude is not null and Longtitude is not null;
