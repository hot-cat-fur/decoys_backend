package pesko.orgasms.serviceUtils;

import pesko.orgasms.app.domain.entities.Role;
import pesko.orgasms.app.domain.entities.Roles;
import pesko.orgasms.app.domain.models.service.RoleServiceModel;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RoleServiceUtil {

    public static List<Role> getAllRoles(){

        return Arrays.stream(Roles.values())
                .map(e->new Role(){{setAuthority(e.name());}}).collect(Collectors.toList());
    }
}
