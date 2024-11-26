package org.nov2024.Service;

import org.nov2024.Models.Role;
import org.nov2024.Models.User;
import org.nov2024.Repo.UserRepository;
import org.nov2024.Utils.CustomTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TokenManagementService {
    private UserRepository userRepository;

    @Autowired
    public TokenManagementService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Boolean validateUserAndRoles(String token){
        String roles = CustomTokenUtil.extractRoles(token);
        String username = CustomTokenUtil.extractUsername(token);

        Optional<User> user = userRepository.findByUsername(username);

        if(!user.isPresent()){return false;}

        Set<Role> rolesSet = user.get().getRoles();

        StringBuilder expectedroles = new StringBuilder();
        for(Role r : rolesSet) {
            if(expectedroles.length() > 0)
                expectedroles.append(",");
            expectedroles.append(r.getRolename());
        }

        return (expectedroles.toString().equalsIgnoreCase(roles));
    }

    public Boolean validateToken(String token){
        return CustomTokenUtil.validateToken(token);
    }

    public String extractUsername(String token){
        return CustomTokenUtil.extractUsername(token);
    }

    public String getRoles(String token){
        return CustomTokenUtil.extractRoles(token);
    }
}
