package pesko.orgasms.app.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pesko.orgasms.app.domain.entities.Role;
import pesko.orgasms.app.domain.entities.Roles;
import pesko.orgasms.app.domain.entities.User;
import pesko.orgasms.app.domain.models.service.RoleServiceModel;
import pesko.orgasms.app.domain.models.service.UserServiceModel;
import pesko.orgasms.app.exceptions.InsideJobExceeption;
import pesko.orgasms.app.exceptions.InvalidUserException;
import pesko.orgasms.app.exceptions.UserAlreadyExistException;
import pesko.orgasms.app.global.GlobalExceptionMessageConstants;
import pesko.orgasms.app.repository.UserRepository;
import pesko.orgasms.app.service.RoleService;
import pesko.orgasms.app.service.UserService;
import pesko.orgasms.app.utils.ValidatorUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static pesko.orgasms.app.global.GlobalExceptionMessageConstants.USER_NOT_FOUND;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final RoleService roleService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ValidatorUtil validator;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, RoleService roleService
            , BCryptPasswordEncoder bCryptPasswordEncoder, ValidatorUtil validator) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.roleService = roleService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.validator = validator;

    }

    @Override
    public UserServiceModel registerUser(UserServiceModel userServiceModel) {

        if (!this.validator.isValid(userServiceModel)) {
            throw new InvalidUserException(GlobalExceptionMessageConstants.INVALID_PROPS);
        }

        User userChecker = this.userRepository.findByUsername(userServiceModel.getUsername()).orElse(null);

        if (userChecker != null) {
            throw new UserAlreadyExistException(GlobalExceptionMessageConstants.USER_ALREADY_EXISTS);
        }

        User user = this.modelMapper.map(userServiceModel, User.class);
        setRoleRegister(user);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        User saved = this.userRepository.save(user);
        return this.modelMapper.map(saved, UserServiceModel.class);


    }

    @Override
    public UserServiceModel deleteUserByUsername(String username) {


        User user = this.userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND));

        this.checkIfRoot(user);

        this.userRepository.deleteById(user.getId());
     return this.modelMapper.map(user,UserServiceModel.class);
    }

    @Override
    public UserServiceModel findByUsername(String username) {
        User user = this.userRepository.findByUsername(username).orElse(null);
     if (user == null) {
            return null;
        }
        return
                this.modelMapper.map(user, UserServiceModel.class);
    }

    @Override
    public UserServiceModel modifyRole(String username, String role) {
        User user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND));

         checkIfRoot(user);
        user.setRoles(new ArrayList<>());
        UserServiceModel userModel = this.modelMapper.map(user, UserServiceModel.class);

        for (Roles r : Roles.values()) {
            Roles desired = Roles.valueOf(String.format("ROLE_%s", role));
            if (r.getNumber() != 4 && desired.getNumber() >= r.getNumber()) {
                RoleServiceModel roleServiceModel = this.roleService.getRole(r.name());
                userModel.getRoles().add(roleServiceModel);
            }
        }
        user = this.modelMapper.map(userModel, User.class);



        return this.modelMapper.map(this.userRepository.saveAndFlush(user), UserServiceModel.class);
    }

    @Override
    public UserServiceModel updateDonateProps(UserServiceModel userServiceModel) {
       User user= this.userRepository.findByUsername(userServiceModel.getUsername()).orElseThrow(()->new UsernameNotFoundException(USER_NOT_FOUND));
       user.setBitcoinAddress(userServiceModel.getBitcoinAddress());
       user.setPatreonLink(userServiceModel.getPatreonLink());
       this.userRepository.saveAndFlush(user);

       return this.modelMapper.map(user,UserServiceModel.class);
    }


    private void setRoleRegister(User user) {

        if (this.userRepository.findAll().size() < 1) {
            this.roleService.initRoles();
            List<Role> roleList = this.roleService.getAllRoles().stream().map(e -> {
                return this.modelMapper.map(e, Role.class);
            }).collect(Collectors.toList());
            user.setRoles(roleList);
        } else {
            Role role = this.modelMapper.map(this.roleService.getRole(Roles.ROLE_USER.name()), Role.class);
            user.getRoles().add(role);
        }
    }

    private void checkIfRoot(User user) {
        user.getRoles().forEach(e -> {
            if (e.getAuthority().equals("ROLE_ROOT")) {
                throw new InsideJobExceeption("Inside Job");
            }
        });
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return this.userRepository.findByUsername(s).orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND));
    }
}