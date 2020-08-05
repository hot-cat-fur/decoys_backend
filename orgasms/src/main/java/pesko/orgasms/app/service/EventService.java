package pesko.orgasms.app.service;

import pesko.orgasms.app.domain.models.service.EventServiceModel;

import java.util.List;

public interface EventService {


     EventServiceModel createEvent(EventServiceModel eventServiceModel);
     List<EventServiceModel>getThisAndNextMonthEvents();


     void deleteEvent(Long eventId);

}
