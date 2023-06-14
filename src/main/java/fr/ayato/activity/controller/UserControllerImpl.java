package fr.ayato.activity.controller;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import fr.ayato.activity.model.UserDTO;
import fr.ayato.activity.repository.UserRepository;
import org.bson.Document;

public class UserControllerImpl implements UserController {
    UserRepository userRepository;

    public UserControllerImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String saveUser(UserDTO userDTO) {
        return this.userRepository.save(userDTO);
    }

    @Override
    public UserDTO getOne(String id) {
        return this.userRepository.getOne(id);
    }

    @Override
    public MongoCollection<Document> getAll() {
        return this.userRepository.getAll();
    }

    @Override
    public DeleteResult deleteOne(String id) {
        return this.userRepository.deleteOne(id);
    }
}
