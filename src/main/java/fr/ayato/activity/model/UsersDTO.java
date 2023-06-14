package fr.ayato.activity.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsersDTO {
    private String id;
    private String name;
    private String firstname;
    private LocalDateTime birthdate;
    private String sexe;

    public UsersDTO(String name, String firstname, LocalDateTime birthdate, String sexe) {
        this.name = name;
        this.firstname = firstname;
        this.birthdate = birthdate;
        this.sexe = sexe;
    }
}
