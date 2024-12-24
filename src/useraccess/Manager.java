package useraccess;

import book.Book;
import exceptions.BillNotFoundException;
import exceptions.BookNotFoundException;
import exceptions.DateNotValidException;
import exceptions.ISBNnotValidException;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.io.File;



public class Manager extends Librarian implements Serializable {


    public Manager() throws IOException, ClassNotFoundException,
            BookNotFoundException,
            BillNotFoundException {
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

        isValid(book.getPurchased_date());

        super.Readbooks();

        boolean exists = false;

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedCurrentDate = currentDate.format(formatter);

        if (!formattedCurrentDate.equals(book.getPurchased_date())) {
            throw new DateNotValidException("Put the current date");
        }

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
        System.out.println(book.toString());

    }




    public String getMonthly_Statistics_of_BooksSold(String startDate, String endDate) throws IOException, ParseException, DateNotValidException, BillNotFoundException {

        isValid(startDate);
        isValid(endDate);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        Date start = dateFormat.parse(startDate);
        Date end = dateFormat.parse(endDate);


        if (start.compareTo(end) > 0) {
            throw new DateNotValidException("Starting date can not be after end date");
        }

        readFile();

        BufferedReader bufferedReader = new BufferedReader(new FileReader("BILL.txt"));


        StringBuilder result = new StringBuilder();
        String line;


        if (start.compareTo(end) > 0) {
            throw new DateNotValidException("Starting date can not be after end date");
        }

        while ((line = bufferedReader.readLine()) != null) {

            String[] parts = line.split(",");

            if (parts.length == 5) {

                Date date2 = dateFormat.parse(parts[0]);

                if (date2.compareTo(start) >= 0 && date2.compareTo(end) <= 0) {

                    result.append(line).append("\n");
                }

            }
        }

        bufferedReader.close();


        if (result.length() == 0) {
            return "No books are sold from" + startDate + " " + "to" + " " + endDate;
        }


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

                    result.append(line).append("\n");
                }
            }
        }

        bufferedReader.close();


        if (result.length() == 0) {
            return "No books are sold during" + " " + date;
        }


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

            if (result.length() == 0) {
                return "No books are sold till now";
            }

            return result.toString();
        }
    }


    public ArrayList<Book> Daily_Statistics_ofBooksBought(String date) throws ParseException, IOException, ClassNotFoundException, DateNotValidException,
            BookNotFoundException {

        isValid(date);

        super.Readbooks();

        ArrayList<Book> booksbought = new ArrayList<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date1 = dateFormat.parse(date);

        int number_of_books = 0;


        for (Book book : super.getBooks()) {

            Date date2 = dateFormat.parse(book.getPurchased_date());

            if (date1.equals(date2)) {

                booksbought.add(book);
                number_of_books++;

            }
        }


        if (number_of_books == 0) {
            throw new BookNotFoundException("No books were bought during" + " " + date);
        }


        return booksbought;
    }

    public ArrayList<Book> Statistics_of_BooksBought_without_Filters() throws ParseException, IOException,
            ClassNotFoundException, BookNotFoundException {

        super.Readbooks();

        ArrayList<Book> booksbought = new ArrayList<>();


        for (Book book : super.getBooks()) {

            booksbought.add(book);

        }

        return booksbought;

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


        super.Readbooks();

        ArrayList<Book> booksbought = new ArrayList<>();

        int number_of_books = 0;


        for (Book book : super.getBooks()) {

            Date date = dateFormat.parse(book.getPurchased_date());

            if (date.compareTo(Startdate) >= 0 && date.compareTo(Enddate) <= 0) {
                booksbought.add(book);
                number_of_books++;
            }
        }


        if (number_of_books == 0) {
            throw new BookNotFoundException("No books were bought from" + " " + startDate + " " + "to" + " " + endDate);
        }

        return booksbought;

    }

    public String incomesWithoutfilters() throws IOException,
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

    public String dailyincomes(String date) throws IOException,
            ParseException, DateNotValidException, BillNotFoundException {

        isValid(date);

        readFile();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date1 = dateFormat.parse(date);

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("BILL.txt"))) {

            int incomes = 0;
            String line;


            while ((line = bufferedReader.readLine()) != null) {

                String[] parts = line.split(",");

                if (parts.length == 5) {
                    Date date2 = dateFormat.parse(parts[0]);

                     if (date1.compareTo(date2) == 0) {

                            if (!line.trim().isEmpty()) {
                                incomes += Integer.parseInt(parts[4]);
                            }
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

    public String monthlyincomes(String startdate, String enddate) throws IOException,
            ParseException, DateNotValidException, BillNotFoundException {

        isValid(startdate);
        isValid(enddate);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        Date start = dateFormat.parse(startdate);
        Date end = dateFormat.parse(enddate);



        if (start.compareTo(end) > 0) {
            throw new DateNotValidException("Starting date can not be after end date");
        }

        readFile();



        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("BILL.txt"))) {

            int incomes = 0;
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



            return String.valueOf(incomes);

        } catch (FileNotFoundException e) {
            // Handle the case where the file is not found (empty file)
            return "0";
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



    public double getTotalCostAdmin(String startdate, String enddate) throws DateNotValidException, ParseException, BookNotFoundException, IOException, ClassNotFoundException {

        isValid(startdate);
        isValid(enddate);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");


        Date start = dateFormat.parse(startdate);
        Date end = dateFormat.parse(enddate);


        if (start.compareTo(end) > 0) {
            throw new DateNotValidException("Starting date can not be after end date");
        }

        super.Readbooks();

        double totalcost = 0;


        for (Book book : super.getBooks()) {
            totalcost += book.getPurchased_price() * book.getStock();
        }

        return totalcost;

    }

    public double totalIncomesAdmin(String startdate, String enddate) throws IOException, BookNotFoundException, DateNotValidException, ParseException, BillNotFoundException {

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
            throw new BookNotFoundException("No books are sold from" + " " + startdate + " " + "to" + " " + enddate);
        }

        return totalincomes;

    }



    public double profit(String startdate, String enddate) throws DateNotValidException, BookNotFoundException, ParseException, IOException, ClassNotFoundException, BillNotFoundException {


        double profit =  totalIncomesAdmin(startdate,enddate) - getTotalCostAdmin(startdate,enddate);

        return profit;

    }


}






















