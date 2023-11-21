package com.example.clothshop.controller;

import com.example.clothshop.dto.MapStructMapper;
import com.example.clothshop.dto.PersonDTO;
import com.example.clothshop.dto.RoleDTO;
import com.example.clothshop.entity.Person;
import com.example.clothshop.service.PersonService;
import com.example.clothshop.util.PersonErrorResponse;
import com.example.clothshop.util.PersonNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class PersonController {

    private final PersonService personService;

    private final MapStructMapper mapStructMapper;

    @GetMapping
    public ResponseEntity<List<PersonDTO>> findAll() {
        return ResponseEntity.ok().body(personService.findAll().stream().map(this::convertToPersonDTO)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{username}")
    public ResponseEntity<Person> findByUsername(@PathVariable String username) {
        return ResponseEntity.ok().body(personService.findByUsername(username));
    }

    @PostMapping
    public ResponseEntity<Person> save(@RequestBody Person user) {
        Person person = personService.save(user);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentRequest().path("/{username}")
                .buildAndExpand(person.getUsername()).toUriString());
        return ResponseEntity.created(uri).build();
    }

    @PostMapping("/{username}/addRoleToUser")
    public ResponseEntity<?> addRoleToUser(@PathVariable String username, @RequestBody RoleDTO request) {
        Person person = personService.addRoleToUser(username, request.getName());
        return ResponseEntity.ok(person);
    }


    @ExceptionHandler
    private ResponseEntity<PersonErrorResponse> handlePersonNotFoundException(PersonNotFoundException exception) {
        PersonErrorResponse response = new PersonErrorResponse("Person with this id wasn't found!");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    private PersonDTO convertToPersonDTO(Person person) {
        return mapStructMapper.personToPersonDTO(person);
    }
}
