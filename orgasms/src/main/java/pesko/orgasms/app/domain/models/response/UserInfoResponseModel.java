package pesko.orgasms.app.domain.models.response;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserInfoResponseModel {

    private String username;
    private Long id;
    private List<String> authorities;
    private List<OrgasmResponseModel> orgasms;

    private String bitcoinAddress;
    private String patreonLink;

    public UserInfoResponseModel(){

        this.setAuthorities(new ArrayList<>());
    }
}
