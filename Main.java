import model.Category;
import model.Expense;
import service.CategoryService;
import service.ExpenseService;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Main class providing the console-based user interface.
 */
public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final CategoryService categoryService = new CategoryService();
    private static final ExpenseService expenseService = new ExpenseService();
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println("   Welcome to Personal Expense Tracker");
        System.out.println("=========================================");

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Select an option (1-9): ");

            switch (choice) {
                case 1:
                    addExpense();
                    break;
                case 2:
                    viewAllExpenses();
                    break;
                case 3:
                    manageCategories();
                    break;
                case 4:
                    filterExpenses();
                    break;
                case 5:
                    generateReports();
                    break;
                case 6:
                    editExpense();
                    break;
                case 7:
                    deleteExpense();
                    break;
                case 8:
                    System.out.println("Exiting the application. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println("1. Add an Expense");
        System.out.println("2. View All Expenses");
        System.out.println("3. Manage Categories");
        System.out.println("4. Filter Expenses");
        System.out.println("5. Generate Reports (Totals)");
        System.out.println("6. Edit an Expense");
        System.out.println("7. Delete an Expense");
        System.out.println("8. Exit");
    }

    private static void addExpense() {
        System.out.println("\n--- Add Expense ---");
        LocalDate date = readDate("Enter Date (yyyy-MM-dd) [Leave blank for today]: ", true);
        if (date == null) date = LocalDate.now();

        double amount = readDouble("Enter Amount: ");
        if (amount <= 0) {
            System.out.println("Amount must be positive.");
            return;
        }

        Category category = selectCategory();
        if (category == null) return;

        System.out.print("Enter Description: ");
        String description = scanner.nextLine();

        expenseService.addExpense(date, amount, category, description);
        System.out.println("Expense added successfully!");
    }

    private static void viewAllExpenses() {
        System.out.println("\n--- All Expenses ---");
        List<Expense> expenses = expenseService.getAllExpenses();
        displayExpenses(expenses);
    }

    private static void manageCategories() {
        System.out.println("\n--- Manage Categories ---");
        System.out.println("1. View all categories");
        System.out.println("2. Add a new category");
        int choice = readInt("Select an option (1-2): ");

        if (choice == 1) {
            System.out.println("Categories:");
            categoryService.getAllCategories().forEach(c -> System.out.println("- " + c.getName()));
        } else if (choice == 2) {
            System.out.print("Enter new category name: ");
            String name = scanner.nextLine();
            if (name.isEmpty()) {
                System.out.println("Category name cannot be empty.");
            } else if (categoryService.addCategory(name)) {
                System.out.println("Category added successfully!");
            } else {
                System.out.println("Category already exists.");
            }
        } else {
            System.out.println("Invalid option.");
        }
    }

    private static void filterExpenses() {
        System.out.println("\n--- Filter Expenses ---");
        System.out.println("1. By Date Range");
        System.out.println("2. By Category");
        System.out.println("3. By Amount Range");
        int choice = readInt("Select an option (1-3): ");

        List<Expense> results = null;

        if (choice == 1) {
            LocalDate start = readDate("Enter start date (yyyy-MM-dd): ", false);
            LocalDate end = readDate("Enter end date (yyyy-MM-dd): ", false);
            if (start != null && end != null) {
                if (start.isAfter(end)) {
                    System.out.println("Start date cannot be after end date.");
                    return;
                }
                results = expenseService.filterByDateRange(start, end);
            }
        } else if (choice == 2) {
            Category cat = selectCategory();
            if (cat != null) {
                results = expenseService.filterByCategory(cat);
            }
        } else if (choice == 3) {
            double min = readDouble("Enter minimum amount: ");
            double max = readDouble("Enter maximum amount: ");
            if (min > max) {
                System.out.println("Minimum amount cannot be greater than maximum amount.");
                return;
            }
            results = expenseService.filterByAmountRange(min, max);
        } else {
            System.out.println("Invalid option.");
            return;
        }

        if (results != null) {
            System.out.println("\nFilter Results:");
            displayExpenses(results);
        }
    }

    private static void generateReports() {
        System.out.println("\n--- Reports ---");
        System.out.println("1. Total by Category");
        System.out.println("2. Total by Month");
        int choice = readInt("Select an option (1-2): ");

        if (choice == 1) {
            Map<Category, Double> totals = expenseService.getCategoryWiseTotals();
            if (totals.isEmpty()) {
                System.out.println("No expenses found.");
            } else {
                System.out.println("Category-wise Totals:");
                totals.forEach((cat, total) -> System.out.printf("%-15s: $%.2f\n", cat.getName(), total));
            }
        } else if (choice == 2) {
            Map<YearMonth, Double> totals = expenseService.getMonthlyTotals();
            if (totals.isEmpty()) {
                System.out.println("No expenses found.");
            } else {
                System.out.println("Monthly Totals:");
                totals.forEach((month, total) -> System.out.printf("%-10s: $%.2f\n", month.toString(), total));
            }
        } else {
            System.out.println("Invalid option.");
        }
    }

    private static void editExpense() {
        System.out.println("\n--- Edit Expense ---");
        viewAllExpenses();
        int id = readInt("Enter the ID of the expense to edit (0 to cancel): ");
        if (id == 0) return;

        System.out.println("Leave blank (or 0 for amount) to keep current value.");
        
        LocalDate date = readDate("Enter new Date (yyyy-MM-dd): ", true);
        double amount = readDouble("Enter new Amount: ", true);
        
        System.out.println("Do you want to change category? (y/n): ");
        String changeCat = scanner.nextLine();
        Category category = null;
        if (changeCat.equalsIgnoreCase("y")) {
            category = selectCategory();
        }

        System.out.print("Enter new Description: ");
        String description = scanner.nextLine();

        boolean updated = expenseService.editExpense(id, date, amount, category, description);
        if (updated) {
            System.out.println("Expense updated successfully!");
        } else {
            System.out.println("Expense ID not found.");
        }
    }

    private static void deleteExpense() {
        System.out.println("\n--- Delete Expense ---");
        int id = readInt("Enter the ID of the expense to delete: ");
        boolean deleted = expenseService.deleteExpense(id);
        if (deleted) {
            System.out.println("Expense deleted successfully!");
        } else {
            System.out.println("Expense ID not found.");
        }
    }

    // --- Helper Methods ---

    private static void displayExpenses(List<Expense> expenses) {
        if (expenses.isEmpty()) {
            System.out.println("No expenses found.");
        } else {
            expenses.forEach(System.out::println);
        }
    }

    private static Category selectCategory() {
        List<Category> categories = categoryService.getAllCategories();
        System.out.println("Available Categories:");
        for (int i = 0; i < categories.size(); i++) {
            System.out.println((i + 1) + ". " + categories.get(i).getName());
        }
        int choice = readInt("Select a category (1-" + categories.size() + "): ");
        if (choice >= 1 && choice <= categories.size()) {
            return categories.get(choice - 1);
        } else {
            System.out.println("Invalid category selection.");
            return null;
        }
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = Integer.parseInt(scanner.nextLine().trim());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private static double readDouble(String prompt) {
        return readDouble(prompt, false);
    }

    private static double readDouble(String prompt, boolean allowEmpty) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (allowEmpty && input.isEmpty()) {
                return 0.0;
            }
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid amount.");
            }
        }
    }

    private static LocalDate readDate(String prompt, boolean allowEmpty) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (allowEmpty && input.isEmpty()) {
                return null;
            }
            try {
                return LocalDate.parse(input, DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use yyyy-MM-dd.");
            }
        }
    }
}
