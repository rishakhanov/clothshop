package com.example.clothshop.service;

import com.example.clothshop.entity.Person;
import com.example.clothshop.entity.Roles;
import com.example.clothshop.repository.PersonRepository;
import com.example.clothshop.repository.RolesRepository;
import com.example.clothshop.util.PersonNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PersonService implements UserDetailsService {

    private final PersonRepository personRepository;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;

    public PersonService(PersonRepository personRepository, RolesRepository rolesRepository,
                         PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.rolesRepository = rolesRepository;
        this.passwordEncoder = passwordEncoder;

    }

    public Person getPersonById(long id) {
        Optional<Person> person = personRepository.findById(id);
        return person.orElseThrow(PersonNotFoundException::new);
    }

    @Transactional
    public Person save(Person person) {
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        return personRepository.save(person);
    }

    @Transactional
    public Person addRoleToUser(String username, String roleName) {
        Person person = personRepository.findByUsername(username);
        Roles roles = rolesRepository.findByName(roleName);
        //person.getRoles().add(roles);
        return person;
    }

    public List<Person> findAll() {
        return personRepository.findAll();
    }

    public Person findByUsername(String username) {
        return personRepository.findByUsername(username);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Person person = personRepository.findByUsername(username);
        if (person == null) {
            String message = String.format("User with username %s not found", username);
            throw new UsernameNotFoundException(message);
        } else {
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            person.getRolesList().forEach(role -> {
                authorities.add(new SimpleGrantedAuthority(role.getName()));
            });
            return new User(person.getUsername(), person.getPassword(), authorities);
        }

    }
}
