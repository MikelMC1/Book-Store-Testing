package useraccess;

import book.Book;
import exceptions.BillNotFoundException;
import exceptions.BookNotFoundException;
import exceptions.DateNotValidException;
import exceptions.ISBNnotValidException;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.io.File;



public class Manager extends Librarian implements Serializable {


    public Manager() throws IOException, ClassNotFoundException,
            BookNotFoundException {
        super();
    }

    public void readFile() throws BillNotFoundException {

        File file = new File("BILL.txt");

        if(file.length() == 0){
            throw new BillNotFoundException("No books have been sold till now");
        }

    }

    public void addBooks(Book book) throws IOException,
            ClassNotFoundException,
            ISBNnotValidException, DateNotValidException,
            BookNotFoundException {

        if (book.getISBN().length() < 6 || book.getISBN().length() > 13) {
            throw new ISBNnotValidException("ISBN should be at least 6 " +
                    "characters long");
        }

        if (book.getStock() <= 0) {
            throw new IOException("Stock cannot be less than one when you add" +
                    "a book");
        }

        isValid(book.getPurchased_date());

        super.Read_books();

        boolean exists = false;

        for (Book book1 : super.getBooks()) {

            if (book.getISBN().equals(book1.getISBN())) {

                if (book.getAuthor().equals(book1.getAuthor()) &&
                        book.getTitle().equals(book1.getTitle())) {
                    book1.setStock(book.getStock() + book1.getStock());
                    exists = true;
                    break;

                } else {
                    throw new ISBNnotValidException("ISBN should be unique for different books,so if you\n" +
                            "want to use ISBN =" + " " + book1.getISBN() + " " + "," + "you can use it for the book of title" +
                            " " + book1.getTitle() + " " + "written by" + " " + book1.getAuthor());
                }

            }

        }

        if (!exists) {
            super.addBookstolist(book);
        }

        super.writeBooksToFile();
        System.out.println(book);

    }


    public String getMonthly_Statistics_of_BooksSold(String startDate, String endDate) throws IOException, ParseException, DateNotValidException, BillNotFoundException {

        isValid(startDate);
        isValid(endDate);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        Date start = dateFormat.parse(startDate);
        Date end = dateFormat.parse(endDate);


        if (start.compareTo(end) > 0) {
            throw new DateNotValidException("Starting date can not be after " +
                    "end date");
        }

        readFile();

        BufferedReader bufferedReader = new BufferedReader(new FileReader("BILL.txt"));


        StringBuilder result = new StringBuilder();
        String line;


        while ((line = bufferedReader.readLine()) != null) {

            String[] parts = line.split(",");

            if (parts.length == 5) {

                Date date2 = dateFormat.parse(parts[0]);

                if (date2.compareTo(start) >= 0 && date2.compareTo(end) <= 0) {

                    result.append(parts[0])
                            .append(",")
                            .append(parts[2])
                            .append(",")
                            .append(parts[3])
                            .append(" ")
                            .append("book/s")
                            .append("\n");
                }

            }
        }

        bufferedReader.close();


        if (result.isEmpty()) {
            return "No books are sold from" + " " + startDate + " " + "to" +
                    " " + endDate;
        }

        result.setLength(result.length() - 1);
        return result.toString();

    }

    public String getDaily_Statistics_of_BooksSold(String date) throws ParseException,
            IOException, DateNotValidException, BillNotFoundException {

        isValid(date);

        readFile();

        BufferedReader bufferedReader = new BufferedReader(new FileReader("BILL.txt"));
        String line;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date1 = dateFormat.parse(date);

        StringBuilder result = new StringBuilder();

        while ((line = bufferedReader.readLine()) != null) {
            String[] parts = line.split(",");


            if (parts.length == 5) {

                Date date2 = dateFormat.parse(parts[0]);

                if (date2.compareTo(date1) == 0) {

                    result.append(parts[0])
                            .append(",")
                            .append(parts[2])
                            .append(",")
                            .append(parts[3])
                            .append(" ")
                            .append("book/s")
                            .append("\n");
                }
            }
        }

        bufferedReader.close();


        if (result.isEmpty()) {
            return "No books are sold during" + " " + date;
        }

        result.setLength(result.length() - 1);
        return result.toString();

    }

    public String getBooksSoldWithoutFilters() throws IOException,
            BillNotFoundException {

        readFile();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("BILL.txt"))) {
            StringBuilder result = new StringBuilder();

            String line;


            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(",");


                if (parts.length == 5) {

                    result.append(line).append("\n");

                }
            }

            bufferedReader.close();

            if (result.isEmpty()) {
                return "No books are sold till now";
            }

            result.setLength(result.length() - 1);
            return result.toString();
        }
    }


    public ArrayList<Book> Daily_Statistics_ofBooksBought(String date) throws ParseException, IOException, ClassNotFoundException, DateNotValidException,
            BookNotFoundException {

        isValid(date);

        super.Read_books();

        ArrayList<Book> books_bought = new ArrayList<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date1 = dateFormat.parse(date);

        int number_of_books = 0;


        for (Book book : super.getBooks()) {

            Date date2 = dateFormat.parse(book.getPurchased_date());

            if (date1.equals(date2)) {

                books_bought.add(book);
                number_of_books++;

            }
        }


        if (number_of_books == 0) {
            throw new BookNotFoundException("No books were bought during" + " " + date);
        }


        return books_bought;
    }

    public ArrayList<Book> Statistics_of_BooksBought_without_Filters() throws IOException,
            ClassNotFoundException {

        super.Read_books();

        return new ArrayList<>(super.getBooks());

    }

    public ArrayList<Book> Monthly_Statistics_of_BooksBought(String startDate, String endDate) throws ParseException, IOException,
            ClassNotFoundException, DateNotValidException, BookNotFoundException
    {

        isValid(startDate);
        isValid(endDate);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date Startdate = dateFormat.parse(startDate);
        Date Enddate = dateFormat.parse(endDate);

        if (Startdate.compareTo(Enddate) > 0) {
            throw new DateNotValidException("Starting date can not be after end date");
        }


        super.Read_books();

        ArrayList<Book> books_bought = new ArrayList<>();

        int number_of_books = 0;


        for (Book book : super.getBooks()) {

            Date date = dateFormat.parse(book.getPurchased_date());

            if (date.compareTo(Startdate) >= 0 && date.compareTo(Enddate) <= 0) {
                books_bought.add(book);
                number_of_books++;
            }
        }


        if (number_of_books == 0) {
            throw new BookNotFoundException("No books were bought from" + " " + startDate + " " + "to" + " " + endDate);
        }

        return books_bought;

    }

    public String incomes_Without_filters() throws IOException,
            BillNotFoundException {

        readFile();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("BILL.txt"))) {

            int incomes = 0;
            String line;


            while ((line = bufferedReader.readLine()) != null) {

                String[] parts = line.split(",");

                if (parts.length == 5) {



                        if (!line.trim().isEmpty()) {
                            incomes += Integer.parseInt(parts[4]);
                        }
                    }

            }

            bufferedReader.close();



            return String.valueOf(incomes);

        } catch (FileNotFoundException e) {
            // Handle the case where the file is not found (empty file)
            return "0";
        }
    }

    public double daily_incomes(String date) throws IOException,
            ParseException, DateNotValidException, BillNotFoundException {

        isValid(date);

        readFile();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date1 = dateFormat.parse(date);

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("BILL.txt"))) {

            double incomes = 0.0;
            String line;


            while ((line = bufferedReader.readLine()) != null) {

                String[] parts = line.split(",");

                if (parts.length == 5) {
                    Date date2 = dateFormat.parse(parts[0]);

                     if (date1.compareTo(date2) == 0) {

                            if (!line.trim().isEmpty()) {
                                incomes += Double.parseDouble(parts[4]);
                            }
                        }
                    }

            }

            bufferedReader.close();

            return incomes;

        } catch (FileNotFoundException e) {
            // Handle the case where the file is not found (empty file)
            return 0;
        }


    }

    public double monthly_incomes(String start_date, String end_date) throws IOException,
            ParseException, DateNotValidException, BillNotFoundException {

        isValid(start_date);
        isValid(end_date);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        Date start = dateFormat.parse(start_date);
        Date end = dateFormat.parse(end_date);


        if (start.compareTo(end) > 0) {
            throw new DateNotValidException("Starting date can not be after end date");
        }

        readFile();



        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("BILL.txt"))) {

            double incomes = 0.0;
            String line;

            while ((line = bufferedReader.readLine()) != null) {

                String[] parts = line.split(",");

                if (parts.length == 5) {

                    Date date = dateFormat.parse(parts[0]);

                        if (date.compareTo(start) >= 0 && date.compareTo(end) <= 0) {


                            if (!line.trim().isEmpty()) {
                                incomes += Double.parseDouble(parts[4]);
                            }

                    }
                }
            }

            bufferedReader.close();



            return incomes;

        } catch (FileNotFoundException e) {
            // Handle the case where the file is not found (empty file)
            return 0.0;
        }

    }


    public String total_of_BooksSold(String ISBN, String startdate, String enddate) throws IOException, DateNotValidException, ParseException, BillNotFoundException, BookNotFoundException {

        isValid(startdate);
        isValid(enddate);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        Date start = dateFormat.parse(startdate);
        Date end = dateFormat.parse(enddate);

        if (start.compareTo(end) > 0) {
            throw new DateNotValidException("Starting date can not be after end date");
        }

        readFile();

        BufferedReader bufferedReader = new BufferedReader(new FileReader("BILL.txt"));
        StringBuilder stringBuilder = new StringBuilder();

        String line;
        int numberofbooks = 0;


        while ((line = bufferedReader.readLine()) != null) {

            String[] parts = line.split(",");


            if (parts.length == 5) {

                Date date = dateFormat.parse(parts[0]);

                if (date.compareTo(start) >= 0 && date.compareTo(end) <= 0) {

                    if (parts[1].equals(ISBN)) {

                        stringBuilder.append(line).append("\n");
                        numberofbooks++;

                    }
                }

            }
        }

        if (numberofbooks == 0) {
            throw new BookNotFoundException("No books are sold till now");
        }

        return stringBuilder.toString();

    }



    public double getTotalCostAdmin(String start_date, String end_date) throws DateNotValidException, ParseException
    ,IOException, ClassNotFoundException {

        isValid(start_date);
        isValid(end_date);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");


        Date start = dateFormat.parse(start_date);
        Date end = dateFormat.parse(end_date);


        if (start.compareTo(end) > 0) {
            throw new DateNotValidException("Starting date can not be after end date");
        }

        super.Read_books();

        double totalcost = 0;


        for (Book book : super.getBooks()) {
            totalcost += book.getPurchased_price() * book.getStock();
        }

        return totalcost;

    }

    public double totalIncomesAdmin(String start_date, String end_date) throws IOException, BookNotFoundException, DateNotValidException, ParseException, BillNotFoundException {

        isValid(start_date);
        isValid(end_date);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        Date start = dateFormat.parse(start_date);
        Date end = dateFormat.parse(end_date);

        if (start.compareTo(end) > 0) {
            throw new DateNotValidException("Starting date can not be after end date");
        }


        readFile();

        BufferedReader bufferedReader = new BufferedReader(new FileReader("BILL.txt"));
        

        String line;
        int numberofbooks = 0;
        double totalincomes = 0;


        while ((line = bufferedReader.readLine()) != null) {

            String[] parts = line.split(",");


            if (parts.length == 6) {

                Date date = dateFormat.parse(parts[1]);

                if (date.compareTo(start) >= 0 && date.compareTo(end) <= 0) {

                    numberofbooks++;
                    totalincomes += Double.parseDouble(parts[5]);

                }
            }
        }

        if (numberofbooks == 0) {
            throw new BookNotFoundException("No books are sold from" + " " + 
                    start_date + " " + "to" + " " + end_date);
        }

        return totalincomes;

    }



    public double profit(String start_date, String end_date) throws DateNotValidException, BookNotFoundException, ParseException, IOException, ClassNotFoundException, BillNotFoundException {


        return totalIncomesAdmin(start_date,end_date) - getTotalCostAdmin(start_date,end_date);


    }


}






















