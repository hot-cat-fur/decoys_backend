package pesko.orgasms.app.domain.models.binding;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class OrgasmBindingModel {



    @Length(min = 1)
    @NotBlank
    private String title;


    @Length(min = 1)
    @NotBlank
    private String videoUrl;


}
