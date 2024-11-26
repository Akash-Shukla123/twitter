package org.nov2024.Service;

import org.nov2024.Models.Role;
import org.nov2024.Repo.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleManagementService{

    private RoleRepository roleRepository;

    @Autowired
    public RoleManagementService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Optional<Role> findByName(String name){
        return roleRepository.findByName(name);
    }

    public Role saveRole(Role role){
        return roleRepository.save(role);
    }

    public List<Role> getAllRole(){
        return roleRepository.findAll();
    }

}
