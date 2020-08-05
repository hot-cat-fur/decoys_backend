package pesko.orgasms.app.domain.models.service;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class EmailServiceModel {

    private String from;
    private String subject;

    @Length(min = 2)
    @NotBlank
    private String text;
}
