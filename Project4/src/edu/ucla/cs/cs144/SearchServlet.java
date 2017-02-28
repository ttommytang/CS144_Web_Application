package edu.ucla.cs.cs144;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SearchServlet extends HttpServlet implements Servlet {
       
    public SearchServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // your codes here
        AuctionSearch as = new AuctionSearch();
        int numResultsToReturn = 20;
        String query;
        int numResultsToSkip;
        try {
            query = request.getParameter("query");
            numResultsToSkip = Integer.parseInt(request.getParameter("numResultsToSkip"));
        } catch (Exception e) {
            numResultsToSkip = 0;
            query = "";
        }


        SearchResult[] results = as.basicSearch(query, numResultsToSkip, numResultsToReturn);
        /*
        PrintWriter out = response.getWriter();

        out.println("<html>");
        out.println("<head><title>Servlet Example</title></head>");
        out.println("<body>" + numResultsToSkip + "</body>");
        out.println("</html>");
        out.close();
        */
       // System.out.println(results.length);
        request.setAttribute("results", results);

        request.getRequestDispatcher("/SearchResult.jsp").forward(request, response);


    }
}
