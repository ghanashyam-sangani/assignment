# Connecting to the ProductApp Database with IntelliJ's Built-in Database Client

This guide will show you how to connect to the PostgreSQL database used by the ProductApp using IntelliJ IDEA's built-in database client.

## Prerequisites

- IntelliJ IDEA (Ultimate Edition) with the Database Tools and SQL plugin installed
- PostgreSQL database server running on localhost:5432 with a database named "products_db"
- Database credentials (username: postgres, password: root) as configured in the application.properties file

## Steps to Connect

1. Open IntelliJ IDEA and your ProductApp project.

2. Click on the "Database" tool window tab on the right side of the IDE. If you don't see it, go to View -> Tool Windows -> Database.

3. Click the "+" button in the Database tool window and select "Data Source" -> "PostgreSQL".

4. In the "Data Sources and Drivers" dialog, configure the connection with the following settings:
   - Name: ProductApp Database
   - Host: localhost
   - Port: 5432
   - Database: products_db
   - User: postgres
   - Password: root

5. Test the connection by clicking the "Test Connection" button. If successful, click "OK" to save the connection.

6. The database connection will appear in the Database tool window. You can expand it to see the database objects (tables, views, etc.).

7. You can now use IntelliJ's database client to:
   - Browse tables and their data
   - Execute SQL queries
   - Create, modify, or delete database objects
   - Export data
   - Generate database diagrams

## Using the Database Client with ProductApp

While the application will continue to use Spring's JdbcClient for database operations in the code, you can use IntelliJ's database client for:

1. **Development and Debugging**:
   - Verify that data is being correctly stored in the database
   - Check the structure of tables
   - Run ad-hoc queries to test or debug issues

2. **Database Administration**:
   - Monitor the database
   - Perform backups
   - Manage users and permissions

3. **Data Exploration**:
   - Analyze data with SQL queries
   - Export data for reporting

## Benefits of Using IntelliJ's Database Client

- Integrated development experience within the same IDE
- Rich SQL editor with code completion
- Visual query builder
- Table editor for easy data manipulation
- Database diagrams for visualizing relationships
- History of executed SQL statements
- Export and import functionality

By using IntelliJ's built-in database client alongside Spring's JdbcClient in your code, you get the best of both worlds: programmatic database access in your application and a powerful GUI tool for database management and exploration during development.