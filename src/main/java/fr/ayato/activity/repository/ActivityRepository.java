package fr.ayato.activity.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import fr.ayato.activity.model.ActivityDTO;
import org.bson.Document;

public interface ActivityRepository {
    String save(ActivityDTO activityDTO);
    MongoCollection<Document> getAll();
    ActivityDTO getOne(String id);
    DeleteResult deleteOne(String id);
}
