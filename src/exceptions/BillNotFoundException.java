package exceptions;


public class BillNotFoundException extends Exception{

    public BillNotFoundException(){
        super("No bills were committed by this user");
    }
    public BillNotFoundException(String message){
        super(message);
    }
}
