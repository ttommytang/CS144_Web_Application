package edu.ucla.cs.cs144;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ItemServlet extends HttpServlet implements Servlet {
       
    public ItemServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // your codes here
        AuctionSearch as = new AuctionSearch();
        String itemId = request.getParameter("id");
        String result = as.getXMLDataForItemId(itemId);
        //String[] attributeArray = result.split("\n");
        /*
        PrintWriter out = response.getWriter();

        out.println("<html>");
        out.println("<head><title>Servlet Example</title></head>");
        out.println("<body>" + result.length() + "</body>");
        out.println("</html>");
        out.close();
        */

        request.setAttribute("attributes", result);
        request.getRequestDispatcher("/ItemInfo.jsp").forward(request, response);
    }
}
