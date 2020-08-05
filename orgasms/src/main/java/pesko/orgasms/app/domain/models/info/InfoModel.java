package pesko.orgasms.app.domain.models.info;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InfoModel {

    private String msg;

    public InfoModel(){

    }
    public InfoModel(String msg){
        this.msg=msg;
    }
}
