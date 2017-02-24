<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>

<html>
	<head>
		<style>
		header {
			background-color: #74afe0;
			font-size: 30pt;
			text-align: center;
			vertical-align: center;
			height: 60px;
			position: relative;
			left: 0px;
			right: 0px;
			width: 100%;
			top: 0;
			line-height: 60px;
		}
		footer {
			background-color: #74afe0;
			position: fixed;
			bottom: 0px;
			width: 100%;
			left: 0px;
			height: 1em;
			text-align: right;
		}
		table, th, td {
    		border: 0px;
		}
	    </style>
	</head>
	<body>
		<header>Item Information</header>
		<div><p><%= request.getAttribute("itemInfo"); %></p></div>
		<footer>&copy; Copyright by University of California, Los Angeles (UCLA)</footer>
	</body>
</html>