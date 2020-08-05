package pesko.orgasms.app.web.controller;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pesko.orgasms.app.domain.models.binding.UserDonateBindingModel;
import pesko.orgasms.app.domain.models.error.ErrorInfo;
import pesko.orgasms.app.domain.models.info.InfoModel;
import pesko.orgasms.app.domain.models.response.UserDonateResponseModel;
import pesko.orgasms.app.domain.models.service.UserServiceModel;
import pesko.orgasms.app.domain.models.binding.UserBindingModel;
import pesko.orgasms.app.exceptions.InvalidUserException;
import pesko.orgasms.app.exceptions.UserAlreadyExistException;
import pesko.orgasms.app.service.UserService;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;

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
    public ResponseEntity<InfoModel> register(@Valid @RequestBody UserBindingModel userBindingModel, BindingResult bindingResult) {



        if(bindingResult.hasErrors() || !userBindingModel.getPassword().equals(userBindingModel.getRepeatPassword())){
            throw new InvalidUserException("Hacker Attack Detected Calling FBI");
        }


        UserServiceModel userServiceModel1 = this.modelMapper.map(userBindingModel, UserServiceModel.class);
         this.modelMapper.map(this.userService.registerUser(userServiceModel1), UserBindingModel.class);

         return ResponseEntity.status(201).body(new InfoModel("Created"));
    }

    @GetMapping("/find/user/donate")
    public ResponseEntity<UserDonateResponseModel> findDonateProps(Principal principal){

        UserDonateResponseModel userInfo = this.modelMapper.map(this.userService.findByUsername(principal.getName()),UserDonateResponseModel.class);

        return ResponseEntity.ok().body(userInfo);
    }

    @PutMapping(value = "/update/donate",consumes = "application/json")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<InfoModel> putBitcoinAddress(@RequestBody UserDonateBindingModel userDonateBindingModel,Principal principal){

        if(userDonateBindingModel.getPatreonLink().startsWith("https://www.patreon.com") || userDonateBindingModel.getPatreonLink().trim().isBlank()){
            UserServiceModel userServiceModel=this.modelMapper.map(userDonateBindingModel,UserServiceModel.class);
            userServiceModel.setUsername(principal.getName());
            this.userService.updateDonateProps(userServiceModel);
        }else {
            throw new IllegalArgumentException("");
        }

        return ResponseEntity.ok().body(new InfoModel("UPDATED"));
    }





    @ExceptionHandler({InvalidUserException.class})
    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    public ErrorInfo userError(HttpServletRequest request, InvalidUserException e) {

//        e.printStackTrace();
        return new ErrorInfo(request.getRequestURI(), e);
    }

    @ExceptionHandler({UserAlreadyExistException.class})
    @ResponseStatus(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS)
    public ErrorInfo userAlreadyExists(HttpServletRequest request,UserAlreadyExistException e){
//        e.printStackTrace();
        return new ErrorInfo(request.getRequestURI(),e);
    }
}
