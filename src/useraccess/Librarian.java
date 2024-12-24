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

    private final  File nrofbooksfile;


    public Librarian() throws IOException, ClassNotFoundException,
            BookNotFoundException, BillNotFoundException {


        books = new ArrayList<>();

        file = new File("bookstore.bin");

        if(!file.exists()) {
            file.createNewFile();

        } else {
            Readbooks();
        }

        BillFile = new File("BILL.txt");
        nrofbooksfile = new File("NrOfBooks.txt");

    }


    public ArrayList<Book> getBooks() {
        return this.books;
    }

    public void Readbooks() throws IOException, ClassNotFoundException, BookNotFoundException {

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

    public void addBookstolist(Book book){
        this.books.add(book);
    }

    public String getMonthlyTotal_books(String userid, String startDate, String endDate) throws IOException, DateNotValidException, ParseException, BookNotFoundException {

        isValid(startDate);
        isValid(endDate);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        Date start = dateFormat.parse(startDate);
        Date end = dateFormat.parse(endDate);

        if (start.compareTo(end) > 0) {
            throw new DateNotValidException("Starting date cannot be after end date");
        }


        if(!((nrofbooksfile.length()) == 0)) {

            try (BufferedReader bufferedReader = new BufferedReader(new FileReader("NrOfBooks.txt"))) {
                String line;

                int nr_of_books = 0;
                boolean userfound = false;

                while ((line = bufferedReader.readLine()) != null) {
                    String[] parts = line.split(",");

                    if (parts.length == 3) {
                        Date date = dateFormat.parse(parts[1]);

                        if (parts[0].equals(userid)) {
                            userfound = true;

                            if (date.compareTo(start) >= 0 && date.compareTo(end) <= 0) {
                                nr_of_books += Integer.parseInt(parts[2]);
                            }
                        }
                    }
                }

                bufferedReader.close();


                if (nr_of_books == 0) {
                    throw new BookNotFoundException("No books were sold by this user");
                }

                return String.valueOf(nr_of_books);
            }
        }

        else{
            throw new BookNotFoundException("There have been no books sold till now");
        }
    }

    public String getDailyTotal_books(String userid, String date) throws IOException, DateNotValidException, ParseException,BookNotFoundException {

        isValid(date);


        if(!((nrofbooksfile.length()) == 0)) {

            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(nrofbooksfile))) {
                String line;
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

                Date date1 = dateFormat.parse(date);

                int nr_of_books = 0;
                boolean userfound = false;

                while ((line = bufferedReader.readLine()) != null) {
                    String[] parts = line.split(",");

                    if (parts.length == 3) {
                        Date date2 = dateFormat.parse(parts[1]);

                        if (parts[0].equals(userid)) {

                            userfound = true;

                            if (date2.compareTo(date1) == 0) {
                                nr_of_books += Integer.parseInt(parts[2]);
                            }
                        }
                    }
                }

                bufferedReader.close();




                if (nr_of_books == 0) {

                    throw new BookNotFoundException("No books were sold by this user");

                }

                return String.valueOf(nr_of_books);
            }
        }

        else {
            throw new BookNotFoundException("There have been no books sold till now");
        }
    }

    public String getTotal_books() throws IOException,BookNotFoundException {

        if(!((nrofbooksfile.length()) == 0)) {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(nrofbooksfile))) {

                String line;
                int nr_of_books = 0;
                boolean librarianfound = false;

                while ((line = bufferedReader.readLine()) != null) {
                    String[] parts = line.split(",");
                    nr_of_books += Integer.parseInt(parts[2]);


                    }


                bufferedReader.close();



                if (nr_of_books == 0) {
                    throw new BookNotFoundException("No books are sold by this user");
                }

                return String.valueOf(nr_of_books);
            }
        }
        else {
            throw new BookNotFoundException("There have been no books sold till now");
        }


    }

    public ArrayList<Book> ItemsBought(String startdate, String enddate) throws BookNotFoundException, IOException, ClassNotFoundException, DateNotValidException, ParseException {

        isValid(startdate);
        isValid(enddate);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        Date start = dateFormat.parse(startdate);
        Date end = dateFormat.parse(enddate);


        if (start.compareTo(end) > 0) {
            throw new DateNotValidException("Starting date can not be after end date");
        }


        Readbooks();


        ArrayList<Book> booksbought = new ArrayList<>();


        for (Book book : getBooks()) {

            Date date = dateFormat.parse(book.getPurchased_date());

            if (date.compareTo(start) >= 0 && date.compareTo(end) <= 0) {

                booksbought.add(book);

            }
        }

        return booksbought;
    }




    public boolean isValid(String date) throws DateNotValidException {

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

        // If the parsing is successful (no exception thrown), the date is considered valid
        return true;
    }

    public String Bill(String date_of_transaction, String title, int quantity) throws
            ParseException, DateNotValidException, BookNotFoundException, IOException {
        {

            isValid(date_of_transaction);

            boolean isAvailable = false;
            boolean isOutOfStock = false;
            boolean isEnough = true;

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date transactionDate = dateFormat.parse(date_of_transaction);


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

                            file.append(date_of_transaction + "," + " " + book.getISBN() + "," + " " + book.getTitle() +
                                    "," + " " + quantity + "," + " " + String.valueOf(book.getSelling_price() * quantity)
                                    + "\n");

                            nrOfBooks(quantity, date_of_transaction);


                            return date_of_transaction + "," + book.getISBN() + "," + book.getTitle() +
                                    "," + quantity + "," + String.valueOf(book.getSelling_price() * quantity);
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
            }  finally {
                if (file != null) {
                    try {
                        file.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    public String nrOfBillsWithoutfilters() throws IOException, BillNotFoundException {


        if(!((BillFile.length()) == 0)) {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(BillFile))) {

                int nrofbills = 0;
                String line;
                boolean userfound = false;

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
        }

        else {
            throw new BillNotFoundException("There have been no books sold till now");
        }
    }

    public String nrOfDailyBills(String date) throws IOException, ParseException, DateNotValidException, BillNotFoundException {

        isValid(date);

        if(!(BillFile.length() == 0)) {

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
        }
        else {
            throw new BillNotFoundException("There have been no books sold till now");
        }

    }

    public String nrOfMonthlyBills(String userid, String startdate, String enddate) throws IOException, ParseException, DateNotValidException,
            BillNotFoundException {

        isValid(startdate);
        isValid(enddate);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        Date start = dateFormat.parse(startdate);
        Date end = dateFormat.parse(enddate);


        if (start.compareTo(end) > 0) {
            throw new DateNotValidException("Starting date can not be after end date");
        }

        if(!(BillFile.length() == 0)) {

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
        }

        else {
            throw new BillNotFoundException("There have been no books sold till now");
        }

    }




    public void nrOfBooks(int quantity, String date) throws IOException {


        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nrofbooksfile,true))) {
            writer.append(date+ "," + quantity);
            writer.close();
        }

    }

}





















