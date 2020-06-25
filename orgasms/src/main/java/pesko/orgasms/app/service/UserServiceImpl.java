package pesko.orgasms.app.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pesko.orgasms.app.domain.entities.Role;
import pesko.orgasms.app.domain.entities.Roles;
import pesko.orgasms.app.domain.entities.User;
import pesko.orgasms.app.domain.models.service.UserServiceModel;
import pesko.orgasms.app.exceptions.InvalidUserException;
import pesko.orgasms.app.exceptions.UserAlreadyExistException;
import pesko.orgasms.app.global.GlobalStaticConsts;
import pesko.orgasms.app.repository.UserRepository;
import pesko.orgasms.app.utils.ValidatorUtil;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final RoleService roleService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ValidatorUtil validator;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, RoleService roleService, BCryptPasswordEncoder bCryptPasswordEncoder, ValidatorUtil validator) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.roleService = roleService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.validator = validator;
    }

    @Override
    public UserServiceModel registerUser(UserServiceModel userServiceModel) {

        if(!this.validator.isValid(userServiceModel)){
            throw new InvalidUserException(GlobalStaticConsts.INVALID_PROPS);
        }

       User userChecker= this.userRepository.findByUsername(userServiceModel.getUsername()).orElse(null);

       if(userChecker!=null){
           throw new UserAlreadyExistException(GlobalStaticConsts.USER_ALREADY_EXISTS);
       }

       User user= this.modelMapper.map(userServiceModel,User.class);
       setRoleRegister(user);
       user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));


       User saved=this.userRepository.save(user);

       return this.modelMapper.map(saved, UserServiceModel.class);


    }

    @Override
    public UserServiceModel deleteUser(UserServiceModel userServiceModel) {

        return null;
    }




    private void setRoleRegister(User user){

        if(this.userRepository.findAll().size()<1){
            this.roleService.initRoles();

            List<Role> roleList =this.roleService.getAllRoles().stream().map(e->{
                return this.modelMapper.map(e,Role.class);
            }).collect(Collectors.toList());

            user.setRoles(roleList);

        }else {


            Role role= this.modelMapper.map(this.roleService.getRole(Roles.ROLE_USER.name()),Role.class);
            user.getRoles().add(role);

        }
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return this.userRepository.findByUsername(s).orElseThrow(()->new UsernameNotFoundException(GlobalStaticConsts.USERNAME_NOT_FOUND));
    }
}