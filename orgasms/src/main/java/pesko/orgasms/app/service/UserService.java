package pesko.orgasms.app.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import pesko.orgasms.app.domain.entities.User;
import pesko.orgasms.app.domain.models.service.UserServiceModel;

public interface UserService extends UserDetailsService {

    UserServiceModel registerUser(UserServiceModel userServiceModel);
    UserServiceModel deleteUser(UserServiceModel userServiceModel);



}
