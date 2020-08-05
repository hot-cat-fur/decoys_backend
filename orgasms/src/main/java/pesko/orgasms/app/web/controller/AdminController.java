package pesko.orgasms.app.web.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pesko.orgasms.app.domain.models.binding.UserSetRoleBindingModel;
import pesko.orgasms.app.domain.models.error.ErrorInfo;
import pesko.orgasms.app.domain.models.info.InfoModel;
import pesko.orgasms.app.domain.models.service.OrgasmServiceModel;
import pesko.orgasms.app.domain.models.service.RoleServiceModel;
import pesko.orgasms.app.domain.models.service.UserServiceModel;
import pesko.orgasms.app.domain.models.response.AdminUrlViewModel;
import pesko.orgasms.app.domain.models.response.OrgasmResponseModel;
import pesko.orgasms.app.domain.models.response.UserInfoResponseModel;
import pesko.orgasms.app.exceptions.FakeOrgasmException;
import pesko.orgasms.app.exceptions.InsideJobExceeption;
import pesko.orgasms.app.service.OrgasmService;
import pesko.orgasms.app.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Value("${cloudinary.video.app}")
    private String url;

    private final UserService userService;
    private final OrgasmService orgasmService;
    private final ModelMapper modelMapper;

    @Autowired
    public AdminController(UserService userService, OrgasmService orgasmService, ModelMapper modelMapper) {
        this.userService = userService;
        this.orgasmService = orgasmService;
        this.modelMapper = modelMapper;
    }

    @GetMapping(value = "/check")
    @PreAuthorize("hasRole('ADMIN')")
    public AdminUrlViewModel areYouAdmin() {

        return new AdminUrlViewModel(url);
    }

    @GetMapping("/find/user/{name}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserInfoResponseModel> findUser(@PathVariable(name = "name") String name) {

        UserServiceModel user = this.userService.findByUsername(name);
        if (user == null) {
            throw new UsernameNotFoundException("User doesn't exist");
        }
        UserInfoResponseModel responseModel = this.modelMapper.map(user, UserInfoResponseModel.class);
        responseModel.setAuthorities(user.getRoles().stream().map(RoleServiceModel::getAuthority).collect(Collectors.toList()));


        return ResponseEntity.ok().body(responseModel);
    }

    @GetMapping("/find/orgasm/{name}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrgasmResponseModel> findOrgasm(@PathVariable(name = "name") String name) {

        OrgasmServiceModel orgasmServiceModel = this.orgasmService.findByTitle(name);

        if (orgasmServiceModel == null) {
            throw new FakeOrgasmException("Orgasm doesn't exist");
        }
      OrgasmResponseModel orgasm= this.modelMapper.map(orgasmServiceModel, OrgasmResponseModel.class);
        return ResponseEntity.ok().body(orgasm);
    }

    @PutMapping("/set-role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserInfoResponseModel> setRoleAuth(@RequestBody UserSetRoleBindingModel userSetRoleBindingModel) {

        if (userSetRoleBindingModel.getRole().equals("ROOT")) {
            throw new IllegalArgumentException("Inside Job");
        }
       UserServiceModel userServiceModel=
               this.userService.modifyRole(userSetRoleBindingModel.getUsername(), userSetRoleBindingModel.getRole());

        UserInfoResponseModel userInfoResponseModel=this.modelMapper.map(userServiceModel,UserInfoResponseModel.class);

        userServiceModel.getRoles().forEach(e->{
            userInfoResponseModel.getAuthorities().add(e.getAuthority());
        });

        return ResponseEntity.ok().body(userInfoResponseModel);
    }

    @DeleteMapping("/delete/user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InfoModel> deleteUser(@RequestParam(value = "name", required = true) String name) {
        this.userService.deleteUserByUsername(name);
        this.orgasmService.removeLikeDislikeByUsername(name);
        return ResponseEntity.ok().body(new InfoModel("deleted"));
    }

    @DeleteMapping("/delete/orgasm")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InfoModel> deleteOrgasm(@RequestParam(value = "name", required = true) String name) {
        this.orgasmService.deleteOrgasm(name);
        return ResponseEntity.ok().body(new InfoModel("deleted"));
    }

    @PutMapping("/modi/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrgasmResponseModel> modifyOrgasm(@RequestParam(value = "title",required = true) String title){

          OrgasmResponseModel model=this.modelMapper.map(this.orgasmService.modifyPending(title), OrgasmResponseModel.class);
        return ResponseEntity.ok().body(model);
    }

    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    @ExceptionHandler(InsideJobExceeption.class)
    public ErrorInfo insideJobHandlerException(HttpServletRequest request,InsideJobExceeption ex){

        return new ErrorInfo(request.getRequestURI(),ex);
    }

    @ExceptionHandler({FakeOrgasmException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorInfo orgasmHandlerException(HttpServletRequest request, FakeOrgasmException ex) {

        return new ErrorInfo(request.getRequestURI(), ex);
    }


}
