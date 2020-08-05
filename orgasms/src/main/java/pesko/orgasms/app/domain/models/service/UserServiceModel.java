package pesko.orgasms.app.domain.models.service;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;


public class UserServiceModel {

    private Long id;
    @NotNull
    @NotBlank
    @Length(min = 3,max = 20)
    private String username;
    @Length(min = 6)
    @NotNull
    @NotBlank
    private String password;

    private String bitcoinAddress;

    private String patreonLink;

    private List<OrgasmServiceModel> orgasms;

    private List<RoleServiceModel>roles;



    public UserServiceModel(){
        this.roles=new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<OrgasmServiceModel> getOrgasms() {
        return orgasms;
    }

    public void setOrgasms(List<OrgasmServiceModel> orgasms) {
        this.orgasms = orgasms;
    }

    public List<RoleServiceModel> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleServiceModel> roles) {
        this.roles = roles;
    }

    public String getBitcoinAddress() {
        return bitcoinAddress;
    }

    public void setBitcoinAddress(String bitcoinAddress) {
        this.bitcoinAddress = bitcoinAddress;
    }

    public String getPatreonLink() {
        return patreonLink;
    }

    public void setPatreonLink(String patreonLink) {
        this.patreonLink = patreonLink;
    }
}

