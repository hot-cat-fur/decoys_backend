package pesko.orgasms.app.domain.models.binding;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class OrgasmBindingModel {



    @Length(min = 1)
    private String title;

    private String content;


    @Length(min = 1)
    private String imgUrl;


    @Length(min = 1)
    private String videoUrl;



}
