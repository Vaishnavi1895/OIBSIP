OIBSIP – Oasis Infobyte Java Development Internship Projects

Welcome to my Java Development Internship Projects repository at Oasis Infobyte (OIBSIP).

This repository contains three Java projects developed as part of my learning and practical work in Java development. These projects helped me strengthen my programming fundamentals, Object-Oriented Programming (OOP), GUI development, input validation, and problem-solving skills.

👩‍💻 Developer Information
Name: Vaishnavi Satish Yadav
Internship Domain: Java Development
Organization: Oasis Infobyte (OIBSIP)
Programming Language: Java
IDE: Eclipse IDE
GUI Framework: Java Swing
🚀 Projects
🎮 Task 1: Number Guessing Game

Project Overview

The Number Guessing Game is a Java Swing-based application in which players attempt to guess a randomly generated number within a limited number of attempts.

Features

Interactive graphical user interface.
Three difficulty levels: Easy, Medium, and Hard.
Random number generation.
Higher and lower hints to guide the player.
Attempt counter and score tracking.
Win and lose messages.
Play Again option.
Input validation.

Technologies Used

Java
Java Swing
Eclipse IDE

How to Run

Install the Java Development Kit (JDK).
Open Eclipse IDE.
Create or open the Java project.
Add NumberGuessingGame.java to the source folder.
Run the class as a Java Application.

Learning Outcomes

Learned to use conditional statements, loops, random number generation, event-driven programming, and input validation while developing an interactive desktop application.

🏦 Task 2: ATM Banking System

Project Overview

The ATM Banking System is a Java-based desktop application that simulates basic ATM operations. It uses Java Swing for the graphical interface and demonstrates Object-Oriented Programming concepts.

Features

User authentication using User ID and PIN.
Access denial after three incorrect login attempts.
Withdraw money after checking the available balance.
Deposit money and update the account balance.
Transfer funds between demo bank accounts.
Display the current account balance.
View transaction history for the current session.
Logout and exit options.
Validation of transaction amounts and available balances.

Technologies Used

Java
Java Swing
Eclipse IDE
ArrayList
Object-Oriented Programming (OOP)

Project Structure

ATMProject/
├── src/
│   └── atm/
│       ├── Main.java
│       ├── ATMGUI.java
│       ├── ATM.java
│       ├── Account.java
│       ├── Bank.java
│       └── Transaction.java
└── README.md


Demo Login Credentials

User ID	PIN	Account ID	Initial Balance
user1	1234	1001	₹10,000
user2	5678	1002	₹5,000

Note: These are educational demo credentials, not real banking accounts.

How to Run

Install the JDK and open Eclipse IDE.
Import or create the ATM project.
Ensure all Java files are in the atm package.
Open Main.java.
Select Run As → Java Application.
Log in using one of the demo accounts.

Learning Outcomes

Practiced encapsulation, classes and objects, methods, conditional statements, collections using ArrayList, transaction processing, and Java Swing GUI development.

Limitations

Account balances and transaction history are stored in memory. Data is not permanently saved to a database. This application is intended for educational purposes only.

🚆 Task 3: Online Train Reservation System

Project Overview

The Online Train Reservation System is a GUI-based Java application designed to simulate basic train ticket reservation and cancellation operations. It uses Java Swing for the user interface, JDBC for database connectivity, and SQLite for data storage.

Features

Login using a username and password.
Access denial for invalid credentials.
Book train tickets by entering passenger and journey details.
Automatically populate the train name using the train number.
Generate a unique PNR for each reservation.
Display booking confirmation with reservation details.
Retrieve booking details using a PNR number.
Cancel bookings after user confirmation.
Validate required fields, journey dates, and numeric train numbers.
Use PreparedStatement for parameterized SQL queries.

Technologies Used

Java
Java Swing
JDBC
SQLite
Eclipse IDE
SQL

How to Run

Install the JDK and open Eclipse IDE.
Import the project into Eclipse.
Add the SQLite JDBC driver to the project build path, or configure the dependency if using Maven.
Ensure the database connection code is configured correctly.
Run the application's actual main class as a Java Application.
Log in using the demo credentials configured in the application.

Demo Login Credentials

Username: admin
Password: admin123

Note: These credentials apply to the demo implementation described here. Change them before sharing a public or deployed application.

Learning Outcomes

Learned database connectivity using JDBC, SQL table creation, CRUD operations, prepared statements, form validation, unique PNR generation, and integration of a graphical interface with a database.

Limitations

This is an educational simulation. It does not connect to a real railway reservation network or verify live seat availability.

🛠️ Technologies Used Across Projects
Programming Language: Java
GUI Development: Java Swing
IDE: Eclipse IDE
Database Connectivity: JDBC
Database: SQLite (Online Reservation System)
Data Structures: ArrayList
Core Concepts: Object-Oriented Programming, conditional statements, loops, methods, event handling, and input validation
Version Control: Git and GitHub
🎯 Internship Learning Objectives

Through these projects, I worked on:

Improving Java programming fundamentals.
Applying Object-Oriented Programming concepts.
Building interactive graphical applications using Java Swing.
Implementing event handling and user input validation.
Understanding database connectivity and SQL operations.
Practicing debugging, testing, and problem-solving.
Organizing project files and documenting source code using GitHub.
📸 Project Demonstrations

Screenshots and demonstration videos can be added to the repository to showcase the working applications.

Suggested screenshots:

Number Guessing Game interface.
ATM Banking System login and transaction screens.
Train Reservation System booking confirmation and cancellation screens.
📌 Repository Structure
OIBSIP/
├── NumberGuessingGame/
├── ATMProject/
├── TrainReservationSystem/
└── README.md


Adjust the folder names above to match the actual structure of your repository.

⚠️ Disclaimer

These projects are intended for educational and internship learning purposes. The ATM application is not a real banking system, and the train reservation application is not connected to an actual railway booking service. Demo credentials must not be used for real accounts.

🙏 Acknowledgement

I am grateful to Oasis Infobyte (OIBSIP) for the opportunity to practice Java development and improve my technical skills through project-based learning.

Thank you for visiting my repository!

Vaishnavi Satish Yadav
Java Development Intern | Aspiring Java Developer
