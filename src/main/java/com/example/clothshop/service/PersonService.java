package com.example.clothshop.service;

import com.example.clothshop.dto.MapStructMapper;
import com.example.clothshop.dto.PersonUpdateDTO;
import com.example.clothshop.entity.Discount;
import com.example.clothshop.entity.Person;
import com.example.clothshop.entity.PersonDiscount;
import com.example.clothshop.repository.PersonDiscountRepository;
import com.example.clothshop.repository.PersonRepository;
import com.example.clothshop.util.exception.PersonNotCreatedException;
import com.example.clothshop.util.exception.PersonNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional(readOnly = true)
public class PersonService {

    private final PersonRepository personRepository;
    private final MapStructMapper mapStructMapper;
    private final PasswordEncoder passwordEncoder;
    private final DiscountService discountService;
    private final PersonDiscountRepository personDiscountRepository;

    public PersonService(PersonRepository personRepository, MapStructMapper mapStructMapper, PasswordEncoder passwordEncoder, DiscountService discountService, PersonDiscountRepository personDiscountRepository) {
        this.personRepository = personRepository;
        this.mapStructMapper = mapStructMapper;
        this.passwordEncoder = passwordEncoder;
        this.discountService = discountService;
        this.personDiscountRepository = personDiscountRepository;
    }

    public Person getPersonById(long id) {
        Optional<Person> person = personRepository.findById(id);
        return person.orElseThrow(PersonNotFoundException::new);
    }

    public List<Person> findAll() {
        return personRepository.findAll();
    }

    public Person findByUsername(String username) {
        Optional<Person> person = personRepository.findByUsername(username);
        return person.orElseThrow(PersonNotFoundException::new);
    }

    public Boolean existsByUsername(String username) {
        return personRepository.existsByUsername(username);
    }

    public Boolean existsByEmail(String email) {
        return personRepository.existsByEmail(email);
    }

    public void checkForValidationErrors(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();

            List<FieldError> errors = bindingResult.getFieldErrors();

            for (FieldError error : errors) {
                errorMsg.append(error.getField())
                        .append(" - ").append(error.getDefaultMessage())
                        .append(";");
            }

            throw new PersonNotCreatedException(errorMsg.toString());
        }
    }

    @Transactional
    public Person save(Person person) {
        return personRepository.save(person);
    }

    @Transactional
    public void deleteUser(long id) {
        Optional<Person> person = personRepository.findById(id);
        if (person.isPresent()) {
            personRepository.deleteById(id);
        } else {
            throw new PersonNotFoundException();
        }
    }

    @Transactional
    public Person updatePerson(PersonUpdateDTO personUpdateDTO, long id) {
        Optional<Person> personSaved = personRepository.findById(id);
        if (personSaved.isPresent()) {
            Person person = personSaved.get();
            person =  mapStructMapper.personUpdateDTOToPerson(personUpdateDTO);
            person.setId(id);
            person.setUsername(personSaved.get().getUsername());
            person.setPassword(passwordEncoder.encode(personUpdateDTO.getPassword()));
            person.setRolesList(personSaved.get().getRolesList());
            person.setOrders(personSaved.get().getOrders());
            return personRepository.save(person);
        } else {
            throw new PersonNotFoundException();
        }
    }

    @Transactional
    public String addDiscount(long personId, long discountId) {
        Discount discount = discountService.getDiscountById(discountId);
        Optional<Person> personSaved = personRepository.findById(personId);
        if (personSaved.isEmpty()) {
            throw new PersonNotFoundException();
        }

        Optional<PersonDiscount> personDiscountSaved = Optional.ofNullable(personDiscountRepository
                .findPersonDiscountByPersonAndDiscount(personId, discountId));
        if (personDiscountSaved.isPresent()) {
            return "The discount was already assigned to the customer.";
        }

        PersonDiscount personDiscount = PersonDiscount.builder()
                .person(personSaved.get())
                .discount(discount)
                .build();
        personDiscountRepository.save(personDiscount);
        return "The " + discount.getValue()*100 + "% discount was assigned to the person with id = " + personSaved.get().getId();
    }

    @Transactional
    public String deleteDiscount(long personId, long discountId) {
        Discount discount = discountService.getDiscountById(discountId);
        Optional<Person> personSaved = personRepository.findById(personId);
        if (personSaved.isEmpty()) {
            throw new PersonNotFoundException();
        }

        Optional<PersonDiscount> personDiscountSaved = Optional.ofNullable(personDiscountRepository
                .findPersonDiscountByPersonAndDiscount(personId, discountId));
        if (personDiscountSaved.isPresent()) {
            personDiscountRepository.delete(personDiscountSaved.get());
            return "The " + discount.getValue()*100 + "% discount assigned to the person with id = " + personSaved.get().getId() + " was deleted.";
        }
        return "The " + discount.getValue()*100 + "% discount wasn't assigned to the person with id = " + personSaved.get().getId() + " and wasn't deleted.";
    }

}
