package com.company.enroller.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.company.enroller.model.Participant;
import com.company.enroller.persistence.ParticipantService;

@RestController
@RequestMapping("/participants")
public class ParticipantRestController {

    @Autowired
    ParticipantService participantService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getParticipants() {
        Collection<Participant> participants = participantService.getAll();
        return new ResponseEntity<Collection<Participant>>(participants, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getParticipant(@PathVariable("id") String login) {
        Participant participant = participantService.findByLogin(login);
        if (participant == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Participant>(participant, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> registerParticipant(@RequestBody Participant participant) {
        //sprawdzamy czy użytkownik istnieje, jeśli nie to dodajemy, jeśli tak to wyrzucamy jakiś błąd
        if (participantService.findByLogin(participant.getLogin()) != null) {
            return new ResponseEntity("Unable to create. A participant with login " + participant.getLogin() + " already exist.", HttpStatus.CONFLICT);
        } else {
            participantService.addParticipant(participant);
            return new ResponseEntity<>(participant, HttpStatus.CREATED);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteParticipant(@PathVariable("id") String login) {
        //sprawdzamy czy użytkownik istnieje, jeśli nie to dodajemy, jeśli tak to wyrzucamy jakiś błąd
        Participant participant = participantService.findByLogin(login);
        if (participant == null) {
            return new ResponseEntity("Unable to remove. A participant with login " + login + " dose not exist.", HttpStatus.NOT_FOUND);
        } else {
            participantService.deleteParticipant(participant);
            return new ResponseEntity<>("", HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateParticipantPassword(@PathVariable("id") String login, @RequestBody Participant participant) {
        Participant participantByLogin = participantService.findByLogin(login);
        if (participantByLogin == null) {
            return new ResponseEntity("Unable to update. A participant with login " + login + " dose not exist.", HttpStatus.NOT_FOUND);
        } else {
            participantService.updateParticipantPassword(participantByLogin, participant.getPassword());
            return new ResponseEntity<>("", HttpStatus.OK);
        }
    }


}
