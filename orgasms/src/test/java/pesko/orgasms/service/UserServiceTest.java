package pesko.orgasms.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import pesko.orgasms.app.domain.entities.Role;
import pesko.orgasms.app.domain.entities.Roles;
import pesko.orgasms.app.domain.entities.User;
import pesko.orgasms.app.domain.models.service.RoleServiceModel;
import pesko.orgasms.app.domain.models.service.UserServiceModel;
import pesko.orgasms.app.exceptions.InvalidUserException;
import pesko.orgasms.app.repository.UserRepository;
import pesko.orgasms.app.service.RoleService;
import pesko.orgasms.app.service.UserService;
import pesko.orgasms.app.service.UserServiceImpl;
import pesko.orgasms.app.utils.ValidatorUtil;
import pesko.orgasms.app.utils.ValidatorUtilImpl;
import pesko.orgasms.serviceUtils.RoleServiceUtil;
import pesko.orgasms.serviceUtils.UserServiceUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceTest {



     @MockBean
     UserRepository userRepository;



     ModelMapper modelMapper;


     @MockBean
     RoleService roleService;
     List<RoleServiceModel> roles;


     BCryptPasswordEncoder bCryptPasswordEncoder;


     ValidatorUtil validator;
     List<User>users;
     UserServiceImpl userService;



     @Before
    public void init(){
         validator=new ValidatorUtilImpl();
       modelMapper=new ModelMapper();
       bCryptPasswordEncoder=new BCryptPasswordEncoder();
         roles=new ArrayList<>();
         users=new ArrayList<>();




         userService=new UserServiceImpl(userRepository,modelMapper,roleService,bCryptPasswordEncoder,validator);




     }

     @Test
    public void registerUser_whenCorrect_shouldRegister(){
         users.addAll(UserServiceUtil.getUsers(1));
         User user=new User();
         user.setPassword("valid_password");
         user.setUsername("valid_username");
         users.add(0,user);
         users.get(0).setRoles(RoleServiceUtil.getAllRoles());


         when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
         Mockito.when(userRepository.save(any())).thenReturn(users.get(0));
         Mockito.doNothing().when(roleService).initRoles();
         when(roleService.getRole(any())).thenReturn(new RoleServiceModel());

      UserServiceModel saveUser= userService.registerUser(modelMapper.map(users.get(0),UserServiceModel.class));

      Assert.assertEquals(saveUser.getRoles().size(),4);
      Assert.assertEquals(saveUser.getUsername(),"valid_username");
      Assert.assertEquals(saveUser.getPassword(),"valid_password");

     }
    @Test(expected = InvalidUserException.class)
    public void registerUser_whenIncorectPassword_shouldThrowException(){
        users.addAll(UserServiceUtil.getUsers(1));
        User user=new User();
        user.setPassword("inv");
        user.setUsername("valid_username");
        users.add(0,user);
        users.get(0).setRoles(RoleServiceUtil.getAllRoles());


        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        Mockito.when(userRepository.save(any())).thenReturn(users.get(0));
        Mockito.doNothing().when(roleService).initRoles();
        when(roleService.getRole(any())).thenReturn(new RoleServiceModel());

        UserServiceModel saveUser= userService.registerUser(modelMapper.map(users.get(0),UserServiceModel.class));


    }
    @Test(expected = InvalidUserException.class)
    public void registerUser_whenIncorectUsername_shouldThrowException(){
        users.addAll(UserServiceUtil.getUsers(1));
        User user=new User();
        user.setPassword("valid_password");
        user.setUsername("in");
        users.add(0,user);
        users.get(0).setRoles(RoleServiceUtil.getAllRoles());


        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        Mockito.when(userRepository.save(any())).thenReturn(users.get(0));
        Mockito.doNothing().when(roleService).initRoles();
        when(roleService.getRole(any())).thenReturn(new RoleServiceModel());

        UserServiceModel saveUser= userService.registerUser(modelMapper.map(users.get(0),UserServiceModel.class));


    }

    @Test(expected = InvalidUserException.class)
    public void registerUser_userAlreadyIxists_shouldThrowException(){
        users.addAll(UserServiceUtil.getUsers(1));
        User user=new User();
        user.setPassword("valid_password");
        user.setUsername("in");
        users.add(0,user);
        users.get(0).setRoles(RoleServiceUtil.getAllRoles());


        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(users.get(0)));
        Mockito.when(userRepository.save(any())).thenReturn(users.get(0));
        Mockito.doNothing().when(roleService).initRoles();
        when(roleService.getRole(any())).thenReturn(new RoleServiceModel());

        UserServiceModel saveUser= userService.registerUser(modelMapper.map(users.get(0),UserServiceModel.class));


    }
//      @Test
//    public void setRole_shouldSetAllRoles_whenDbEmpty(){
//
//         List<User>users=new ArrayList<>();
//         List<RoleServiceModel>roleServices=new ArrayList<>();
//        roleServices.addAll(Arrays.stream(Roles.values()).map(e->{
//            RoleServiceModel roleServiceModel=new RoleServiceModel();
//            roleServiceModel.setAuthority(e.name());
//            return roleServiceModel;
//        }).collect(Collectors.toList()));
//         when(userRepository.findAll()).thenReturn(users);
//        Mockito.doNothing().when(roleService).initRoles();
//        when(roleService.getAllRoles()).thenReturn(roleServices);
//
//        User user=new User();
//        userService.setRoleRegister(user);
//
//        Assert.assertEquals(user.getRoles().size(),4);
//        //    ROLE_GUEST,ROLE_USER,ROLE_ADMIN,ROLE_ROOT
//        Assert.assertEquals(user.getRoles().get(0).getAuthority(),"ROLE_GUEST");
//          Assert.assertEquals(user.getRoles().get(1).getAuthority(),"ROLE_USER");
//          Assert.assertEquals(user.getRoles().get(2).getAuthority(),"ROLE_ADMIN");
//          Assert.assertEquals(user.getRoles().get(3).getAuthority(),"ROLE_ROOT");
//
//    }
//    @Test
//    public void setRole_shouldSetUserRole_whenDbEmpty(){
//
//        List<User>users=new ArrayList<>();
//        users.add(new User());
//        List<RoleServiceModel>roleServices=new ArrayList<>();
//        roleServices.addAll(Arrays.stream(Roles.values()).map(e->{
//            RoleServiceModel roleServiceModel=new RoleServiceModel();
//            roleServiceModel.setAuthority(e.name());
//            return roleServiceModel;
//        }).collect(Collectors.toList()));
//        when(userRepository.findAll()).thenReturn(users);
//        Mockito.doNothing().when(roleService).initRoles();
//        RoleServiceModel role=new RoleServiceModel();
//        role.setAuthority("ROLE_USER");
//        when(roleService.getRole(any())).thenReturn(role);
//
//        User user=new User();
//        userService.setRoleRegister(user);
//
//        Assert.assertEquals(user.getRoles().size(),1);
//        //    ROLE_GUEST,ROLE_USER,ROLE_ADMIN,ROLE_ROOT
//
//        Assert.assertEquals(user.getRoles().get(0).getAuthority(),"ROLE_USER");
//
//
//    }


}
