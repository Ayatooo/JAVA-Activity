package fr.ayato.activity.model;

import lombok.*;
import org.bson.types.ObjectId;

import java.util.Date;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityDTO {
    private ObjectId id;
    @Setter
    private String name;
    @Setter
    private int duration;
    @Setter
    private Date date;
    @Setter
    private int rpe;
    @Setter
    private int charge;

    public ActivityDTO(String name, int duration, Date date, int rpe, int charge) {
        this.name = name;
        this.duration = duration;
        this.date = date;
        this.rpe = rpe;
        this.charge = charge;
    }
}
