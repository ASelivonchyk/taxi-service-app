🚕 Taxi service web app 🚕
--

### Project description:
It`s a simple web application that provides taxi administration service. 
The application is built on the SOLID principles of object-oriented programming, so it can be easily maintained and scaled. 
The program architecture uses MVC pattern, that esealy allows to represent all the information, that storage in database, on a browser page. To protect database data and privacy it uses a web filter, which allows work with application only for authorized users.

### Features:
- Login and logout in service;
- Registration as a Driver;
- Driver authentication;
- Creating - updating - deliting drivers/cars/manufacturers;
- Displaying in a table all drivers/cars/manufacturers that are containing in databse;
- Assing driver to car and removing from car;

### Project structure:
Project based on 3-layer architecture: 
Data access layer (DAO) -> Businnes layer (services) -> Presentation/View layer (controllers).

### Technologies:
- Java 11;
- MySQL;
- Java Database Connectivity API (JDBC);
- Java Server Pages (JSP);
- Javax Servlet;
- JavaServer Pages Standard Tag Librar (JSTL):
- Apache Tomcat;
- Apache Maven as a build tool;
- html, css;
- Bootstrap 5 framework.

### How to start the application.
To start the project:
- clone this repository;
- install and configure the Tomcat servlet container (recomended version - 9.x.xx);
- install and run MySQL server;
- run src/main/resources/init_db.sql (in development environment or MySQL Workbench) to create schema and tables;
- in src/main/webapp/META-INF/context.xml fill your USERNAME and PASSWORD to database;
- deploy application to Tomcat (using development environment or under the Tomcats \webapps directory).
