package pesko.orgasms.app.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import pesko.orgasms.app.domain.entities.User;
import pesko.orgasms.app.domain.models.service.UserServiceModel;

public interface UserService extends UserDetailsService {

    UserServiceModel registerUser(UserServiceModel userServiceModel);
    UserServiceModel deleteUserByUsername(String username);
    UserServiceModel findByUsername(String username);
    UserServiceModel modifyRole(String username, String role);
    UserServiceModel updateDonateProps(UserServiceModel userServiceModel);


}
