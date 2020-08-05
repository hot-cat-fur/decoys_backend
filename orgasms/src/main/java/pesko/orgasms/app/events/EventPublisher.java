package pesko.orgasms.app.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class EventPublisher {

    @Autowired
    private ApplicationEventPublisher publisher;

    public void publishDeleteEvent(String objectName){
        OrgasmDeletionEvent orgasmDeletionEvent=new OrgasmDeletionEvent(this,objectName);
        publisher.publishEvent(orgasmDeletionEvent);
    }
}
