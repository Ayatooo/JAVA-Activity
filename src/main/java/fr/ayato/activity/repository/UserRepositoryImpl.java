package fr.ayato.activity.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import fr.ayato.activity.model.UserDTO;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Objects;
import static fr.ayato.activity.mapper.UserMapper.documentToUser;
import static fr.ayato.activity.mapper.UserMapper.userToDocument;

public class UserRepositoryImpl implements UserRepository {
    MongoCollection<Document> collection;

    public UserRepositoryImpl(MongoCollection<Document> collection) {
        this.collection = collection;
    }

    @Override
    public String save(UserDTO userDTO) {
        InsertOneResult result = this.collection.insertOne(userToDocument(userDTO));
        return Objects.requireNonNull(result.getInsertedId()).toString();
    }

    @Override
    public MongoCollection<Document> getAll() {
        return this.collection;
    }

    @Override
    public UserDTO getOne(String id) {
        return documentToUser(Objects.requireNonNull(this.collection.find(new Document("_id", new ObjectId(id))).first()));
    }

    @Override
    public DeleteResult deleteOne(String id) {
        return this.collection.deleteOne(new Document("_id", new ObjectId(id)));
    }
}
