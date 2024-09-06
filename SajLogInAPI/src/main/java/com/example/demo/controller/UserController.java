package com.example.demo.controller;

import com.example.demo.DTO.logInDTO;
import com.example.demo.repo.UserRepo;
import com.example.demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

    @RestController
 //   @RequestMapping("/auth")
    public class UserController {

        @Autowired
        UserRepo repo;

        @Autowired
        private PasswordEncoder passwordEncoder;

        @GetMapping("/getUser")
        public List<User> getUser() {
            return repo.findAll();
        }

        @GetMapping("/test")
        public String test() {
            System.out.println("Test endpoint was hit!");
            return "Server is running!";
        }

        @PostMapping("/create")
        public String create(@RequestBody User user) {
            if (repo.findByUserName(user.getUserName()).isPresent()) {
                return "Username already taken!";
            }
            String id = UUID.randomUUID().toString();

            String hashedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(hashedPassword);

            repo.save(user);

            return "User created successfully";
        }

        //using DTO--
        @PostMapping("/logInUser")
        public ResponseEntity<String> logInUser(@RequestBody logInDTO loginDTO) {
            System.out.println("Received Username: " + loginDTO.getUserName());
            System.out.println("Received Password: " + loginDTO.getPassword());

            Optional<User> foundUser = repo.findByUserName(loginDTO.getUserName());

            if (foundUser.isPresent()) {
                User user = foundUser.get();

                if (passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
                    return ResponseEntity.ok("LogIn Successfully");
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Password");
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid Username");
            }
        }

//        @PostMapping("/logInUser")
//        public String logInUser(@RequestBody User user) {
//            System.out.println("Received Username: " + user.getUserName());
//            System.out.println("Received Password: " + user.getPassword());
//
//            Optional<User> foundUser = repo.findByUserName(user.getUserName());
//
//            if (foundUser.isPresent()) {
//                System.out.println("User found: " + foundUser.get());
//
//                boolean passwordMatches = passwordEncoder.matches(user.getPassword(), foundUser.get().getPassword());
//                System.out.println("Password matches: " + passwordMatches);
//
//                if (passwordMatches) {
//                    return "LogIn Successfully";
//                } else {
//                    return "Invalid Password";
//                }
//            } else {
//                return "Invalid Username";
//            }
//        }
    }





