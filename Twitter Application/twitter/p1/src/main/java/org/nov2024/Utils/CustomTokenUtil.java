package org.nov2024.Utils;

import org.nov2024.Models.Permissions;
import org.nov2024.Models.Role;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class CustomTokenUtil {

    private static final String SECRET_KEY = "twitterapplication-authenticateuser";
    private static final long VALIDITY = 1000*300;

    public static String generateToken(String username, Set<String> rolenames){

        Map<String, Object> payload = new HashMap<>();
        payload.put("username", username);
        payload.put("roles", rolenames.toString());
        payload.put("expiry", String.valueOf(System.currentTimeMillis() + VALIDITY));

        String payloadStr = payload.toString();
        String encodedPayload = Base64.getEncoder().encodeToString(payloadStr.getBytes(StandardCharsets.UTF_8));

        String encodedSignature = Base64.getEncoder().encodeToString((encodedPayload+SECRET_KEY).getBytes(StandardCharsets.UTF_8));

        return encodedPayload+"."+encodedSignature;
    }

    public static Boolean validateToken(String token) {
        try {
            String[] tokenparts = token.split("\\.");
            if (tokenparts.length != 2)
                return false;

            String encodedPayload = tokenparts[0];
            String encodedSignature = tokenparts[1];

            String payload = new String(Base64.getDecoder().decode(encodedPayload), StandardCharsets.UTF_8);
            //String signature = new String(Base64.getDecoder().decode(encodedSignature), StandardCharsets.UTF_8);

            String expectedSignature = Base64.getEncoder().encodeToString((encodedPayload + SECRET_KEY).getBytes(StandardCharsets.UTF_8));

            if (!encodedSignature.equals(expectedSignature)) {
                return false;
            }

            payload = payload.substring(1, payload.length() - 1);

            String expiryString = payload.split(",")[1].split("=")[1].replace("}", "").trim();
            long expiry = Long.parseLong(expiryString);

            return System.currentTimeMillis() <= expiry;

        } catch (Exception e) {
            return false;
        }
    }

    public static String extractUsername(String token){
        try {
            String[] tokenparts = token.split("\\.");
            if(tokenparts.length != 2)
                throw new RuntimeException("Invalid token format");

            String encodedPayload = tokenparts[0];
            String payload = new String(Base64.getDecoder().decode(encodedPayload), StandardCharsets.UTF_8);

            String username = payload.split(",")[2].split("=")[1]
                    .replace("}", "")
                    .trim();

            if(username == null || username.isEmpty()){
                throw new RuntimeException("Invalid token format in payload");
            }else{
                return username;
            }

        } catch (Exception e) {
            throw new RuntimeException("Invalid Token");
        }
    }

    public static String extractRoles(String token){
        try {
            String[] tokenparts = token.split("\\.");
            if(tokenparts.length != 2)
                throw new RuntimeException("Invalid token format");

            String encodedPayload = tokenparts[0];
            String payload = new String(Base64.getDecoder().decode(encodedPayload), StandardCharsets.UTF_8);
            payload = payload.substring(1, payload.length() - 1);

            String roles = payload.split(",")[0].split("=")[1]
                    .replace("{", "")
                    .replace("[", "")
                    .replace("]", "")
                    .trim();

            if(roles == null || roles.isEmpty()){
                throw new RuntimeException("Invalid token format in payload");
            }else{
                return roles;
            }

        } catch (Exception e) {
            throw new RuntimeException("Invalid Token");
        }
    }
}
