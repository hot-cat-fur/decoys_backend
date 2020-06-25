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
import pesko.orgasms.app.domain.entities.Orgasm;
import pesko.orgasms.app.domain.models.binding.OrgasmBindingModel;
import pesko.orgasms.app.exceptions.FakeOrgasmException;
import pesko.orgasms.app.repository.OrgasmRepository;
import pesko.orgasms.app.service.OrgasmService;
import pesko.orgasms.app.web.controller.OrgasmController;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class OrgasmControllerTest {

    OrgasmController orgasmController;

    @MockBean
    OrgasmRepository orgasmRepository;

    @MockBean
    BindingResult bindingResult;

    @Autowired
    OrgasmService orgasmService;
    @Autowired
    ModelMapper modelMapper;
    OrgasmBindingModel orgasm=new OrgasmBindingModel();
    @Before
    public void init(){
        when(orgasmRepository.save(any())).thenReturn(new Orgasm());
        when(bindingResult.hasErrors()).thenReturn(true);
        orgasmController=new OrgasmController(orgasmService,modelMapper);

    }

    @Test
    public void addOrgasm_whenValid_ShouldReturnTrue(){
        when(bindingResult.hasErrors()).thenReturn(false);
        orgasm.setVideoUrl("valid_url");
        orgasm.setTitle("valid_title");
        orgasm.setImgUrl("valid_title");
        boolean result=orgasmController.addOrgasm(orgasm,bindingResult);

        Assert.assertTrue(result);
    }
    @Test(expected = FakeOrgasmException.class)
    public void addOrgasm_whenInvalidVideoUrl_ShouldThrowException(){
        orgasm.setVideoUrl("     ");
        orgasm.setTitle("valid_title");
        orgasm.setImgUrl("valid_title");
      orgasmController.addOrgasm(orgasm,bindingResult);


    }
    @Test(expected = FakeOrgasmException.class)
    public void addOrgasm_whenInvalidImgUrl_ShouldThrowException(){
        orgasm.setVideoUrl("valid_url");
        orgasm.setTitle("valid_title");
        orgasm.setImgUrl("      ");
     orgasmController.addOrgasm(orgasm,bindingResult);


    }
    @Test(expected = FakeOrgasmException.class)
    public void addOrgasm_whenInvalidTitle_ShouldThrowException(){
        orgasm.setVideoUrl("valid");
        orgasm.setTitle("     ");
        orgasm.setImgUrl("valid_title");
      orgasmController.addOrgasm(orgasm,bindingResult);


    }
}
