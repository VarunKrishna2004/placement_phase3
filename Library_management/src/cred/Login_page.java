package cred;

import java.sql.*;
import java.util.Scanner;

public class Login_page {
    private static Connection connection;

    public static void main(String[] args) {
        try {
            // Connect to MySQL database
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Librarymanagement", "root", "VarunK_6");

            Scanner scanner = new Scanner(System.in);
            System.out.println("Welcome to the Library Management System");
            System.out.print("Enter User ID: ");
            String userId = scanner.next();
            System.out.print("Enter Password: ");
            String password = scanner.next();

            if (authenticateUser(userId, password)) {
                System.out.println("Login successful!");
                System.out.print("Enter (1) to Borrow a Book or (2) to Return a Book: ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        borrowBook();
                        break;
                    case 2:
                        returnBook();
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }
            } else {
                System.out.println("Invalid login credentials.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static boolean authenticateUser(String userId, String password) {
        try {
            String query = "SELECT * FROM employee WHERE user_id = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, userId);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void borrowBook() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Search for a book to borrow:");
        System.out.println("Enter (1) to search by Name, (2) by Author, (3) by Genre, or (4) by Book ID:");
        int searchOption = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        String searchQuery = "";
        String searchTerm = "";

        try {
            switch (searchOption) {
                case 1:
                    System.out.print("Enter book name: ");
                    searchTerm = scanner.nextLine();
                    searchQuery = "SELECT * FROM books WHERE name LIKE ?";
                    break;
                case 2:
                    System.out.print("Enter author name: ");
                    searchTerm = scanner.nextLine();
                    searchQuery = "SELECT * FROM books WHERE author LIKE ?";
                    break;
                case 3:
                    System.out.print("Enter book genre: ");
                    searchTerm = scanner.nextLine();
                    searchQuery = "SELECT * FROM books WHERE Genre LIKE ?";
                    break;
                case 4:
                    System.out.print("Enter book ID: ");
                    searchTerm = scanner.nextLine();
                    searchQuery = "SELECT * FROM books WHERE book_id = ?";
                    break;
                default:
                    System.out.println("Invalid search option.");
                    return;
            }

            PreparedStatement searchStmt = connection.prepareStatement(searchQuery);
            if (searchOption == 4) {
                searchStmt.setInt(1, Integer.parseInt(searchTerm));
            } else {
                searchStmt.setString(1, "%" + searchTerm + "%");
            }

            ResultSet resultSet = searchStmt.executeQuery();
            System.out.println("Search Results:");
            while (resultSet.next()) {
                System.out.println("Book ID: " + resultSet.getInt("book_id") + ", Name: " + resultSet.getString("name") +
                                   ", Author: " + resultSet.getString("author") + ", Genre: " + resultSet.getString("Genre") +
                                   ", Available: " + resultSet.getInt("available") + ", Borrowed: " + resultSet.getInt("borrowed"));
            }

            System.out.print("Enter the Book ID to borrow: ");
            int bookId = scanner.nextInt();

            String queryCheckStock = "SELECT available FROM books WHERE book_id = ?";
            PreparedStatement checkStockStmt = connection.prepareStatement(queryCheckStock);
            checkStockStmt.setInt(1, bookId);
            ResultSet stockResult = checkStockStmt.executeQuery();

            if (stockResult.next() && stockResult.getInt("available") > 0) {
                String queryUpdateStock = "UPDATE books SET available = available - 1, borrowed = borrowed + 1 WHERE book_id = ?";
                PreparedStatement updateStockStmt = connection.prepareStatement(queryUpdateStock);
                updateStockStmt.setInt(1, bookId);
                updateStockStmt.executeUpdate();

                System.out.println("Book borrowed successfully!");
            } else {
                System.out.println("Book not available for borrowing.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void returnBook() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter Book ID to return: ");
            int bookId = scanner.nextInt();

            String queryUpdateStock = "UPDATE books SET available = available + 1, borrowed = borrowed - 1 WHERE book_id = ?";
            PreparedStatement updateStockStmt = connection.prepareStatement(queryUpdateStock);
            updateStockStmt.setInt(1, bookId);
            updateStockStmt.executeUpdate();

            System.out.println("Book returned successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
