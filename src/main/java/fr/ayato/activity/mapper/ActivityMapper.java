package fr.ayato.activity.mapper;

import fr.ayato.activity.model.ActivityDTO;
import org.bson.Document;

public class ActivityMapper {

    public static Document activityToDocument(ActivityDTO activityDTO) {
        return new Document()
                .append("name", activityDTO.getName())
                .append("duration", activityDTO.getDuration())
                .append("date", activityDTO.getDate())
                .append("rpe", activityDTO.getRpe())
                .append("charge", activityDTO.getCharge());
    }

    public static ActivityDTO documentToActivity(Document document) {
        return new ActivityDTO(
                document.getObjectId("_id"),
                document.getString("name"),
                document.getInteger("duration"),
                document.getDate("date"),
                document.getInteger("rpe"),
                document.getInteger("charge")
        );
    }
}
