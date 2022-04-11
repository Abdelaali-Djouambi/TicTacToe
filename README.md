# TicTacToe

TicTacToe Kata using TDD, this application has multiple endpoints to simulate TicTacToe game between two players.

# Prerequisites

In order to run the application, you need to have:
1. JDK 18 (https://www.oracle.com/java/technologies/downloads/)
2. Maven (https://maven.apache.org/download.cgi).
3. Git (https://git-scm.com/downloads)
   You also need to configurate your user and system path variables for both java and maven. You also need to have git installed in order to clone the project .

# How to run

1. This project is saved on github, you can clone it by running the folowing command on any repository (Git must be installed), it will create a TicTacToe folder.

   git clone https://github.com/Abdelaali-Djouambi/TicTacToe.git

2. Go to TicTacToe directory and run the folowing command in order to build and package the application

       mvn clean install

3. Go to /target inside TicTacToe and run the folowing command, and the application should start.

       java java -jar TicTacToe-0.0.1-SNAPSHOT.jar

You can also run the application by executing it's main method, after opening it with your favorite IDE.

# Data base access

This application has an embedded H2 database, so that you don't need to install any database drivers. You can access the embedded H2 database by browsing to http://localhost:8080/h2-console, use the following access parameters:

Driver Class = org.h2.Driver, JDBC URL = jdbc:h2:mem:testdb, User Name = sa, Password = password

# Documentation

Once the applciation is started you can access the documentation of its APIs by browsing to http://localhost:8080/swagger-ui/
this openApi documentation presents the description of the different endpoints of the application.

# Calling the APIs

You can test the endpoints using the Swagger UI in http://localhost:8080/swagger-ui/ as a REST Client, or use Postman or any other REST Client you have.

# Running the unit tests

You can run the tests of the applciation by moving to TicTacToe directory and running

      mvn test

