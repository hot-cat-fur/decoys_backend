package pesko.orgasms.app.exceptions;

public class InvalidEmailException extends RuntimeException {




    public InvalidEmailException(String errorMsg){
        super(errorMsg);
    }
}
