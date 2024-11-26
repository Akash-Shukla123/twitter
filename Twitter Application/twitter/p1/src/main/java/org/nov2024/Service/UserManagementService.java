package org.nov2024.Service;

import jakarta.transaction.Transactional;
import org.nov2024.Models.ManageSubscription;
import org.nov2024.Models.Role;
import org.nov2024.Models.User;
import org.nov2024.Repo.RoleRepository;
import org.nov2024.Repo.SubscriptionRepository;
import org.nov2024.Repo.UserRepository;
import org.nov2024.Utils.CustomTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserManagementService {
    private UserRepository userRepository;
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    public UserManagementService(UserRepository userRepository, RoleRepository roleRepository, SubscriptionRepository subscriptionRepository) {
        this.userRepository = userRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    public List<User> getAllusers(){
        return userRepository.findAll();
    }

    public String loginUser(String username, String password) throws AccountNotFoundException {
        User user = userRepository.findByUsernamePassword(username,password).orElseThrow(
                () -> new AccountNotFoundException("User not found with username "+username));

        Set<String> rolenames = user.getRoles().stream().map(Role::getRolename).collect(Collectors.toSet());
        return CustomTokenUtil.generateToken(username,rolenames);
    }

    public Optional<User> findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    @Transactional
    public void saveUser(User user){
        userRepository.save(user);
    }

    @Transactional
    public void createSubscription(String subscriber, String producer){

        User subscriberUser = userRepository.findByUsername(subscriber).orElseThrow(
                () -> new IllegalArgumentException("user not found"));

        User producerUser = userRepository.findByUsername(producer).orElseThrow(
                () -> new IllegalArgumentException("user not found"));

        if(subscriberUser.getUserid().equals(producerUser.getUserid())){
            throw new IllegalArgumentException("User cannot subscribe to himself/herself");
        }

        Integer isSubscription = subscriptionRepository.existsBySubscriberAndProducer(subscriberUser.getUserid() , producerUser.getUserid());
        if(isSubscription == 1){
            throw new IllegalArgumentException("Subscription already Exists");
        }

        // subscription can only happen b/w S-P & P-P

        boolean isEligible = true;
        if(subscriberUser.getRoles().contains("SUBSCRIBER") && producerUser.getRoles().contains("PRODUCER")){
            isEligible = false;
        }

        if(subscriberUser.getRoles().contains("PRODUCER") && producerUser.getRoles().contains("PRODUCER")){
            isEligible = false;
        }

        if(isEligible == false){
            throw new IllegalArgumentException("User not eligible to subscribe");
        }

        ManageSubscription manageSubscription = new ManageSubscription();
        manageSubscription.setSubscriber(subscriberUser);
        manageSubscription.setProducer(producerUser);

        subscriptionRepository.save(manageSubscription);

    }

    public List<String> getAllProducers(){
        return userRepository.getAllProducers();
    }

}
