<%@ page import="java.io.*" %>
<%@ page import="edu.ucla.cs.cs144.SearchResult"%>
<html>
<head>
    <style>
        #prev {
            position: fixed;
            left:1em;
        }
        #next {
            position: fixed;
            right:1em;
        }
    </style>
    <title>HTTP Header Request Example</title>

    <body>

        <h2>SearchResult</h2>
        <% SearchResult[] results = (SearchResult[])request.getAttribute("results");%>
        <table width="100%" border="1" align="center">
            <tr bgcolor="#949494">
                <th>Param ID</th><th>Param Name</th>
            </tr>
        <% for (int i = 0; i < results.length; i++) { %>
            <tr>
                <td><%= results[i].getItemId() %>
                <td><%= results[i].getName() %>
            </tr>
        <% } %>
        <%
            String query = request.getParameter("query");
            int numResultsToSkip = Integer.parseInt(request.getParameter("numResultsToSkip"));
        %>
        </table>
        <div>
            <a id="prev" href="/eBay/search?query=<%=query%>&amp;numResultsToSkip=<%=numResultsToSkip >= 20 ? numResultsToSkip - 20: numResultsToSkip%>">Prev</a>
            <a id="next" href="/eBay/search?query=<%=query%>&amp;numResultsToSkip=<%=results.length == 20 ? numResultsToSkip + 20: numResultsToSkip%>">Next</a>
        </div>
    </body>
</html>
