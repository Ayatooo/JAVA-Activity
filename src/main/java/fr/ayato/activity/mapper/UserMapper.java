package fr.ayato.activity.mapper;

import fr.ayato.activity.model.UserDTO;
import org.bson.Document;

public class UserMapper {

    public static Document userToDocument(UserDTO userDTO) {
        return new Document()
                .append("name", userDTO.getName())
                .append("firstname", userDTO.getFirstname())
                .append("birthdate", userDTO.getBirthdate())
                .append("sexe", userDTO.getSexe());
    }

    public static UserDTO documentToUser(Document document) {
        return new UserDTO(
                document.getObjectId("_id"),
                document.getString("name"),
                document.getString("firstname"),
                document.getDate("birthdate"),
                document.getString("sexe")
        );
    }
}
