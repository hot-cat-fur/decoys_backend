package pesko.orgasms.app.exceptions;

public class UserAlreadyExistException extends RuntimeException {

  public  UserAlreadyExistException(String errMsg){
        super(errMsg);
    }
}
