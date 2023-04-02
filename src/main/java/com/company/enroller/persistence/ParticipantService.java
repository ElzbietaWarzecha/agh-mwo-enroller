package com.company.enroller.persistence;

import java.util.Collection;

import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import com.company.enroller.model.Participant;

@Component("participantService")
public class ParticipantService {

	DatabaseConnector connector;

	public ParticipantService() {
		connector = DatabaseConnector.getInstance();
	}

	public Collection<Participant> getAll() {
		String hql = "FROM Participant";
		Query query = connector.getSession().createQuery(hql);
		return query.list();
	}

	public Participant findByLogin(String login) {
		//wersja 1
//		String hql = "FROM Participant where login:=login"; //zostawiam miejsca na wartości a potem je uzupełniam w setParameter
//		Query query = connector.getSession().createQuery(hql);
//		query.setParameter("login", login);
//		return (Participant) query.uniqueResult(); //getSingleResult rzuca wyjątkiem jeśli nie znajdzie obiektu w bazie danych, trzeba by użyć try catch albo jakoś inaczej to złapać

		//wersja 2
		return (Participant) connector.getSession().get(Participant.class, login);
	}

	public Participant addParticipant(Participant participant) {
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().save(participant);
		transaction.commit();
		return participant; //gdyby coś się w trakcie tworzenia zmieniło w obiekcie to zwracamy, żeby potem widzieć tę zmiane w response
	}

	public void deleteParticipant(Participant participant) {
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().delete(participant);
		transaction.commit();
	}

	public void updateParticipantPassword(Participant participant, String newPassword) {
		Transaction transaction = connector.getSession().beginTransaction();
		participant.setPassword(newPassword);
		connector.getSession().update(participant);
		transaction.commit();
	}
}
