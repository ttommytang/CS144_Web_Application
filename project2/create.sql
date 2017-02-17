

create table if not exists ItemInfo (
	ItemId varchar(50) not null,
	Name varchar(50) not null,
	Currently decimal(8, 2) not null,
	Buy_Price decimal(8, 2),
	First_Bid decimal(8, 2) not null,
	Number_of_Bids int not null,
	Started timestamp not null,
	Ends timestamp not null,
	Country varchar(30) not null,
	Location varchar(60) not null,
	Latitude varchar(20),
	Longitude varchar(20),
	SellerId varchar(30) not null,
	Description varchar(4000) not null,

	primary key(ItemId)

	-- foreign key(SellerId) references SellInfo(SellerId)
	--	on delete cascade
	--	on update cascade

);

create table if not exists CategoryInfo (
	ItemId varchar(50),
	Category varchar(100),
	primary key(ItemId, Category)
);


create table if not exists BidsInfo (
	ItemId varchar(50) not null,
	UserId varchar(50) not null,
	Time timestamp not null,
	Amount decimal(8, 2) not null,

	primary key(ItemId, UserId, Time)
);

create table if not exists UserInfo (
	UserId varchar(50) not null,
	Rating integer not null,
	Location varchar(50),
	Country varchar(50),

	primary key(UserId)
);

create table if not exists SellerInfo (
	SellerId varchar(50) not null,
	Rating integer not null,

	primary key(SellerId)
);

