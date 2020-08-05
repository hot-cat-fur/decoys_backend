package pesko.orgasms.app.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pesko.orgasms.app.domain.entities.Event;
import pesko.orgasms.app.domain.models.service.EventServiceModel;
import pesko.orgasms.app.exceptions.InvalidEventException;
import pesko.orgasms.app.global.GlobalExceptionMessageConstants;
import pesko.orgasms.app.repository.EventRepository;
import pesko.orgasms.app.service.EventService;
import pesko.orgasms.app.utils.ValidatorUtil;

import java.time.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final ValidatorUtil validatorUtil;
    private final ModelMapper modelMapper;


    @Autowired
    public EventServiceImpl(EventRepository eventRepository, ValidatorUtil validatorUtil, ModelMapper modelMapper) {
        this.eventRepository = eventRepository;
        this.validatorUtil = validatorUtil;
        this.modelMapper = modelMapper;
    }

    @Override
    public EventServiceModel createEvent(EventServiceModel eventServiceModel) {

        if(!validatorUtil.isValid(eventServiceModel)){
            throw new InvalidEventException("Invalid props");
        }

        Event event=this.modelMapper.map(eventServiceModel,Event.class);

        return this.modelMapper.map(this.eventRepository.saveAndFlush(event),EventServiceModel.class);
    }

    @Override
    public List<EventServiceModel> getThisAndNextMonthEvents() {

        LocalDateTime today = LocalDateTime.now();


        Month month=today.getMonth();
        int year=today.getYear();
        LocalDateTime dateTime=LocalDateTime.of(year,month,1,0,0);

        LocalDateTime nextM = dateTime.plusMonths(2);



        List<EventServiceModel>events= this.eventRepository.findAllByDateBetween(dateTime,nextM)
                .stream()
                .map(e->this.modelMapper.map(e,EventServiceModel.class))
                .collect(Collectors.toList());

        return events;
    }



    @Override
    public void deleteEvent(Long eventId) {

        Event event = eventRepository.findById(eventId).orElseThrow(()-> new IllegalArgumentException(GlobalExceptionMessageConstants.INVALID_PROPS));
        this.eventRepository.deleteById(event.getId());
    }
}
