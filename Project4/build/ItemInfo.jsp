<%@ page import="java.io.*" %>
<html>
    <head>Item Imfo
        <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
        <style type="text/css">
            .container {
                top: 80px;
                width:100%;
                height:calc(100% - 80px);
            }
            #textInfo {
                position: fixed;
                width: calc(50% - 1.5em);
                left: 1em;
            }
            #mapInfo {
                position: fixed;
                float: right;
                height:calc(100% - 80px);
                width: calc(50% - 1.5em);


            }

        </style>
        <script type="text/javascript"
                src="http://maps.google.com/maps/api/js?sensor=false">
        </script>
        <script type="text/javascript">
            function initialize() {
                var latlng = new google.maps.LatLng(34.063509,-118.44541);
                var myOptions = {
                    zoom: 14, // default is 8
                    center: latlng,
                    mapTypeId: google.maps.MapTypeId.ROADMAP
                };
                var map = new google.maps.Map(document.getElementById("mapInfo"),
                    myOptions);
            }

        </script>
    </head>

    <body onload="initialize()">
        <div class="container">

            <div id="textInfo">
            <% String attributes = (String)request.getAttribute("attributes"); %>


            <%= attributes %>
            </div>
            <div id="mapInfo"></div>
        </div>
    </body>
</html>