package pesko.orgasms.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.BindingResult;
import pesko.orgasms.app.domain.entities.User;
import pesko.orgasms.app.domain.models.binding.UserBindingModel;
import pesko.orgasms.app.exceptions.InvalidUserException;
import pesko.orgasms.app.repository.UserRepository;
import pesko.orgasms.app.service.RoleService;
import pesko.orgasms.app.service.UserService;
import pesko.orgasms.app.web.controller.UserController;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class UserControllerTest {


    @Autowired
    UserService userService;

    @Autowired
    ModelMapper modelMapper;

    @MockBean
    BindingResult bindingResult;

    @MockBean
    UserRepository userRepository;

    @Autowired
    UserController userController;

    @Autowired
    RoleService roleRepository;

    List<User> users;

    User user;

    @Before
    public void init(){
       users=new ArrayList<>();
       user=new User();
        user.setUsername("valid_username");
        user.setPassword("valid_password");
       userController=new UserController(userService,modelMapper);

       when(userRepository.findAll()).thenReturn(users);
       when(userRepository.save(any())).thenReturn(user);
       when(bindingResult.hasErrors()).thenReturn(true);
    }

    @Test
    public void register_whenCorrect_UserViewModel(){

        when(bindingResult.hasErrors()).thenReturn(false);
        UserBindingModel userViewModel=new UserBindingModel();
        userViewModel.setUsername("valid_username");
        userViewModel.setPassword("valid_password");
        userViewModel.setRepeatPassword("valid_password");

       UserBindingModel saved= userController.register(userViewModel,bindingResult);

        Assert.assertEquals(saved.getUsername(),"valid_username");
        Assert.assertEquals(saved.getPassword(),"valid_password");
        Assert.assertEquals(saved.getRepeatPassword(),null);
    }

    @Test(expected = InvalidUserException.class)
    public void register_whenIncorectUsername_shouldThrowException(){


        UserBindingModel userViewModel=new UserBindingModel();
        userViewModel.setUsername("va");
        userViewModel.setPassword("valid_password");
        userViewModel.setRepeatPassword("valid_password");

      userController.register(userViewModel,bindingResult);

    }
    @Test(expected = InvalidUserException.class)
    public void register_whenIncorectPassword_shouldThrowException(){

        UserBindingModel userViewModel=new UserBindingModel();
        userViewModel.setUsername("val");
        userViewModel.setPassword("inval");
        userViewModel.setRepeatPassword("inval");

        userController.register(userViewModel,bindingResult);

    }
    @Test(expected = InvalidUserException.class)
    public void register_whenPasswordsNotMatching_shouldThrowException(){

        UserBindingModel userViewModel=new UserBindingModel();
        userViewModel.setUsername("valid");
        userViewModel.setPassword("valid_password");
        userViewModel.setRepeatPassword("password_valid");

        userController.register(userViewModel,bindingResult);

    }
}
