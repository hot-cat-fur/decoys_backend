package pesko.orgasms.app.service;

import pesko.orgasms.app.domain.models.service.EmailServiceModel;

public interface EmailService {

    void sendSimpleMessage(EmailServiceModel model);
}
