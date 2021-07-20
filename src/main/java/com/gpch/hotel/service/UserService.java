package com.gpch.hotel.service;

import com.gpch.hotel.model.ConfirmationToken;
import com.gpch.hotel.model.User;
import com.gpch.hotel.repository.ConfirmationTokenRepository;
import com.gpch.hotel.repository.RoleRepository;
import com.gpch.hotel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("userService")
@Transactional
public class UserService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    public UserService(@Qualifier("userRepository") UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, ConfirmationTokenRepository confirmationTokenRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.confirmationTokenRepository = confirmationTokenRepository;
    }


    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void DeleteUserById(String id) {
        userRepository.deleteById(id);
    }

    public User FindUserById(String id) {
        return userRepository.findById(id);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(1);
        userRepository.save(user);
    }

    public void Updateuser(User user) {
        User userFromDb = userRepository.findById(user.getId());
        userFromDb.setName(user.getName());
        userFromDb.setLastName(user.getLastName());
        userFromDb.setRoles(user.getRoles());
        userRepository.save(userFromDb);
    }

    public void Updateaccount(User user) {
        User userFromDb = userRepository.findById(user.getId());
        userFromDb.setName(user.getName());
        userFromDb.setLastName(user.getLastName());
        userRepository.save(userFromDb);
    }

    public void Changepassword(String id, String newpassword) {
        User userFromDb = userRepository.findById(id);
        userFromDb.setPassword(bCryptPasswordEncoder.encode(newpassword));
        userRepository.save(userFromDb);
    }

    public void forgetpassword(String Email, String newpassword) {
        User userFromDb = userRepository.findByEmail(Email);
        userFromDb.setPassword(bCryptPasswordEncoder.encode(newpassword));
        userRepository.save(userFromDb);
    }

    public void Deletetoken(String confirmationToken) {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
        confirmationTokenRepository.delete(token);
    }

    public ConfirmationToken findconfirmationToken(String confirmationToken) {
        return confirmationTokenRepository.findByConfirmationToken(confirmationToken);
    }

    public String Createtoken(User existingUser) {
        // Create token
        ConfirmationToken confirmationToken = new ConfirmationToken(existingUser);
        // Save it
        confirmationTokenRepository.save(confirmationToken);

        return confirmationToken.getConfirmationToken();
    }
}