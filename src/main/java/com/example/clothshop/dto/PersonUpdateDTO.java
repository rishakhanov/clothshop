package com.example.clothshop.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PersonUpdateDTO {

    @NotBlank
    @Size(min = 3, max = 50, message = "Firstname should be between 2 and 100 characters")
    private String firstname;

    @NotBlank
    @Size(min = 3, max = 50, message = "Lastname should be between 2 and 100 characters")
    private String lastname;

    @NotBlank
    @Email
    @Size(max = 50, message = "Email should not be longer than 50 characters")
    private String email;

    @NotBlank
    @Size(min = 5, max = 50, message = "Password should be between 5 and 50 characters")
    private String password;

    @NotBlank
    private String phone;

}
