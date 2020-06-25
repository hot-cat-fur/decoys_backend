package pesko.orgasms.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import pesko.orgasms.app.domain.entities.Role;
import pesko.orgasms.app.domain.models.service.RoleServiceModel;
import pesko.orgasms.app.repository.RoleRepository;
import pesko.orgasms.app.service.RoleServiceImpl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class RoleServiceTest {

    RoleRepository roleRepository;
    ModelMapper modelMapper;
    RoleServiceImpl roleService;
    @Before
    public void init(){
       roleRepository= Mockito.mock(RoleRepository.class);
        modelMapper=new ModelMapper();
        Role role=new Role();
        role.setAuthority("ROLE_USER");
        when(roleRepository.findByAuthority(any())).thenReturn(role);
        roleService=new RoleServiceImpl(roleRepository,modelMapper);
    }

    @Test
    public void getRole_whenCorrect_ShouldReturnRole(){

        RoleServiceModel roleServiceModel=roleService.getRole("test");

        Assert.assertEquals(roleServiceModel.getAuthority(),"ROLE_USER");
    }


}
