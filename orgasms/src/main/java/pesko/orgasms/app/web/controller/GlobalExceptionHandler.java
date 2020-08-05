package pesko.orgasms.app.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import pesko.orgasms.app.domain.models.error.ErrorInfo;
import pesko.orgasms.app.exceptions.FakeOrgasmException;

import javax.servlet.http.HttpServletRequest;


@ControllerAdvice
public class GlobalExceptionHandler {


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Throwable.class})
    public @ResponseBody ErrorInfo handleRestError(HttpServletRequest request,Exception e){


        e.printStackTrace();
        return  new ErrorInfo(request.getRequestURI(),e);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({UsernameNotFoundException.class})
    public @ResponseBody ErrorInfo handleUsernameNotFoundException(HttpServletRequest request,Exception e){


        return new ErrorInfo(request.getRequestURI(),e);
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({FakeOrgasmException.class})
    public ErrorInfo orgasmHandlerException(HttpServletRequest request, FakeOrgasmException ex) {

//        ex.printStackTrace();
        return new ErrorInfo(request.getRequestURI(), ex);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MultipartException.class})
    public ErrorInfo multipartfileExceptionHandler(HttpServletRequest request,MultipartException ex){

        return new ErrorInfo(request.getRequestURI(),ex);
    }
}
