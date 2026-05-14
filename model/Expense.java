package model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Represents an individual expense record.
 */
public class Expense implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private LocalDate date;
    private double amount;
    private Category category;
    private String description;

    public Expense(int id, LocalDate date, double amount, Category category, String description) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.category = category;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return String.format("ID: %-5d | Date: %-12s | Amount: $%-8.2f | Category: %-15s | Desc: %s",
                id, date, amount, category.getName(), description);
    }
}
