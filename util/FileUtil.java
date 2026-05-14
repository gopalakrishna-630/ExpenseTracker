package util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for handling file operations (serialization/deserialization).
 */
public class FileUtil {

    /**
     * Saves a list of objects to a file using serialization.
     *
     * @param data     The list of objects to save.
     * @param filename The name of the file to save to.
     * @param <T>      The type of objects in the list.
     */
    public static <T> void saveData(List<T> data, String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(data);
        } catch (IOException e) {
            System.err.println("Error saving data to " + filename + ": " + e.getMessage());
        }
    }

    /**
     * Loads a list of objects from a file using deserialization.
     *
     * @param filename The name of the file to load from.
     * @param <T>      The expected type of objects in the list.
     * @return The loaded list of objects, or an empty list if the file doesn't exist or an error occurs.
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> loadData(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<T>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading data from " + filename + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
