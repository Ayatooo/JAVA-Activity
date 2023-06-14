package fr.ayato.activity.model;

import lombok.*;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private ObjectId id;
    @Setter
    private String name;
    @Setter
    private String firstname;
    @Setter
    private Date birthdate;
    @Setter
    private String sexe;
}
