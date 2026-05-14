package service;

import model.Category;
import util.FileUtil;

import java.util.List;

/**
 * Service class for managing categories.
 */
public class CategoryService {
    private static final String FILE_NAME = "categories.dat";
    private List<Category> categories;

    public CategoryService() {
        this.categories = FileUtil.loadData(FILE_NAME);
        // Initialize with default categories if empty
        if (this.categories.isEmpty()) {
            addCategory("Food");
            addCategory("Transport");
            addCategory("Utilities");
            addCategory("Entertainment");
            addCategory("Healthcare");
            addCategory("Shopping");
        }
    }

    /**
     * Adds a new category if it doesn't already exist.
     *
     * @param name Name of the category.
     * @return true if added, false if it already exists.
     */
    public boolean addCategory(String name) {
        Category newCategory = new Category(name.trim());
        if (!categories.contains(newCategory)) {
            categories.add(newCategory);
            save();
            return true;
        }
        return false;
    }

    /**
     * Retrieves all categories.
     *
     * @return List of categories.
     */
    public List<Category> getAllCategories() {
        return categories;
    }

    /**
     * Finds a category by its name (case-insensitive).
     *
     * @param name Name of the category to find.
     * @return The Category object, or null if not found.
     */
    public Category getCategoryByName(String name) {
        return categories.stream()
                .filter(c -> c.getName().equalsIgnoreCase(name.trim()))
                .findFirst()
                .orElse(null);
    }

    private void save() {
        FileUtil.saveData(categories, FILE_NAME);
    }
}
