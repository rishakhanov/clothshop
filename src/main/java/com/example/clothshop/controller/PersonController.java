package com.example.clothshop.controller;

import com.example.clothshop.dto.*;
import com.example.clothshop.entity.*;
import com.example.clothshop.service.PersonService;
import com.example.clothshop.service.RolesService;
import com.example.clothshop.service.UserDetailsImpl;
import com.example.clothshop.util.exception.*;
import com.example.clothshop.util.jwt.JwtUtils;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/api/users")
public class PersonController {

    private final PersonService personService;
    private final MapStructMapper mapStructMapper;


    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PersonDTO>> findAll() {
        return ResponseEntity.ok().body(personService.findAll().stream().map(e -> mapStructMapper.personToPersonDTO(e))
                .collect(Collectors.toList()));
    }

    @GetMapping("/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PersonDTO> findByUsername(@PathVariable String username) {
        return ResponseEntity.ok().body(mapStructMapper.personToPersonDTO(personService.findByUsername(username)));
    }

    @GetMapping("/id/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public PersonDTO findById(@PathVariable("id") long id) {
        return mapStructMapper.personToPersonDTO(personService.getPersonById(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(@PathVariable("id") long id) {
        personService.deleteUser(id);
        return "Person with ID = " + id + " was deleted.";
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public PersonDTO update(@PathVariable("id") long id, @RequestBody @Valid PersonUpdateDTO personUpdateDTO,
                            BindingResult bindingResult) {
        personService.checkForValidationErrors(bindingResult);
        return mapStructMapper.personToPersonDTO(personService.updatePerson(personUpdateDTO, id));
    }

    @PostMapping("/{pid}/discount/{did}")
    @PreAuthorize("hasRole('ADMIN')")
    public String addDiscount(@PathVariable("pid") long personId, @PathVariable("did") long discountId) {
        return personService.addDiscount(personId, discountId);
    }

    @PostMapping("/{pid}/discount/{did}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteDiscount(@PathVariable("pid") long personId, @PathVariable("did") long discountId) {
        return personService.deleteDiscount(personId, discountId);
    }

    @ExceptionHandler
    private ResponseEntity<PersonErrorResponse> handlePersonNotFoundException(PersonNotFoundException exception) {
        PersonErrorResponse response = new PersonErrorResponse("User with this id wasn't found!");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<PersonErrorResponse> handlePersonNotCreatedException(PersonNotCreatedException exception) {
        PersonErrorResponse response = new PersonErrorResponse(exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler ResponseEntity<RoleErrorResponse> handleRoleNotFoundException(RoleNotFoundException exception) {
        RoleErrorResponse response = new RoleErrorResponse("Role wasn't found!");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }


}
