package com.example.clothshop.repository;

import com.example.clothshop.entity.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PersonRepositoryTests {

    @Autowired
    private PersonRepository personRepository;

    private Person person;

    @BeforeEach
    public void setup() {

        person = Person.builder()
                .orders(new ArrayList<>())
                .rolesList(new ArrayList<>())
                .username("PersonTest")
                .firstname("FirstNameTest")
                .lastname("LastNameTest")
                .email("EmailTest")
                .password("PasswordTest")
                .phone("1-000-0101")
                .build();
    }

    @Test
    public void givenPersonObject_whenSaveThenReturnSavedPerson() {
        Person savedPerson = personRepository.save(person);

        assertThat(savedPerson).isNotNull();
        assertThat(savedPerson.getId()).isGreaterThan(0);
    }

    @Test
    public void givenPersonList_whenFindAll_thenPersonList() {
        int personSize = (int) personRepository.count();
        List<Person> personList = personRepository.findAll();

        assertThat(personList).isNotNull();
        assertThat(personList.size()).isEqualTo(personSize);
    }

    @Test
    public void givenPersonObject_whenFindById_thenReturnPersonObject() {
        Person savedPerson = personRepository.save(person);

        Person personDB = personRepository.findById(person.getId()).get();

        assertThat(personDB).isNotNull();
    }

    @Test
    public void givenPersonName_whenFindByName_thenReturnPersonObject() {
        Person savedPerson = personRepository.save(person);

        Person personDB = personRepository.findByUsername(person.getUsername()).get();

        assertThat(personDB).isNotNull();
    }

    @Test
    public void givenPersonObject_whenUpdatePerson_thenReturnUpdatedPerson() {
        personRepository.save(person);

        Person savedPerson = personRepository.findById(person.getId()).get();
        savedPerson.setUsername("PersonTestUpdate");
        Person updatedPerson = personRepository.save(savedPerson);

        assertThat(updatedPerson.getUsername()).isEqualTo("PersonTestUpdate");
    }

    @Test
    public void givenPersonObject_whenDeletePerson_thenRemovePerson() {
        personRepository.save(person);

        personRepository.delete(person);
        Optional<Person> personOptional = personRepository.findById(person.getId());

        assertThat(personOptional).isEmpty();
    }
}
