package com.example.clothshop.dto;

import com.example.clothshop.entity.Orders;
import com.example.clothshop.entity.Roles;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class PersonDTO {

    private Long id;

    private String username;

    private String firstname;

    private String lastname;

    private String email;

    private String password;

    private String phone;

    private List<Orders> orders;

    //private List<Roles> rolesList;
}
