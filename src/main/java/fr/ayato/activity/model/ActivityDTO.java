package fr.ayato.activity.model;

import lombok.*;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityDTO {
    private String name;
    private int duration;
    private Date date;
    private int rpe;
    private int charge;
}
