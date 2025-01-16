package org.example.externalservice.dto;

import java.util.List;

public record UserRepresentation(
        String id,
        String username,
        boolean enabled,
        String email,
        String firstName,
        String lastName,
        boolean emailVerified,
        List<Credential> credentials,
        List<String> groups
) {
    public static UserRepresentation from(UserDto userDto, String group) {
        return new UserRepresentation(
                null,
                userDto.username(),
                true,
                userDto.email(),
                userDto.firstName(),
                userDto.lastName(),
                true,
                List.of(new Credential("password", userDto.password(), false)),
                List.of(group)
        );
    }

    public record Credential(String type, String value, boolean temporary) {
        public static Credential from( String value) {
            return new Credential(
                    "password",
                    value,
                    false
            );
        }
    }
}