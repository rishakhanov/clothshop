package com.example.clothshop.service;

import com.example.clothshop.entity.Roles;
import com.example.clothshop.repository.RolesRepository;
import com.example.clothshop.util.exception.RoleNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@Transactional(readOnly = true)
public class RolesService {

    private final RolesRepository rolesRepository;

    public RolesService(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
    }

    public Roles findByName(String roleName) {
        Optional<Roles> role = rolesRepository.findByName(roleName);
        return role.orElseThrow(RoleNotFoundException::new);
    }
}
