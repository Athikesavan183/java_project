[Uploading README.md…]()
# Customer App

A simple Java console application that collects customer details from the
user and stores them in PostgreSQL by calling the `proc_insertcustomer`
stored procedure.

## Project Structure

```
CustomerApp/
├── pom.xml
├── setup.sql                      # Table + stored procedure DDL
├── src/main/resources/
│   └── db.properties               # DB connection settings
└── src/main/java/com/example/customerapp/
    ├── Main.java                   # Console entry point (collects input)
    ├── Customer.java                # Model class
    ├── CustomerDAO.java             # Calls proc_insertcustomer via JDBC
    └── DBConnection.java            # Loads db.properties, opens connections
```

## Prerequisites

- Java 17+
- Maven 3.8+
- PostgreSQL database

## Setup

1. **Create the table and procedure.** Run `setup.sql` against your
   PostgreSQL database (e.g. via `psql` or a GUI client):

   ```
   psql -U your_db_username -d your_database_name -f setup.sql
   ```

2. **Configure the connection.** Edit
   `src/main/resources/db.properties`:

   ```properties
   db.url=jdbc:postgresql://localhost:5432/your_database_name
   db.username=your_db_username
   db.password=your_db_password
   ```

## Build

```
mvn clean package
```

This produces a runnable, all-dependencies-included jar at:

```
target/customer-app-jar-with-dependencies.jar
```

## Run

```
java -jar target/customer-app-jar-with-dependencies.jar
```

You'll be prompted for:

- Full Name
- Phone
- Email (validated for basic format)
- Address
- City
- Postcode

The current date is automatically used for `datecreated`. On submit, the
app calls `proc_insertcustomer` through a JDBC `CallableStatement`, which
inserts the row into `tb_customer`.

## Notes / Possible extensions

- Input is currently collected via the console (`Scanner`). This same
  `CustomerDAO`/`Customer` code can be reused behind a Swing GUI, a
  JavaFX form, or a Spring Boot REST endpoint if you'd prefer a
  graphical or web-based interface — just ask.
- Currently there's no duplicate-email check; add one in
  `CustomerDAO`/the procedure if needed.
- Consider using a connection pool (e.g. HikariCP) instead of opening a
  new connection per call if this grows beyond a simple demo.
