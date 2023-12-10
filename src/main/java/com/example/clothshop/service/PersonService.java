package com.example.clothshop.service;

import com.example.clothshop.dto.MapStructMapper;
import com.example.clothshop.dto.PersonUpdateDTO;
import com.example.clothshop.entity.Orders;
import com.example.clothshop.entity.Person;
import com.example.clothshop.repository.PersonRepository;
import com.example.clothshop.util.exception.PersonNotCreatedException;
import com.example.clothshop.util.exception.PersonNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PersonService {

    private final PersonRepository personRepository;
    private final MapStructMapper mapStructMapper;
    private final PasswordEncoder passwordEncoder;
    private final OrderService orderService;

    public PersonService(PersonRepository personRepository, MapStructMapper mapStructMapper, PasswordEncoder passwordEncoder, OrderService orderService) {
        this.personRepository = personRepository;
        this.mapStructMapper = mapStructMapper;
        this.passwordEncoder = passwordEncoder;
        this.orderService = orderService;
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

    public List<Orders> getOrdersOfUser(long id) {
        return orderService.getOrdersOfUser(id);
    }

//    @Transactional
//    public Person addRoleToUser(String username, String roleName) {
//        Optional<Person> person = personRepository.findByUsername(username);
//        if (person.isPresent()) {
//            Roles roles = rolesRepository.findByName(roleName);
//            person.get().getRolesList().add(roles);
//        }
//        return person.orElseThrow(PersonNotFoundException::new);
//    }



//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Person person = personRepository.findByUsername(username);
//        if (person == null) {
//            String message = String.format("User with username %s not found", username);
//            throw new UsernameNotFoundException(message);
//        } else {
//            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
//            person.getRolesList().forEach(role -> {
//                authorities.add(new SimpleGrantedAuthority(role.getName()));
//            });
//            return new User(person.getUsername(), person.getPassword(), authorities);
//        }
//
//    }
}
