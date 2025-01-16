package org.example.externalservice.controller;

import lombok.AllArgsConstructor;
import org.example.externalservice.dto.UserDto;
import org.example.externalservice.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/account")
public class AccountController {
    private AccountService accountService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerParkingUser(@RequestBody UserDto userDto) {
        accountService.registerUser(userDto);
    }
}
