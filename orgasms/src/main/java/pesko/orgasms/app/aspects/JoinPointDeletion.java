package pesko.orgasms.app.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pesko.orgasms.app.domain.models.service.OrgasmServiceModel;
import pesko.orgasms.app.domain.models.service.UserServiceModel;
import pesko.orgasms.app.events.EventPublisher;

@Aspect
@Component
public class JoinPointDeletion {


    private final EventPublisher eventPublisher;

    @Autowired
    public JoinPointDeletion(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }


    @AfterReturning(value = "execution(* pesko.orgasms.app.service.impl.OrgasmServiceImpl.deleteOrgasm(..)) ||" +
            " execution(* pesko.orgasms.app.service.impl.OrgasmServiceImpl.deleteOwnOrgasm(..)))")
    public void afterOrgasmIsDeletedFromTheDB(JoinPoint jp){

      String title= (String) jp.getArgs()[0];
      eventPublisher.publishDeleteEvent(title);
    }

    @AfterReturning(value = "execution(* pesko.orgasms.app.service.impl.UserServiceImpl.deleteUserByUsername(..)) ",returning = "result")
    public void afterUserIsDeleted(JoinPoint jp, UserServiceModel result){

        result.getOrgasms().forEach(e->{
            eventPublisher.publishDeleteEvent(e.getTitle());
        });


    }
}
