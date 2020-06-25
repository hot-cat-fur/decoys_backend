package pesko.orgasms.app.domain.models.binding;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UserBindingModel {

    @NotNull
    @Length(min = 6)
    private String username;

    @Length(min = 6)
    private String password;
    @Length(min = 6)
    private String repeatPassword;

}
