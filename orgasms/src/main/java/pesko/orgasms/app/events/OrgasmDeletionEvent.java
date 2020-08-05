package pesko.orgasms.app.events;

import org.springframework.context.ApplicationEvent;

public class OrgasmDeletionEvent extends ApplicationEvent {
    private String objectName;
    public OrgasmDeletionEvent(Object source,String objectName) {
        super(source);
        this.objectName=objectName;
    }

    public String getObjectName() {
        return objectName;
    }
}
