package org.nov2024.Controller;

import org.nov2024.Models.Role;
import org.nov2024.Repo.RoleRepository;
import org.nov2024.Service.RoleManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
public class RoleController {

    private RoleManagementService roleManagementService;

    @Autowired
    public RoleController(RoleManagementService roleManagementService) {
        this.roleManagementService = roleManagementService;
    }

    @GetMapping("/read")
    public List<Role> getAllRole(){
        return roleManagementService.getAllRole();
    }

    @PostMapping("/create")
    public Role saveRole(@RequestBody Role role){
        return roleManagementService.saveRole(role);
    }

}
