package fr.ayato.activity.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import fr.ayato.activity.model.UserDTO;
import org.bson.Document;

public interface UserRepository {
    String save(UserDTO userDTO);
    MongoCollection<Document> getAll();
    UserDTO getOne(String id);
    DeleteResult deleteOne(String id);
}
