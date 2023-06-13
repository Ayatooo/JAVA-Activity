package fr.ayato.activity.controller;

import com.mongodb.client.MongoCollection;
import fr.ayato.activity.model.ActivityDTO;
import org.bson.Document;

public interface ActivityController {
    String saveActivity(ActivityDTO activityDTO);
    MongoCollection<Document> getAll();
    ActivityDTO getOne(String id);
}
