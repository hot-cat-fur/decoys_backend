package pesko.orgasms.app.service;

import pesko.orgasms.app.domain.models.service.RoleServiceModel;

import java.util.List;

public interface RoleService {

    void initRoles();
    List<RoleServiceModel>getAllRoles();
    RoleServiceModel getRole(String role);
}
