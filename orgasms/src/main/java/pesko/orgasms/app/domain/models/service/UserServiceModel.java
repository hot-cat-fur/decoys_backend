package pesko.orgasms.app.domain.models.service;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserServiceModel {

    private Integer id;
    @NotNull
    @NotBlank
    @Length(min = 3,max = 20)
    private String username;
    @Length(min = 6)
    @NotNull
    @NotBlank
    private String password;
    private List<OrgasmServiceModel> orgasms;

    private List<RoleServiceModel>roles;

    public UserServiceModel(){
        this.roles=new ArrayList<>();
    }

}

