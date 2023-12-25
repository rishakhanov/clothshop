package com.example.clothshop.controller;

import com.example.clothshop.dto.*;
import com.example.clothshop.entity.Orders;
import com.example.clothshop.entity.Person;
import com.example.clothshop.entity.Roles;
import com.example.clothshop.service.PersonService;
import com.example.clothshop.service.RolesService;
import com.example.clothshop.service.UserDetailsImpl;
import com.example.clothshop.util.exception.*;
import com.example.clothshop.util.jwt.JwtUtils;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@RequestMapping("/api/users")
public class PersonController {

    private final AuthenticationManager authenticationManager;
    private final PersonService personService;
    private final RolesService rolesService;
    private final MapStructMapper mapStructMapper;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;


    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PersonDTO>> findAll() {
        return ResponseEntity.ok().body(personService.findAll().stream().map(this::convertToPersonDTO)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PersonDTO> findByUsername(@PathVariable String username) {
        return ResponseEntity.ok().body(convertToPersonDTO(personService.findByUsername(username)));
    }

    @GetMapping("/id/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public PersonDTO findById(@PathVariable("id") long id) {
        return convertToPersonDTO(personService.getPersonById(id));
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

//    @GetMapping("/orders")
//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
//    public List<PersonOrdersDTO> getOrdersOfUser(Authentication authentication) {
//        //authentication = SecurityContextHolder.getContext().getAuthentication();
//        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
//        Long id = userPrincipal.getId();
//        List<Orders> orders = personService.getOrdersOfUser(id);
//        return orders.stream().map(mapStructMapper::orderToPersonOrdersDTO).collect(Collectors.toList());
//    }

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

    private PersonDTO convertToPersonDTO(Person person) {
        return mapStructMapper.personToPersonDTO(person);
    }


    /*
    @PostMapping()
    public ResponseEntity<SignupRequestDTO> createUser(@Valid @RequestBody SignupRequestDTO signupRequestDTO,
                                        BindingResult bindingResult) {
        if (personService.existsByUsername(signupRequestDTO.getUsername())) {
            throw new PersonNotCreatedException("A user already exists with that username.");
        }

        if (personService.existsByEmail(signupRequestDTO.getEmail())) {
            throw new PersonNotCreatedException("Email is already in use!");
        }

        personService.checkForValidationErrors(bindingResult);

        Person person = mapStructMapper.signupRequestDTOToPerson(signupRequestDTO);

        person.setPassword(encoder.encode(signupRequestDTO.getPassword()));

        List<Roles> roles = new ArrayList<>();
        Roles role = rolesService.findByName("ROLE_USER");
        roles.add(role);
        person.setRolesList(roles);
        person.setOrders(new ArrayList<>());

        return ResponseEntity.ok(mapStructMapper.personToSignupRequestDTO(personService.save(person)));
    }
    */
    /*
    @GetMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponseDTO(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }
     */

//    @GetMapping("/logout")
//    public String logout() {
//
//    }

//    @PostMapping
//    public ResponseEntity<Person> save(@RequestBody Person user) {
//        Person person = personService.save(user);
//        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentRequest().path("/{username}")
//                .buildAndExpand(person.getUsername()).toUriString());
//        return ResponseEntity.created(uri).build();
//    }

//    @PostMapping("/{username}/addRoleToUser")
//    public ResponseEntity<?> addRoleToUser(@PathVariable String username, @RequestBody RoleDTO request) {
//        Person person = personService.addRoleToUser(username, request.getName());
//        return ResponseEntity.ok(person);
//    }




}
