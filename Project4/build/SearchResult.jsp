<%@ page import="java.io.*" %>
<%@ page import="edu.ucla.cs.cs144.SearchResult"%>
<html>
<head>
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

    <!-- Optional theme -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">
    <title>eBay Search</title>
    <script type="text/javascript" src="SuggestionController.js"></script>
    <script type="text/javascript" src="SuggestionProvider.js"></script>

    <script type="text/javascript">
        window.onload = function() {
            var autoSuggest = new AutoSuggestControl(document.getElementById("q"), new SuggestionProvider());
        }
    </script>
    <style>
        div.suggestions {
            -moz-box-sizing: border-box;
            box-sizing: border-box;
            border: 1px solid black;
            position: absolute;
            background-color: white;
        }
        div.suggestions div {
            cursor: default;
            padding: 0px 3px;
        }
        div.suggestions div.current {
            background-color: #3366cc;
            color: white;
        }
        #prev {
            position: absolute;

            text-align: left;
            left:1em;
        }
        #next {
            position: absolute;
            text-align: right;

            right:1em;
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
    <div class="container">
        <div class="jumbotron">
            <h1 style="text-align: center">eBay Auction Search Interface</h1>

        </div>
        <div id="searchBox">
            <form class="form" action="/eBay/search" method="GET" >
                <h1>Search for : </h1>
                <input class="form-control" type = "text" autocomplete="off" id="q" name="query"><br>
                <input type="hidden" name="numResultsToSkip" value="0">
                <input type="submit" class="btn btn-primary" value="Submit">
            </form>
        </div>
        <div id="result" style="visibility:  <%= request.getParameter("query") == null ? "hidden" : "visible"%>">
            <h2>Search Result for <%= "\"" + ((request.getParameter("query") == null) ? "" : request.getParameter("query")) + "\""%></h2>
            <% SearchResult[] results = (SearchResult[])request.getAttribute("results");%>
            <table class = "table">
                <tr>
                    <th>Item ID</th><th>Item Name</th>
                </tr>
            <% for (int i = 0; i < results.length; i++) { %>
                <tr>
                    <td><a href="/eBay/item?id=<%= results[i].getItemId() %>"><%= results[i].getItemId() %>
                    <td><%= results[i].getName() %>
                </tr>
            <% } %>
            <%
                String query;
                int skipNum;
                try {
                    query = request.getParameter("query");
                    skipNum = Integer.parseInt(request.getParameter("numResultsToSkip"));
                } catch (Exception e) {
                    query = "";
                    skipNum = 0;
                }
            %>
            </table>

            <div id="flip">
                <a id="prev" class="btn btn-default" href="/eBay/search?query=<%=query%>&amp;numResultsToSkip=<%=skipNum >= 20 ? skipNum - 20: skipNum%>">Prev</a>
                <a id="next" class="btn btn-default" href="/eBay/search?query=<%=query%>&amp;numResultsToSkip=<%=results.length == 20 ? skipNum + 20: skipNum%>">Next</a>
            </div>
        </div>

    </div>
    </body>
</html>
