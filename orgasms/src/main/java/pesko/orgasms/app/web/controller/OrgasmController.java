package pesko.orgasms.app.web.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pesko.orgasms.app.domain.models.error.ErrorInfo;
import pesko.orgasms.app.domain.models.service.OrgasmServiceModel;
import pesko.orgasms.app.domain.models.binding.OrgasmBindingModel;
import pesko.orgasms.app.exceptions.FakeOrgasmException;
import pesko.orgasms.app.service.OrgasmService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orgasm")
public class OrgasmController {

    private final OrgasmService orgasmService;
    private final ModelMapper modelMapper;

    @Autowired
    public OrgasmController(OrgasmService orgasmService, ModelMapper modelMapper) {
        this.orgasmService = orgasmService;
        this.modelMapper = modelMapper;
    }


    @PostMapping(path = "/create", produces = "application/json")
    @PreAuthorize("hasRole('ADMIN')")
    public boolean addOrgasm(@Valid @RequestBody OrgasmBindingModel orgasmViewModel, BindingResult bindingResult) {

        if(bindingResult.hasErrors()){
            throw new FakeOrgasmException("You can't trick me MF");
        }
        this.orgasmService.saveOrgasm(this.modelMapper.map(orgasmViewModel, OrgasmServiceModel.class));

        return true;
    }

    @GetMapping(path = "/videos")
    @PreAuthorize("hasRole('USER')")
    public List<OrgasmBindingModel> findAllOrgasms() {
        return this.orgasmService.findAll()
                .stream()
                .map(e -> this.modelMapper.map(e, OrgasmBindingModel.class))
                .collect(Collectors.toList());
    }

    @ExceptionHandler({FakeOrgasmException.class})
    public ErrorInfo orgasmHandlerException(HttpServletRequest request, FakeOrgasmException ex) {

        ex.printStackTrace();
        return new ErrorInfo(request.getRequestURI(), ex);
    }
}
