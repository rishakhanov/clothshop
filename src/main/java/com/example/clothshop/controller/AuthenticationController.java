package com.example.clothshop.controller;

import com.example.clothshop.dto.JwtResponseDTO;
import com.example.clothshop.dto.LoginRequestDTO;
import com.example.clothshop.dto.MapStructMapper;
import com.example.clothshop.dto.SignupRequestDTO;
import com.example.clothshop.entity.Person;
import com.example.clothshop.entity.Roles;
import com.example.clothshop.service.PersonService;
import com.example.clothshop.service.RolesService;
import com.example.clothshop.service.UserDetailsImpl;
import com.example.clothshop.util.exception.PersonErrorResponse;
import com.example.clothshop.util.exception.PersonNotCreatedException;
import com.example.clothshop.util.jwt.JwtUtils;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@Slf4j
@RequestMapping("/api/auth")
public class AuthenticationController {


    private AuthenticationManager authenticationManager;
    private PersonService personService;
    private RolesService rolesService;
    private MapStructMapper mapStructMapper;
    private PasswordEncoder encoder;
    private JwtUtils jwtUtils;

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

    @PostMapping("/signup")
    public ResponseEntity<SignupRequestDTO> signup(@Valid @RequestBody SignupRequestDTO signupRequestDTO,
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

    /*
    @ExceptionHandler
    private ResponseEntity<PersonErrorResponse> handlePersonNotCreatedException(PersonNotCreatedException exception) {
        PersonErrorResponse response = new PersonErrorResponse(exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    */

}
