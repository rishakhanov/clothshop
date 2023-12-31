package com.example.clothshop.dto;

import com.example.clothshop.entity.Orders;
import com.example.clothshop.entity.Roles;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SignupRequestDTO {

    @NotBlank
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank
    @Size(min = 3, max = 50, message = "Firstname should be between 2 and 100 characters")
    private String firstname;

    @NotBlank
    @Size(min = 3, max = 50, message = "Lastname should be between 2 and 100 characters")
    private String lastname;

    @NotBlank
    @Size(max = 50, message = "Email should not be longer than 50 characters")
    @Email
    private String email;

    @NotBlank
    @Size(min = 5, max = 50, message = "Password should be between 5 and 50 characters")
    private String password;

    @NotBlank
    @Size(max = 50)
    private String phone;

    //private List<Orders> orders;

    //private List<Roles> rolesList;


}
