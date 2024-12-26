package main;

import book.Book;
import exceptions.BillNotFoundException;
import exceptions.BookNotFoundException;
import exceptions.DateNotValidException;
import exceptions.ISBNnotValidException;
import useraccess.Manager;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws BillNotFoundException, ISBNnotValidException, DateNotValidException {
       

        try {
            Scanner sc = new Scanner(System.in);
            Manager manager = new Manager();

            while (true) {
                System.out.println("\nChoose an option:");
                System.out.println("1. Add a book");
                System.out.println("2. Generate a bill");
                System.out.println("3. Display book details from file");
                System.out.println("4. View monthly incomes");
                System.out.println("5. View number of bills and total books");
                System.out.println("6. View monthly statistics of books sold");
                System.out.println("7. View daily statistics of books sold");
                System.out.println("8. View monthly statistics of books bought");
                System.out.println("Enter your choice:");

                if (!sc.hasNextInt()) { // Validate input type
                    System.out.println("Invalid input. Please enter a number.");
                    sc.nextLine(); // Clear invalid input
                    continue;
                }

                int choice = sc.nextInt();
                sc.nextLine(); // Consume leftover newline

                switch (choice) {
                    case 1:
                        System.out.println("Enter ISBN:");
                        String ISBN = sc.nextLine();

                        System.out.println("Enter author:");
                        String author = sc.nextLine();

                        System.out.println("Enter title:");
                        String title = sc.nextLine();

                        System.out.println("Enter book category:");
                        String bookCategory = sc.nextLine();

                        LocalDate currentDate = LocalDate.now();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMMM/yyyy");
                        String purchasedDate = currentDate.format(formatter);
                        System.out.println("Purchased date set to: " + purchasedDate);

                        System.out.println("Enter purchased price:");
                        while (!sc.hasNextDouble()) {
                            System.out.println("Invalid input. Please enter a valid number for the price:");
                            sc.nextLine();
                        }
                        double purchasedPrice = sc.nextDouble();
                        sc.nextLine();

                        System.out.println("Enter selling price:");
                        while (!sc.hasNextDouble()) {
                            System.out.println("Invalid input. Please enter a valid number for the price:");
                            sc.nextLine();
                        }
                        double sellingPrice = sc.nextDouble();
                        sc.nextLine();

                        System.out.println("Enter stock quantity:");
                        while (!sc.hasNextInt()) {
                            System.out.println("Invalid input. Please enter a valid number for stock:");
                            sc.nextLine();
                        }
                        int stock = sc.nextInt();
                        sc.nextLine();

                        manager.addBooks(new Book(ISBN, author, title, bookCategory, purchasedDate, purchasedPrice, sellingPrice, stock));
                        System.out.println("Book added successfully!");
                        break;

                    case 2:
                        System.out.println("Enter the date of the transaction (dd/MM/yyyy):");
                        String transactionDate = sc.nextLine();

                        System.out.println("Enter the requested book title:");
                        String requestedBook = sc.nextLine();

                        System.out.println("Enter the quantity:");
                        while (!sc.hasNextInt()) {
                            System.out.println("Invalid input. Please enter a valid number for quantity:");
                            sc.nextLine();
                        }
                        int quantity = sc.nextInt();
                        sc.nextLine();

                        manager.Bill(transactionDate, requestedBook, quantity);
                        System.out.println("Bill generated successfully!");
                        break;

                    case 3:
                        System.out.println("Reading book details from file...");
                        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("bookstore.bin"))) {
                            System.out.println("Book details: " + objectInputStream.readObject());
                        }
                        break;

                    case 4:
                        System.out.println("Enter start date (dd/MM/yyyy):");
                        String startDate = sc.nextLine();
                        System.out.println("Enter end date (dd/MM/yyyy):");
                        String endDate = sc.nextLine();
                        System.out.println("Monthly incomes: " + manager.monthlyincomes(startDate, endDate));
                        break;

                    case 5:
                        System.out.println(manager.nrOfBillsWithoutfilters());
                        break;

                    case 6:
                        System.out.println("Enter start date (dd/MM/yyyy):");
                        String startStatsDate = sc.nextLine();
                        System.out.println("Enter end date (dd/MM/yyyy):");
                        String endStatsDate = sc.nextLine();
                        manager.getMonthly_Statistics_of_BooksSold(startStatsDate, endStatsDate);
                        System.out.println("Monthly statistics retrieved.");
                        break;

                    case 7:
                        System.out.println("Daily statistics feature is not implemented yet.");
                        break;

                    case 8:
                        System.out.println("Enter start date (dd/MM/yyyy):");
                        String startBoughtDate = sc.nextLine();
                        System.out.println("Enter end date (dd/MM/yyyy):");
                        String endBoughtDate = sc.nextLine();
                        manager.Monthly_Statistics_of_BooksBought(startBoughtDate, endBoughtDate);
                        System.out.println("Monthly statistics of books bought retrieved.");
                        break;

                    default:
                        System.out.println("Invalid choice. Please select a valid option.");
                        break;
                }
            }
        } catch (IOException | ClassNotFoundException | ParseException e) {
            System.out.println(e.getMessage());

        } catch (BookNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
