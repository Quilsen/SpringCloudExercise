package org.example.externalservice.dto;

public record UserDto(String username,
                      String email,
                      String firstName,
                      String lastName,
                      String password) {
}
