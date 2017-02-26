package edu.ucla.cs.cs144;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProxyServlet extends HttpServlet implements Servlet {
       
    public ProxyServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // your codes here


        try {

            // encode the destination url
            String query = request.getParameter("query");
            String des = "http://google.com/complete/search?output=toolbar&q=" + URLEncoder.encode(query, "UTF-8");;


            //set the format of response
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/xml");

            PrintWriter writer = response.getWriter();

            //Use reader to read data from Google
            BufferedReader reader = null;
            StringBuilder sb = new StringBuilder();

            //build connection
            URL url = new URL(des);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(1000);
            connection.connect();

            //getInputStream method can retrieve data from google
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line = null;

            /*
            writer.println("<html>");
            writer.println("<head><title>Suggestions</title></head>");
            writer.println("<body>");
            */
            while ((line = reader.readLine()) != null) {
                /*
                writer.println("<div>");
                writer.println(line);
                writer.println("</div");
                */
                sb.append(line + "\n");

            }

            //write the data from reader into response and return the xml data back to the client

            writer.write(sb.toString());

            /*
            writer.println(sb.toString());
            writer.println("</body>");
            writer.println("</html>");
            */


            connection.disconnect();
            reader.close();


        } catch (Exception e) {
            e.printStackTrace();
        }



    }
}
