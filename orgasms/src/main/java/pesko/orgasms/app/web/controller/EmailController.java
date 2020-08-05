package pesko.orgasms.app.web.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pesko.orgasms.app.domain.models.binding.EmailBindingModel;
import pesko.orgasms.app.domain.models.error.ErrorInfo;
import pesko.orgasms.app.domain.models.info.InfoModel;
import pesko.orgasms.app.domain.models.service.EmailServiceModel;
import pesko.orgasms.app.exceptions.InvalidEmailException;
import pesko.orgasms.app.global.GlobalExceptionMessageConstants;
import pesko.orgasms.app.service.EmailService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/mail")
public class EmailController {

    private final EmailService emailService;
    private final ModelMapper modelMapper;

    @Autowired
    public EmailController(EmailService emailService, ModelMapper modelMapper) {
        this.emailService = emailService;
        this.modelMapper = modelMapper;
    }


    @PostMapping("/send")
    public ResponseEntity<InfoModel> sendEmail(@Valid @RequestBody  EmailBindingModel emailBindingModel,BindingResult bindingResult){

        if(bindingResult.hasErrors()){
          throw new InvalidEmailException(GlobalExceptionMessageConstants.INVALID_EMAIL_REQUEST);
        }

        emailService.sendSimpleMessage(this.modelMapper.map(emailBindingModel, EmailServiceModel.class));


        return ResponseEntity.ok().body(new InfoModel("Email received"));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidEmailException.class)
    public ErrorInfo invalidEmail(HttpServletRequest request,InvalidEmailException ex){

        return new ErrorInfo(request.getRequestURI(),ex);
    }
}
