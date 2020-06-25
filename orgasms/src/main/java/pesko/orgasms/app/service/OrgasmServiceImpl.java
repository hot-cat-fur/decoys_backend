package pesko.orgasms.app.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pesko.orgasms.app.domain.entities.Orgasm;
import pesko.orgasms.app.domain.models.service.OrgasmServiceModel;
import pesko.orgasms.app.exceptions.FakeOrgasmException;
import pesko.orgasms.app.global.GlobalStaticConsts;
import pesko.orgasms.app.repository.OrgasmRepository;
import pesko.orgasms.app.utils.ValidatorUtil;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrgasmServiceImpl implements OrgasmService {

    private final OrgasmRepository orgasmRepository;
    private final ModelMapper modelMapper;
    private final ValidatorUtil validatorUtil;

    @Autowired
    public OrgasmServiceImpl(OrgasmRepository orgasmRepository, ModelMapper modelMapper, ValidatorUtil validatorUtil) {
        this.orgasmRepository = orgasmRepository;
        this.modelMapper = modelMapper;

        this.validatorUtil = validatorUtil;
    }

    @Override
    public OrgasmServiceModel saveOrgasm(OrgasmServiceModel orgasmServiceModel) {

        if (!this.validatorUtil.isValid(orgasmServiceModel)) {
            throw new FakeOrgasmException(GlobalStaticConsts.FAKE_ORGASM);
        }

        Orgasm orgasm = this.modelMapper.map(orgasmServiceModel, Orgasm.class);

        this.orgasmRepository.saveAndFlush(orgasm);

        return orgasmServiceModel;
    }

    @Override
    public List<OrgasmServiceModel> findAll() {

        List<OrgasmServiceModel> orgasmServiceModels =
                this.orgasmRepository.findAll()
                        .stream().map(e -> this.modelMapper.map(e, OrgasmServiceModel.class)).collect(Collectors.toList());

        return orgasmServiceModels;
    }
}
