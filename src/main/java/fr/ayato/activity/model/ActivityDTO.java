package fr.ayato.activity.model;

import lombok.*;
import org.bson.types.ObjectId;

import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityDTO {
    @Getter
    private ObjectId id;
    @Getter @Setter
    private String name;
    @Getter @Setter
    private int duration;
    @Getter @Setter
    private Date date;
    @Getter @Setter
    private int rpe;
    @Getter @Setter
    private int charge;

    public ActivityDTO(String name, int duration, Date date, int rpe, int charge) {
        this.name = name;
        this.duration = duration;
        this.date = date;
        this.rpe = rpe;
        this.charge = charge;
    }
}
