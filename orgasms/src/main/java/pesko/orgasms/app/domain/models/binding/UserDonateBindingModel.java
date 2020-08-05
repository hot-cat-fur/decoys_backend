package pesko.orgasms.app.domain.models.binding;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDonateBindingModel {

    private String bitcoinAddress;
    private String patreonLink;
}
