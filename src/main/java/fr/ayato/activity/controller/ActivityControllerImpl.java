package fr.ayato.activity.controller;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.InsertOneResult;
import fr.ayato.activity.model.ActivityDTO;
import fr.ayato.activity.repository.ActivityRepository;
import org.bson.Document;

public class ActivityControllerImpl implements ActivityController {
    ActivityRepository activityRepository;

    public ActivityControllerImpl(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Override
    public String saveActivity(ActivityDTO activityDTO) {
        return this.activityRepository.save(activityDTO);
    }

    @Override
    public ActivityDTO getOne(String id) {
        return this.activityRepository.getOne(id);
    }

    @Override
    public MongoCollection<Document> getAll() {
        return this.activityRepository.getAll();
    }
}
