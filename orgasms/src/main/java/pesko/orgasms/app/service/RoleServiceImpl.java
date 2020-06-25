package pesko.orgasms.app.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pesko.orgasms.app.domain.entities.Role;
import pesko.orgasms.app.domain.entities.Roles;
import pesko.orgasms.app.domain.models.service.RoleServiceModel;
import pesko.orgasms.app.repository.RoleRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository, ModelMapper modelMapper) {
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void initRoles() {
        if(this.roleRepository.findAll().size()<1){
            List<Role>roles= Arrays.stream(Roles.values()).map(e->new Role(e.name())).collect(Collectors.toList());
            this.roleRepository.saveAll(roles);
        }
    }

    @Override
    public List<RoleServiceModel> getAllRoles() {
       List<RoleServiceModel>roles=this.roleRepository.findAll().stream().map(e->{
        return  this.modelMapper.map(e,RoleServiceModel.class);
       }).collect(Collectors.toList());

        return roles;
    }

    @Override
    public RoleServiceModel getRole(String role) {

        return this.modelMapper.map(this.roleRepository.findByAuthority(role),RoleServiceModel.class);
    }
}
