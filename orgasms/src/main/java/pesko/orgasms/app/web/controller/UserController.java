package pesko.orgasms.app.web.controller;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pesko.orgasms.app.domain.models.error.ErrorInfo;
import pesko.orgasms.app.domain.models.service.UserServiceModel;
import pesko.orgasms.app.domain.models.binding.UserBindingModel;
import pesko.orgasms.app.exceptions.InvalidUserException;
import pesko.orgasms.app.exceptions.UserAlreadyExistException;
import pesko.orgasms.app.service.UserService;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;


    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;

    }


    @PostMapping(value = "/register", produces = "application/json")
    public UserBindingModel register(@Valid @RequestBody UserBindingModel userBindingModel, BindingResult bindingResult) {

        if(bindingResult.hasErrors() || !userBindingModel.getPassword().equals(userBindingModel.getRepeatPassword())){
            throw new InvalidUserException("Hacker Attack Detected Calling FBI");
        }



        UserServiceModel userServiceModel1 = this.modelMapper.map(userBindingModel, UserServiceModel.class);
         this.modelMapper.map(this.userService.registerUser(userServiceModel1), UserBindingModel.class);

         return userBindingModel;
    }



    @ExceptionHandler({InvalidUserException.class})
    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    public ErrorInfo userError(HttpServletRequest request, InvalidUserException e) {

        e.printStackTrace();
        return new ErrorInfo(request.getRequestURI(), e);
    }

    @ExceptionHandler({UserAlreadyExistException.class})
    @ResponseStatus(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS)
    public ErrorInfo userAlreadyExists(HttpServletRequest request,UserAlreadyExistException e){
        e.printStackTrace();
        return new ErrorInfo(request.getRequestURI(),e);
    }
}
