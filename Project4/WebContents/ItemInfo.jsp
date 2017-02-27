<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>

<html>
<head>
	<style>
		header {
		  background-color: #74afe0;
		  font-size: 26pt;
		  text-align: center;
		  height: 50px;
		  position: fixed;
		  left: 0px;
		  right: 0px;
		  width: 100%;
		  top: 0;
		  line-height: 50px;
		}
		
		footer {
		  background-color: #74afe0;
		  position: fixed;
		  bottom: 0px;
		  width: 100%;
		  left: 0px;
		  height: 1em;
		  text-align: right;
          font-size:6pt;
		}
		
		.container {
		  margin-top: 80px;
		  margin-bottom: 1.5em;
		  left: 0;
		  bottom: 0;
		}
		
		.container p,
		ul,
		h2 {
		  margin: 0;
		}
		
		.left-col {
		  width: calc(20% - 2em);
		  text-align: right;
		  color: #74afe0;
		  overflow: auto;
		  float: left;
		  clear: left;
		}
		
		.right-col {
		  width: calc(80% - 3em);
		  text-align: justify;
		  bottom: 1em;
		  overflow: scroll;
		  border-left: 3px solid #74afe0;
		  padding-right: 0.5em;
		}
		
		.content {
		  font-size: 25px;
		}
		
		#floatbutton {
			padding: 0px;
			border-radius: 10px;
			height: 40px;
			width: 100px;
			color: white;
			left: 2%;
			top: 5px;
			background-color: #c1ddf4;
			position: fixed;
			cursor: pointer;
			font-size: 16pt;
            text-align: center;
		}
		
		#map_canvas {
			height: 400px;
			width: 720px;

		}
	</style>
</head>
<body>
	<header>Item Information</header>
	<div id="floatbutton" onclick="history.back()">
		<p>Back</p>
	</div>
	<div class="container">
		<div class="left-col">
			<div class="content">
				<p>Item ID</p>
			</div>
		</div>
		<div class="right-col">
			<div class="content">
				<p><%= request.getAttribute("ItemID")%></p>
			</div>
		</div>
		<div class="left-col">
			<div class="content">
				<p>Name</p>
			</div>
		</div>
		<div class="right-col">
			<div class="content">
				<p><%= request.getAttribute("Name")%></p>
			</div>
		</div>
		<div class="left-col">
			<div class="content">
				<p>Category</p>
			</div>
		</div>
		<div class="right-col">
			<div class="content">
				<% List<String> categories = (List<String>)request.getAttribute("Category"); 
				for(String str : categories) {%>
				<p><%= str%></p>
				<% } %>
			</div>
		</div>
		<div class="left-col">
			<div class="content">
				<p>Current Price</p>
			</div>
		</div>
		<div class="right-col">
			<div class="content">
				<p><%= request.getAttribute("Currently")%></p>
			</div>
		</div>
		<% if(request.getAttribute("Buy_Price")!=null) {%>
		<div class="left-col">
			<div class="content">
				<p>Buy Price</p>
			</div>
		</div>
		<div class="right-col">
			<div class="content">
				<p><%= request.getAttribute("Buy_Price")%></p>
			</div>
		</div>
		<% } %>
		<div class="left-col">
			<div class="content">
				<p>First Bid</p>
			</div>
		</div>
		<div class="right-col">
			<div class="content">
				<p><%= request.getAttribute("First_Bid")%></p>
			</div>
		</div>
		<div class="left-col">
			<div class="content">
				<p>Number of Bids</p>
			</div>
		</div>
		<div class="right-col">
			<div class="content">
				<p><%= request.getAttribute("Num_Of_Bids")%></p>
			</div>
		</div>
		<% if(!request.getAttribute("Num_Of_Bids").equals("0")) {
		List<String> biddersRating = (List<String>)request.getAttribute("BiddersRating");
		List<String> biddersID = (List<String>)request.getAttribute("BiddersID");
		List<String> biddersLoc = (List<String>)request.getAttribute("BiddersLoc");
		List<String> biddersCountry = (List<String>)request.getAttribute("BiddersCountry");
		List<String> bidsTime = (List<String>)request.getAttribute("BidsTime");
		List<String> bidsAmount = (List<String>)request.getAttribute("BidsAmount"); %>
			<div class="left-col">
				<div class="content">
					<p>Bids Detail</p>
				</div>
			</div>
			<div class="right-col">
				<table>
					<colgroup>
						<col style="background-color:#d0e3f4">
						<col style="background-color:#afd5f7">
						<col style="background-color:#97c5ef">
						<col style="background-color:#afd5f7">
						<col style="background-color:#d0e3f4">
					</colgroup>
					<tr>
						<th>Bidder ID</th>
						<th>Rating</th>
						<th>Location</th>
						<th>Time</th>
						<th>Amount</th>
					</tr>
				<% for (int i = 0; i < biddersRating.size(); i++) { 
					String bidderLoc = biddersLoc.get(i) + ", " + biddersCountry.get(i); %>
					<tr>
						<td><%= biddersID.get(i) %></td>
						<td><%= biddersRating.get(i) %></td>
						<td><%= bidderLoc %></td>
						<td><%= bidsTime.get(i) %></td>
						<td><%= bidsAmount.get(i) %></td>
					</tr>
					<% } %>
				</table>
				
			</div>
			<% } %>
		<div class="left-col">
			<div class="content">
				<p>Location</p>
			</div>
		</div>
		<% String loc = (String)request.getAttribute("Location");
		double lat = 0.0;
		double lng = 0.0;
		if(request.getAttribute("Latitude") != null && !request.getAttribute("Latitude").equals("") && request.getAttribute("Longitude") != null && !request.getAttribute("Longitude").equals("")) {
			loc = loc + "(" + request.getAttribute("Latitude") + ", " + request.getAttribute("Longitude") + ")"; 
			lat = Double.parseDouble((String)request.getAttribute("Latitude"));
			lng = Double.parseDouble((String)request.getAttribute("Longitude"));
		}%>
		<div class="right-col">
			<div class="content">
				<p><%= request.getAttribute("Location") %></p>
			</div>
			<% if(lat != 0.0 && lng != 0.0) { %>
			<div id="map_canvas"></div>
				<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script> 
				<script type="text/javascript"> 
	  				function initialize() { 
		    			var latlng = new google.maps.LatLng(<%= lat%>,<%= lng%>); 
		    			var myOptions = { 
		      				zoom: 14, // default is 8  
		      				center: latlng, 
		      				mapTypeId: google.maps.MapTypeId.ROADMAP 
		    			}; 
		    			var map = new google.maps.Map(document.getElementById("map_canvas"), 
		        			myOptions); 
						var marker = new google.maps.Marker({
							position: latlng,
							map: map,
                            animation: google.maps.Animation.BOUNCE
						});
	  				}
					initialize("map_canvas");
				</script> 
			<% } %>
		</div>
		<div class="left-col">
			<div class="content">
				<p>Country</p>
			</div>
		</div>
		<div class="right-col">
			<div class="content">
				<p><%= request.getAttribute("Country") %></p>
			</div>
		</div>
		<div class="left-col">
			<div class="content">
				<p>Started Time</p>
			</div>
		</div>
		<div class="right-col">
			<div class="content">
				<p><%= request.getAttribute("Started") %></p>
			</div>
		</div>
		<div class="left-col">
			<div class="content">
				<p>Ends Time</p>
			</div>
		</div>
		<div class="right-col">
			<div class="content">
				<p><%= request.getAttribute("Ends") %></p>
			</div>
		</div>
		<div class="left-col">
			<div class="content">
				<p>Seller</p>
			</div>
		</div>
		<% String seller = request.getAttribute("SellerID") + "(Rating:" + request.getAttribute("SellerRating") + ")"; %>
		<div class="right-col">
			<div class="content">
				<p><%= seller %></p>
			</div>
		</div>
		<div class="left-col">
			<div class="content">
				<p>Description</p>
			</div>
		</div>
		<div class="right-col">
			<div class="content">
				<p><%= request.getAttribute("Description") %></p>
			</div>
		</div>
	</div>
    <footer>&copy; Copyright and Made by Jiapeng & Tommy</footer>
</body>
</html>