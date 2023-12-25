package com.example.clothshop.service;

import com.example.clothshop.dto.MapStructMapper;
import com.example.clothshop.dto.PersonUpdateDTO;
import com.example.clothshop.entity.Person;
import com.example.clothshop.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTests {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private MapStructMapper mapStructMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PersonService personService;

    private Person person;

    private PersonUpdateDTO personUpdateDTO;

    @BeforeEach
    public void setup() {
        person = Person.builder()
                .id(1L)
                .orders(null)
                .rolesList(null)
                .username("UsernameTest")
                .firstname("FirstNameTest")
                .lastname("LastNameTest")
                .email("person@gmail.com")
                .password("passwordTest")
                .phone("1-22-333")
                .build();

        personUpdateDTO = PersonUpdateDTO.builder()
                .firstname("FirstNameTest")
                .lastname("LastNameTest")
                .email("person@gmail.com")
                .password("passwordTest")
                .phone("1-22-333")
                .build();
    }

    @Test
    public void givenPersonId_whenGetPersonById_thenReturnPersonObject() {
        given(personRepository.findById(person.getId())).willReturn(Optional.of(person));

        Person savedPerson = personService.getPersonById(person.getId());

        assertThat(savedPerson).isNotNull();
    }

    @Test
    public void givenPersonList_whenGetAllPersons_thenReturnPersonsList() {
        Person person2 = Person.builder()
                .id(2L)
                .orders(null)
                .rolesList(null)
                .username("UsernameTest2")
                .firstname("FirstNameTest2")
                .lastname("LastNameTest2")
                .email("person2@gmail.com")
                .password("passwordTest2")
                .phone("1-22-333-444")
                .build();

        given(personRepository.findAll()).willReturn(List.of(person, person2));

        List<Person> personList = personService.findAll();

        assertThat(personList).isNotNull();
        assertThat(personList).size().isEqualTo(2);
    }

    @Test
    public void givenPersonId_whenDeletePerson_thenNothing() {
        long personId = 1L;
        given(personRepository.findById(personId)).willReturn(Optional.of(person));
        willDoNothing().given(personRepository).deleteById(personId);

        personService.deleteUser(personId);

        verify(personRepository, times(1)).deleteById(personId);
    }

    @Test
    public void givenPersonObject_whenSavePerson_thenReturnPersonObject() {
        given(personRepository.save(person)).willReturn(person);

        Person savedPerson = personService.save(person);

        assertThat(savedPerson).isNotNull();
    }

    @Test
    public void givenPersonUpdateDTOObject_whenUpdatePerson_thenReturnUpdatedPerson() {
        long id = 1L;
        given(personRepository.findById(id)).willReturn(Optional.of(person));
        given(mapStructMapper.personUpdateDTOToPerson(personUpdateDTO)).willReturn(person);
        given(passwordEncoder.encode(personUpdateDTO.getPassword())).willReturn(person.getPassword());
        given(personRepository.save(person)).willReturn(person);
        person.setUsername("UpdatedUsername");

        Person updatedPerson = personService.updatePerson(personUpdateDTO, id);

        assertThat(updatedPerson.getUsername()).isEqualTo("UpdatedUsername");
    }

}
