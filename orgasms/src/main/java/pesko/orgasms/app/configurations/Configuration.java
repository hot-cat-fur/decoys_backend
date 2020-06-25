package pesko.orgasms.app.configurations;


import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pesko.orgasms.app.utils.ValidatorUtil;
import pesko.orgasms.app.utils.ValidatorUtilImpl;

@org.springframework.context.annotation.Configuration
public class Configuration {


    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Bean
   public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
   public ValidatorUtil validatorUtil(){
        return new ValidatorUtilImpl();
    }

}
