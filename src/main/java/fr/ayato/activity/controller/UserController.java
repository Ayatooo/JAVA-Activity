package fr.ayato.activity.controller;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import fr.ayato.activity.model.UserDTO;
import org.bson.Document;

@SuppressWarnings("unused")
public interface UserController {
    String saveUser(UserDTO userDTO);
    UserDTO getOne(String id);
    MongoCollection<Document> getAll();
    DeleteResult deleteOne(String id);

}
