package pesko.orgasms.app.exceptions;

public class InvalidUserException extends RuntimeException {

   public InvalidUserException(String errorMsg){
        super(errorMsg);
    }
}
