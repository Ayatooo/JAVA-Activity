package fr.ayato.activity;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import fr.ayato.activity.controller.ActivityControllerImpl;
import fr.ayato.activity.model.ActivityDTO;
import fr.ayato.activity.repository.ActivityRepositoryImpl;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static fr.ayato.activity.mapper.ActivityMapper.documentToActivity;

@Slf4j
public class Main {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure().load();
        MongoCollection<Document> collection = Connection.client(dotenv.get("DB_NAME"), dotenv.get("DB_COLLECTION"));
        ActivityRepositoryImpl activityRepository = new ActivityRepositoryImpl(collection);
        //save(activityRepository);
        //getAll(activityRepository);
        //getById(activityRepository, "648840c1020c1b00db0914ed");
        //deleteById(activityRepository, "648840c1020c1b00db0914ed");

    }

    private static void save(ActivityRepositoryImpl activityRepository) {
        ActivityDTO activity = new ActivityDTO(
                "rommmmm",
                60,
                new Date(),
                8,
                5
        );

        ActivityControllerImpl activityController = new ActivityControllerImpl(activityRepository);
        String id = activityController.saveActivity(activity);
        log.info("Actvity created", id);
    }

    private static void getAll(ActivityRepositoryImpl activityRepository) {
        ActivityControllerImpl activityController = new ActivityControllerImpl(activityRepository);
        MongoCollection<Document> activities = activityController.getAll();
        List<ActivityDTO> activityDTOList = new ArrayList<>();
        for (Document activity : activities.find()) {
            activityDTOList.add(documentToActivity(activity));
        }
        activityDTOList.forEach(activityDTO -> log.info(activityDTO.getId().toString()));
    }

    private static void getById(ActivityRepositoryImpl activityRepository, String id) {
        ActivityControllerImpl activityController = new ActivityControllerImpl(activityRepository);
        ActivityDTO activityDTO = activityController.getOne(id);
        log.info(activityDTO.getName());
    }

    private static void deleteById(ActivityRepositoryImpl activityRepository, String id) {
        ActivityControllerImpl activityController = new ActivityControllerImpl(activityRepository);
        activityController.deleteOne(id);
        log.info("Activity deleted");
    }
}