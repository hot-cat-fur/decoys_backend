package pesko.orgasms.app.domain.models.service;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class OrgasmServiceModel {


    private Long id;

    @NotNull
    @NotBlank
    private String title;

    @NotNull
    @NotBlank
    private String videoUrl;

    private UserServiceModel user;

    private boolean pending;

    private Map<String,Boolean> likeDislike;


}
