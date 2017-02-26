<%@ page import="java.io.*" %>
<%@ page import="edu.ucla.cs.cs144.SearchResult"%>
<html>
<head>
    <style>
        #prev {
            position: absolute;
            width: 50%;
            text-align: left;
            left:1em;
        }
        #next {
            position: absolute;
            text-align: right;
            width: 50%;
            right:1em;
        }
        #searchBox {

            height:40px;

        }
        #result {
            top:40px;
            width: 100%;
            overflow: scroll;
        }
        #flip {
            bottom: 0px;
            height:40px;
            width: 100%;
        }
    </style>
    <title>Search Result</title>

    <body>
    <div id="searchBox">
        <form action="/eBay/search" method="GET" >
            Search for : <input type = "text" name="query"><br>
            <input type="hidden" name="numResultsToSkip" value="0">
            <input type="submit" value="Submit">
        </form>
    </div>
    <div id="result">
        <h2>Search Result for <%= "\"" + request.getParameter("query") + "\""%></h2>
        <% SearchResult[] results = (SearchResult[])request.getAttribute("results");%>
        <table width="100%" border="1" align="center">
            <tr bgcolor="#949494">
                <th>Param ID</th><th>Param Name</th>
            </tr>
        <% for (int i = 0; i < results.length; i++) { %>
            <tr>
                <td><a href="/eBay/item?id=<%= results[i].getItemId() %>"><%= results[i].getItemId() %>
                <td><%= results[i].getName() %>
            </tr>
        <% } %>
        <%
            String query = request.getParameter("query");
            int numResultsToSkip = Integer.parseInt(request.getParameter("numResultsToSkip"));
        %>
        </table>
    </div>
        <div id="flip">
            <a id="prev" href="/eBay/search?query=<%=query%>&amp;numResultsToSkip=<%=numResultsToSkip >= 20 ? numResultsToSkip - 20: numResultsToSkip%>">Prev</a>
            <a id="next" href="/eBay/search?query=<%=query%>&amp;numResultsToSkip=<%=results.length == 20 ? numResultsToSkip + 20: numResultsToSkip%>">Next</a>
        </div>


    </body>
</html>
