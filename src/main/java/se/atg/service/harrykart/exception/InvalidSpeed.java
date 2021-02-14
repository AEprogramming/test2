package se.atg.service.harrykart.exception;

public class InvalidSpeed extends IllegalArgumentException {

    public InvalidSpeed(String message){
        super(message);
    }
}
