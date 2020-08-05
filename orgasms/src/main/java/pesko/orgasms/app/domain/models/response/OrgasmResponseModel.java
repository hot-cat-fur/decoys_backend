package pesko.orgasms.app.domain.models.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrgasmResponseModel {

    private Long id;

    private String title;

    private String videoUrl;

    private boolean pending;

    private UserDonateResponseModel user;

}
