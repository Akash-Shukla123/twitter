package org.nov2024.Controller;

import org.nov2024.Models.Role;
import org.nov2024.Models.User;
import org.nov2024.Repo.RoleRepository;
import org.nov2024.Repo.UserRepository;
import org.nov2024.Service.RoleManagementService;
import org.nov2024.Service.TokenManagementService;
import org.nov2024.Service.UserManagementService;
import org.nov2024.Utils.CustomTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserManagementService userManagementService;
    private TokenManagementService tokenManagementService;
    private RoleManagementService roleManagementService;

    @Autowired
    public UserController(UserManagementService userManagementService, TokenManagementService tokenManagementService, RoleManagementService roleManagementService) {
        this.userManagementService = userManagementService;
        this.tokenManagementService = tokenManagementService;
        this.roleManagementService = roleManagementService;
    }

    @GetMapping("/read")
    public List<User> getAllUsers() {
        return userManagementService.getAllusers();
    }

    @GetMapping("/read-producers")
    public List<String> getAllProducers() {
        return userManagementService.getAllProducers();
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) throws Exception {
        String token = userManagementService.loginUser(user.getUsername(), user.getPassword());

        if(token == null || token.isEmpty())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");

        List<String> roleslist = Arrays.asList(tokenManagementService.getRoles(token).split(","));

        String redirectUrl;
        if (roleslist.contains("PRODUCER")) {
            redirectUrl = "/producer.html";
        } else if (roleslist.contains("SUBSCRIBER")) {
            redirectUrl = "/subscriber.html";
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("redirectUrl", redirectUrl);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<?> addUser(@RequestBody User user) {

        if(userManagementService.findByUsername(user.getUsername()).isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Username already exists");
        }

        user.setCreatedt(new Date(System.currentTimeMillis()));
        user.setCreateby(user.getUsername());

        if(user.getRoles() == null || user.getRoles().isEmpty())
            return ResponseEntity.badRequest().body("At least one role should exist");

        // map role names to role entites
        Set<Role> roles = new HashSet<>();
        for(Role role: user.getRoles()){
            Role existingrole = roleManagementService.findByName(role.getRolename())
                    .orElseThrow(() -> new RuntimeException("Role not present"));

            roles.add(existingrole);
        }

        user.setRoles(roles);

        userManagementService.saveUser(user);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/create-subscription")
    public ResponseEntity<?> createSubscription(@RequestBody String producername, @RequestHeader(value = "Authorization", required = false) String authorizationHeader){

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // Extract the token after "Bearer "
            String token = authorizationHeader.substring(7);

            String username = CustomTokenUtil.extractUsername(token);

            if(username.isEmpty()){
                return ResponseEntity.badRequest().body("Username not found");
            }

            producername = producername.split(":")[1]
                    .replace("\"","")
                    .replace("}","");

            userManagementService.createSubscription(username,producername);

            return ResponseEntity.ok(HttpStatus.OK);
        }else {
            return ResponseEntity.badRequest().body("Invalid Token");
        }
    }
}