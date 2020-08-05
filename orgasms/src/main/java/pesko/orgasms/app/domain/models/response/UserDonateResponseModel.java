package pesko.orgasms.app.domain.models.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDonateResponseModel {

    private String bitcoinAddress;
    private String patreonLink;
}
