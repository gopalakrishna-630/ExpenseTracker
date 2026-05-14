package service;

import model.Category;
import model.Expense;
import util.FileUtil;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service class for managing expenses.
 */
public class ExpenseService {
    private static final String FILE_NAME = "expenses.dat";
    private List<Expense> expenses;
    private int nextId = 1;

    public ExpenseService() {
        this.expenses = FileUtil.loadData(FILE_NAME);
        if (!expenses.isEmpty()) {
            nextId = expenses.stream().mapToInt(Expense::getId).max().orElse(0) + 1;
        }
    }

    /**
     * Adds a new expense.
     */
    public void addExpense(LocalDate date, double amount, Category category, String description) {
        Expense expense = new Expense(nextId++, date, amount, category, description);
        expenses.add(expense);
        save();
    }

    /**
     * Retrieves all expenses sorted by date descending.
     */
    public List<Expense> getAllExpenses() {
        return expenses.stream()
                .sorted(Comparator.comparing(Expense::getDate).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Edits an existing expense.
     */
    public boolean editExpense(int id, LocalDate date, double amount, Category category, String description) {
        for (Expense expense : expenses) {
            if (expense.getId() == id) {
                if (date != null) expense.setDate(date);
                if (amount > 0) expense.setAmount(amount);
                if (category != null) expense.setCategory(category);
                if (description != null && !description.isEmpty()) expense.setDescription(description);
                save();
                return true;
            }
        }
        return false;
    }

    /**
     * Deletes an expense by ID.
     */
    public boolean deleteExpense(int id) {
        boolean removed = expenses.removeIf(e -> e.getId() == id);
        if (removed) {
            save();
        }
        return removed;
    }

    /**
     * Filters expenses by a date range.
     */
    public List<Expense> filterByDateRange(LocalDate start, LocalDate end) {
        return expenses.stream()
                .filter(e -> !e.getDate().isBefore(start) && !e.getDate().isAfter(end))
                .sorted(Comparator.comparing(Expense::getDate).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Filters expenses by category.
     */
    public List<Expense> filterByCategory(Category category) {
        return expenses.stream()
                .filter(e -> e.getCategory().equals(category))
                .sorted(Comparator.comparing(Expense::getDate).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Filters expenses by an amount range.
     */
    public List<Expense> filterByAmountRange(double min, double max) {
        return expenses.stream()
                .filter(e -> e.getAmount() >= min && e.getAmount() <= max)
                .sorted(Comparator.comparing(Expense::getAmount).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Gets total expenses grouped by category.
     */
    public Map<Category, Double> getCategoryWiseTotals() {
        return expenses.stream()
                .collect(Collectors.groupingBy(
                        Expense::getCategory,
                        Collectors.summingDouble(Expense::getAmount)
                ));
    }

    /**
     * Gets total expenses grouped by month.
     */
    public Map<YearMonth, Double> getMonthlyTotals() {
        return expenses.stream()
                .collect(Collectors.groupingBy(
                        e -> YearMonth.from(e.getDate()),
                        Collectors.summingDouble(Expense::getAmount)
                ));
    }

    private void save() {
        FileUtil.saveData(expenses, FILE_NAME);
    }
}
