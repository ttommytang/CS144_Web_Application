1. Parsed the XML files using JDK's XML parser, then implemented two java classes to process the DOM tree and transformed it and wrote into the dat files.
2. Designed a relational database schema to store the data, and wrote MySQL script to create and load the data tables with the consideration of the database integrity.
3. Wrote a bash script to automotive the procedure and tested the database with several queries.

Before this project, we designed a relational schema for a snapshot of eBay auction data provided in a set of XML files. In this project we provide "search" functions to this data. 
Our search functions will support two types of queries: keyword-based search over text fields (such as item descriptions) and search for items located within a geographic region. 
For the first type of queries, we will be using the Apache Lucene text search engine. For the second type, we will use a spatial index in MySQL.