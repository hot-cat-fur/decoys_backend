package pesko.orgasms.service;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import pesko.orgasms.app.domain.entities.Orgasm;
import pesko.orgasms.app.domain.models.service.OrgasmServiceModel;
import pesko.orgasms.app.exceptions.FakeOrgasmException;
import pesko.orgasms.app.repository.OrgasmRepository;
import pesko.orgasms.app.service.OrgasmServiceImpl;
import pesko.orgasms.app.utils.ValidatorUtil;
import pesko.orgasms.app.utils.ValidatorUtilImpl;
import pesko.orgasms.serviceUtils.OrgasmServiceUtil;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class OrgasmServiceTest {

    OrgasmRepository orgasmRepository;
    List<Orgasm> orgasmsList;
    ModelMapper modelMapper;
    ValidatorUtil validatorUtil;
    OrgasmServiceImpl orgasmService;

    @Before
    public void setUp() {
        modelMapper = new ModelMapper();
        validatorUtil = new ValidatorUtilImpl();
        orgasmsList = new ArrayList<>();
        orgasmRepository = Mockito.mock(OrgasmRepository.class);
        when(orgasmRepository.findAll()).thenReturn(orgasmsList);

        orgasmService = new OrgasmServiceImpl(orgasmRepository, modelMapper, validatorUtil);

    }

    @Test
    public void findAll_whenTwoOrgasms_expectTwo() {
        orgasmsList.addAll(OrgasmServiceUtil.createOrgasm(2));

        List<OrgasmServiceModel> orgasmServiceModels = orgasmService.findAll();

        assertThat(orgasmServiceModels.size(), is(2));

    }

    @Test
    public void findAll_whenZeroOrgasms_expectToBeEmpty() {


        List<OrgasmServiceModel> orgasmServiceModels = orgasmService.findAll();

        assertThat(orgasmServiceModels.size(), is(0));

    }

    @Test
    public void saveOrgasm_whenValid_shouldReturnOrgasmServiceModel() {

        OrgasmServiceModel orgasm = new OrgasmServiceModel();
        orgasm.setImgUrl("valid");
        orgasm.setTitle("valid");
        orgasm.setVideoUrl("valid");
        OrgasmServiceModel savedOrgasm = orgasmService.saveOrgasm(orgasm);

        Assert.assertEquals(orgasm, savedOrgasm);

    }

    @Test(expected = FakeOrgasmException.class)
    public void saveOrgasm_whenInvalid_shouldThrowIlligalArgumentException() {

        OrgasmServiceModel orgasm = new OrgasmServiceModel();

        orgasmService.saveOrgasm(orgasm);

    }

}
