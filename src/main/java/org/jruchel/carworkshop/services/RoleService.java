package org.jruchel.carworkshop.services;

import org.jruchel.carworkshop.models.entities.Role;
import org.jruchel.carworkshop.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Role findById(Integer id) {
        Optional<Role> optional = roleRepository.findById(id);
        return optional.orElse(null);
    }

    public List<Role> findAll() {
        return (List<Role>) roleRepository.findAll();
    }

    public void save(Role role) {
        roleRepository.save(role);
    }

    public Role getRoleByTitle(String title) {
        return roleRepository.getRoleByTitle(title);
    }
}