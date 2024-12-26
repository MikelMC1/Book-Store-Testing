package useraccess;


import book.Book;
import exceptions.BillNotFoundException;
import exceptions.BookNotFoundException;
import exceptions.DateNotValidException;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Librarian extends Book implements Serializable {

    private ArrayList<Book> books;

    private final File file;

    private final File BillFile;

    private final File nrofbooksfile;


    public Librarian() throws IOException, ClassNotFoundException {


        books = new ArrayList<>();

        file = new File("bookstore.bin");

        if (!file.exists()) {
            if (file.createNewFile()) {
                System.out.println("Created new bookstore.bin file.");
            } else {
                System.out.println("Failed to create bookstore.bin file.");
            }
        } else {
            Read_books();
        }
        BillFile = new File("BILL.txt");
        nrofbooksfile = new File("NrOfBooks.txt");

    }


    public ArrayList<Book> getBooks() {
        return this.books;
    }

    public void Read_books() throws IOException, ClassNotFoundException {

        if (!((file.length()) == 0)) {

            FileInputStream fileInput = new FileInputStream(file);
            ObjectInputStream objectInput = new ObjectInputStream(fileInput);

            books = (ArrayList<Book>) objectInput.readObject();
            fileInput.close();
            objectInput.close();

        }

    }

    public void writeBooksToFile() throws IOException {
        ObjectOutputStream objectOutput = new ObjectOutputStream(new FileOutputStream(file));
        objectOutput.writeObject(books);
        objectOutput.close();
    }

    public void addBookstolist(Book book) {
        this.books.add(book);
    }

    public String getMonthlyTotal_books(String startDate, String endDate) throws IOException, DateNotValidException, ParseException, BookNotFoundException {

        isValid(startDate);
        isValid(endDate);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        Date start = dateFormat.parse(startDate);
        Date end = dateFormat.parse(endDate);

        if (start.compareTo(end) > 0) {
            throw new DateNotValidException("Starting date cannot be after end date");
        }


        if (!((nrofbooksfile.length()) == 0)) {

            try (BufferedReader bufferedReader = new BufferedReader(new FileReader("NrOfBooks.txt"))) {
                String line;

                int nr_of_books = 0;


                while ((line = bufferedReader.readLine()) != null) {
                    String[] parts = line.split(",");

                    if (parts.length == 2) {
                        Date date = dateFormat.parse(parts[0]);

                        if (date.compareTo(start) >= 0 && date.compareTo(end) <= 0) {
                            nr_of_books += Integer.parseInt(parts[1]);
                        }
                    }

                }

                bufferedReader.close();


                if (nr_of_books == 0) {
                    throw new BookNotFoundException("No books were sold by this user");
                }

                return String.valueOf(nr_of_books);
            }
        } else {
            throw new BookNotFoundException("There have been no books sold till now");
        }
    }

    public String getDailyTotal_books(String date) throws IOException, DateNotValidException, ParseException, BookNotFoundException {

        isValid(date);


        if (!((nrofbooksfile.length()) == 0)) {

            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(nrofbooksfile))) {
                String line;
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

                Date date1 = dateFormat.parse(date);

                int nr_of_books = 0;


                while ((line = bufferedReader.readLine()) != null) {
                    String[] parts = line.split(",");

                    if (parts.length == 2) {
                        Date date2 = dateFormat.parse(parts[0]);

                        if (date2.compareTo(date1) == 0) {
                            nr_of_books += Integer.parseInt(parts[1]);
                        }

                    }
                }

                bufferedReader.close();

                if (nr_of_books == 0) {

                    throw new BookNotFoundException("No books were sold by this user");

                }

                return String.valueOf(nr_of_books);
            }
        } else {
            throw new BookNotFoundException("There have been no books sold till now");
        }
    }

    public String getTotal_books() throws IOException, BookNotFoundException {

        if (!((nrofbooksfile.length()) == 0)) {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(nrofbooksfile))) {

                String line;
                int nr_of_books = 0;


                while ((line = bufferedReader.readLine()) != null) {
                    String[] parts = line.split(",");
                    nr_of_books += Integer.parseInt(parts[1]);


                }


                bufferedReader.close();


                if (nr_of_books == 0) {
                    throw new BookNotFoundException("No books are sold by this user");
                }

                return String.valueOf(nr_of_books);
            }
        } else {
            throw new BookNotFoundException("There have been no books sold till now");
        }


    }

    public ArrayList<Book> ItemsBought(String startdate, String enddate) throws IOException, ClassNotFoundException, DateNotValidException, ParseException {

        isValid(startdate);
        isValid(enddate);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        Date start = dateFormat.parse(startdate);
        Date end = dateFormat.parse(enddate);


        if (start.compareTo(end) > 0) {
            throw new DateNotValidException("Starting date can not be after end date");
        }


        Read_books();


        ArrayList<Book> booksbought = new ArrayList<>();


        for (Book book : getBooks()) {

            Date date = dateFormat.parse(book.getPurchased_date());

            if (date.compareTo(start) >= 0 && date.compareTo(end) <= 0) {

                booksbought.add(book);

            }
        }

        return booksbought;
    }


    public void isValid(String date) throws DateNotValidException {

        DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);

        try {
            // Attempt to parse the given date string using the specified date format
            sdf.parse(date);
        } catch (ParseException e) {
            // If a ParseException is caught, throw a custom exception indicating that the date is not valid
            throw new DateNotValidException("Invalid date format. The format required for the date to be entered is"
                    + " " + "dd/MM/yyyy");
        }


    }

    public String Bill(String date_of_transaction, String title, int quantity) throws
            ParseException, DateNotValidException, BookNotFoundException, IOException {
        {

            isValid(date_of_transaction);

            boolean isOutOfStock = false;
            boolean isEnough = true;


            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String formattedCurrentDate = currentDate.format(formatter);

            if (!formattedCurrentDate.equals(date_of_transaction)) {
                throw new DateNotValidException("Transaction date should be equal to the current date.");
            }

            BufferedWriter file = null;

            try {
                file = new BufferedWriter(new FileWriter(BillFile, true));

                for (Book book : books) {

                    if (book.getTitle().equalsIgnoreCase(title)) {

                        if (book.getStock() == 0) {
                            isOutOfStock = true;
                            break;
                        } else if (quantity <= book.getStock()) {
                            book.setStock(book.getStock() - quantity);
                            writeBooksToFile();
                            String billEntry = date_of_transaction + ", " +
                                    book.getISBN() + ", " +
                                    book.getTitle() + ", " +
                                    quantity + ", " +
                                    (book.getSelling_price() * quantity) + "\n";

                            file.append(billEntry);

                            nr_Of_Books(quantity, date_of_transaction);


                            return date_of_transaction + "," + book.getISBN() + "," + book.getTitle() +
                                    "," + quantity + "," + (book.getSelling_price() * quantity);
                        } else {
                            isEnough = false;
                            break;
                        }
                    }
                }


                if (isOutOfStock) {

                    throw new BookNotFoundException(title + " " + " is out of stock");
                }

                if (!isEnough) {
                    throw new BookNotFoundException("There is not enough copies of " + " " + title);
                }
                throw new BookNotFoundException(title + " " + "is not available");

            } catch (IOException e) {
                throw new IOException(e);
            } finally {
                if (file != null) {
                    try {
                        file.close();
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }

    }

    public String nrOfBillsWithoutfilters() throws IOException, BillNotFoundException {

        if (!((BillFile.length()) == 0)) {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(BillFile))) {

                int nrofbills = 0;
                String line;

                while ((line = bufferedReader.readLine()) != null) {

                    String[] parts = line.split(",");

                    if (parts.length == 5) {

                        if (!line.trim().isEmpty()) {
                            nrofbills++;
                        }
                    }

                }

                bufferedReader.close();


                if (nrofbills == 0) {
                    throw new BillNotFoundException();
                }

                return String.valueOf(nrofbills);

            } catch (FileNotFoundException e) {
                // Handle the case where the file is not found (empty file)
                return "0";
            }
        } else {
            throw new BillNotFoundException("There have been no books sold till now");
        }
    }

    public String nrOfDailyBills(String date) throws IOException, ParseException, DateNotValidException, BillNotFoundException {

        isValid(date);

        if (!(BillFile.length() == 0)) {

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date1 = dateFormat.parse(date);

            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(BillFile))) {

                int nrofbills = 0;
                String line;


                while ((line = bufferedReader.readLine()) != null) {

                    String[] parts = line.split(",");

                    if (parts.length == 5) {


                        Date date2 = dateFormat.parse(parts[0]);

                        if (date1.compareTo(date2) == 0) {

                            if (!line.trim().isEmpty()) {
                                nrofbills++;
                            }
                        }
                    }
                }


                bufferedReader.close();


                if (nrofbills == 0) {
                    throw new BillNotFoundException();
                }

                return String.valueOf(nrofbills);

            } catch (FileNotFoundException e) {
                // Handle the case where the file is not found (empty file)
                return "0";
            }
        } else {
            throw new BillNotFoundException("There have been no books sold till now");
        }

    }

    public String nrOfMonthlyBills(String start_date, String end_date) throws IOException, ParseException, DateNotValidException,
            BillNotFoundException {

        isValid(start_date);
        isValid(end_date);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        Date start = dateFormat.parse(start_date);
        Date end = dateFormat.parse(end_date);


        if (start.compareTo(end) > 0) {
            throw new DateNotValidException("Starting date can not be after end date");
        }

        if (!(BillFile.length() == 0)) {

            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(BillFile))) {

                int nrofbills = 0;
                String line;


                while ((line = bufferedReader.readLine()) != null) {

                    String[] parts = line.split(",");

                    if (parts.length == 5) {

                        Date date = dateFormat.parse(parts[0]);

                        if (date.compareTo(start) >= 0 && date.compareTo(end) <= 0) {

                            if (!line.trim().isEmpty()) {
                                nrofbills++;
                            }
                        }

                    }
                }

                bufferedReader.close();


                if (nrofbills == 0) {
                    throw new BillNotFoundException();
                }

                return String.valueOf(nrofbills);

            } catch (FileNotFoundException e) {
                // Handle the case where the file is not found (empty file)
                return "0";
            }
        } else {
            throw new BillNotFoundException("There have been no books sold till now");
        }

    }


    public void nr_Of_Books(int quantity, String date) throws IOException, ParseException {
        File file = new File("NrOfBooks.txt");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date transactionDate = dateFormat.parse(date);

        // Read all lines from the file
        List<String> lines = new ArrayList<>();
        boolean updated = false;

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length == 2) {
                    Date fileDate = dateFormat.parse(parts[0]);

                    // Update the line if the date matches
                    if (fileDate.compareTo(transactionDate) == 0) {
                        int currentQuantity = Integer.parseInt(parts[1]);
                        int newQuantity = currentQuantity + quantity;
                        lines.add(parts[0] + "," + newQuantity); // Update the line
                        updated = true;
                    } else {
                        lines.add(line); // Keep the line as is
                    }
                }
            }
        }

        // If the date was not found, add a new line
        if (!updated) {
            lines.add(date + "," + quantity);
        }

        // Write all lines back to the file
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            for (String updatedLine : lines) {
                bufferedWriter.write(updatedLine);
                bufferedWriter.newLine();
            }
        }
    }

}





















