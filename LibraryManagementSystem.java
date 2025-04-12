import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

class Book {
    String title;
    String author;
    boolean isBorrowed;

    public Book(String title, String author, boolean isBorrowed) {
        this.title = title;
        this.author = author;
        this.isBorrowed = isBorrowed;
    }

    public Book(String title, String author) {
        this(title, author, false);
    }

    public void borrowBook() {
        if (!isBorrowed) {
            isBorrowed = true;
            System.out.println("You borrowed \"" + title + "\"");
        } else {
            System.out.println("Sorry, this book is already borrowed.");
        }
    }

    public void returnBook() {
        if (isBorrowed) {
            isBorrowed = false;
            System.out.println("You returned \"" + title + "\"");
        } else {
            System.out.println("This book was not borrowed.");
        }
    }

    public void displayInfo() {
        System.out.println("Title: " + title + " | Author: " + author + " | Available: " + (!isBorrowed));
    }

    public String toFileString() {
        return title + "," + author + "," + isBorrowed;
    }
}

class Library {
    ArrayList<Book> books = new ArrayList<>();
    final String FILE_NAME = "books.txt";

    public Library() {
        loadBooksFromFile();
    }

    public void addBook(Book book) {
        books.add(book);
        System.out.println("Book \"" + book.title + "\" added.");
        saveBooksToFile();
    }

    public void showAllBooks() {
        if (books.isEmpty()) {
            System.out.println("No books in the library.");
        } else {
            for (Book book : books) {
                book.displayInfo();
            }
        }
    }

    public void searchBook(String title) {
        boolean found = false;
        for (Book book : books) {
            if (book.title.equalsIgnoreCase(title)) {
                book.displayInfo();
                found = true;
            }
        }
        if (!found) {
            System.out.println("Book not found.");
        }
    }

    public Book getBookByTitle(String title) {
        for (Book book : books) {
            if (book.title.equalsIgnoreCase(title)) {
                return book;
            }
        }
        return null;
    }

    public void saveBooksToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Book book : books) {
                writer.write(book.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving books: " + e.getMessage());
        }
    }

    public void loadBooksFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String title = parts[0];
                    String author = parts[1];
                    boolean isBorrowed = Boolean.parseBoolean(parts[2]);
                    books.add(new Book(title, author, isBorrowed));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading books: " + e.getMessage());
        }
    }
}

public class LibraryManagementSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Library library = new Library();

        while (true) {
            System.out.println("\n===== Library Menu =====");
            System.out.println("1. Add Book");
            System.out.println("2. Show All Books");
            System.out.println("3. Search Book");
            System.out.println("4. Borrow Book");
            System.out.println("5. Return Book");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    System.out.print("Enter title: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter author: ");
                    String author = scanner.nextLine();
                    library.addBook(new Book(title, author));
                    break;

                case 2:
                    library.showAllBooks();
                    break;

                case 3:
                    System.out.print("Enter title to search: ");
                    String searchTitle = scanner.nextLine();
                    library.searchBook(searchTitle);
                    break;

                case 4:
                    System.out.print("Enter book title to borrow: ");
                    String borrowTitle = scanner.nextLine();
                    Book bookToBorrow = library.getBookByTitle(borrowTitle);
                    if (bookToBorrow != null) {
                        bookToBorrow.borrowBook();
                        library.saveBooksToFile();
                    } else {
                        System.out.println("Book not found.");
                    }
                    break;

                case 5:
                    System.out.print("Enter book title to return: ");
                    String returnTitle = scanner.nextLine();
                    Book bookToReturn = library.getBookByTitle(returnTitle);
                    if (bookToReturn != null) {
                        bookToReturn.returnBook();
                        library.saveBooksToFile();
                    } else {
                        System.out.println("Book not found.");
                    }
                    break;

                case 6:
                    System.out.println("Exiting Library System...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }
}

