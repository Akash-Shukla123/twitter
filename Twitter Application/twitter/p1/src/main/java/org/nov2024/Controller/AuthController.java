package org.nov2024.Controller;

import org.nov2024.Models.User;
import org.nov2024.Repo.UserRepository;
import org.nov2024.Service.TokenManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private TokenManagementService tokenManagementService;
    @Autowired
    public AuthController(TokenManagementService tokenManagementService, UserRepository userRepository) {
        this.tokenManagementService = tokenManagementService;
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validate(@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // Extract the token after "Bearer "
            String token = authorizationHeader.substring(7);

            Map<String, Object> response = new HashMap<>();

            Boolean isValid = tokenManagementService.validateToken(token);
            if(!isValid){
                response.put("valid", false);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }


            Boolean userAndRolesValid = tokenManagementService.validateUserAndRoles(token);
            // Return ResponseEntity with the Map and HTTP status
            if (!userAndRolesValid) {
                response.put("valid", false);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            response.put("valid", true);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return ResponseEntity.badRequest().body("Invalid Auth Key");
        }
    }
}
