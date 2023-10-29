package br.com.example.estudocamel.api.service;

import br.com.example.estudocamel.context.domain.Person;
import br.com.example.estudocamel.context.route.PersonRoute;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {

    @Autowired
    private ProducerTemplate template;

    public List<Person> getPerson() {
        return template.requestBody(PersonRoute.FIND_PERSONS, null, List.class);
    }

    public Person getPersonById(Long id) {
        return template.requestBody(PersonRoute.FIND_PERSONS_BY_ID, id, Person.class);
    }

    public Person createPerson(Person person) {
        return template.requestBody(PersonRoute.CREATE_PERSON, person, Person.class);
    }
}
