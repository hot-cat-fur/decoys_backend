package pesko.orgasms.app.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pesko.orgasms.app.domain.models.error.ErrorInfo;

import javax.servlet.http.HttpServletRequest;


@ControllerAdvice
public class GlobalExceptionHandler {


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Throwable.class})
    public @ResponseBody ErrorInfo handleRestError(HttpServletRequest request,Exception e){


        e.printStackTrace();
        return  new ErrorInfo(request.getRequestURI(),e);
    }
}
