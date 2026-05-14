# Personal Expense Tracker

## Description
The Personal Expense Tracker is a clean, robust, and intuitive console-based Java application designed to help users effectively record, manage, and analyze their daily expenses. It features a fully-functional menu-driven interface, allowing users to effortlessly add, edit, delete, and view their expenses. Additionally, the application supports detailed filtering and generates comprehensive category-wise and monthly reports, providing users with valuable insights into their spending habits. It ensures data persistence by securely saving expenses and category details into serialized `.dat` files.

*Note: The project comes pre-populated with 100 generated sample expenses across various realistic categories and dates. This dummy data allows you to test out the application's filtering, viewing, and reporting capabilities immediately out of the box!*

## Features
- **Expense Recording**: Add expenses with details like date, amount, category, and description.
- **Category Management**: View existing categories and add custom spending categories dynamically.
- **Data Persistence**: Expenses and categories are saved locally using Java Serialization, ensuring no data loss upon exit.
- **Advanced Filtering**: Filter expenses by a specific date range, category, or amount range.
- **Detailed Reports**: View total expenditure summaries grouped by category or by month.
- **Modify/Delete**: Update past expenses or remove incorrect entries using their unique IDs.
- **Input Validation**: Prevents crashes gracefully with handled exceptions for invalid dates, numbers, or formats.

## Technologies Used
- **Language**: Java 11+ (Core Java)
- **Concepts**: Object-Oriented Programming (OOP), Collections Framework, File I/O (Serialization), Streams API, Exception Handling

## Project Structure
```
ExpenseTracker/
├── Main.java               # Main application loop and console UI
├── model/
│   ├── Category.java       # Category entity class
│   └── Expense.java        # Expense entity class
├── service/
│   ├── CategoryService.java # Business logic for category management
│   └── ExpenseService.java  # Business logic for expense management
└── util/
    └── FileUtil.java       # Reusable utility for Serialization/Deserialization
```

## How to Compile and Run

Ensure you have the Java Development Kit (JDK) installed on your system.

**Linux / macOS / Windows (via terminal or command prompt):**

1. **Navigate to the project root directory**:
   ```bash
   cd /path/to/ExpenseTracker
   ```

2. **Compile the Java files**:
   ```bash
   javac model/*.java util/*.java service/*.java Main.java
   ```

3. **Run the application**:
   ```bash
   java Main
   ```

## Sample Usage

Upon launching, you will be greeted with the Main Menu:
```
=========================================
   Welcome to Personal Expense Tracker
=========================================

--- Main Menu ---
1. Add an Expense
2. View All Expenses
3. Manage Categories
4. Filter Expenses
5. Generate Reports (Totals)
6. Edit an Expense
7. Delete an Expense
8. Exit
Select an option (1-9): 1

--- Add Expense ---
Enter Date (yyyy-MM-dd) [Leave blank for today]: 2023-10-15
Enter Amount: 45.50
Available Categories:
1. Food
2. Transport
...
Select a category (1-6): 1
Enter Description: Lunch at cafe
Expense added successfully!
```

## Future Enhancements
- Implement a graphical user interface (GUI) using JavaFX or Swing.
- Migrate from file-based storage to a relational database like MySQL or SQLite.
- Introduce user authentication to support multiple independent accounts.
- Export functionality for exporting data to CSV or Excel formats.

## Author
Developed as a foundational core Java project demonstrating proficiency in architecture design, data management, and problem-solving.
