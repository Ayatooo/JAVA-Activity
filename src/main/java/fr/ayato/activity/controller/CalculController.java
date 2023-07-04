package fr.ayato.activity.controller;

import fr.ayato.activity.model.ActivityDTO;

import java.util.List;

public interface CalculController {
    int calculatTotalLoad(List<ActivityDTO> activityDTOList);
    double calculateMonotony(List<ActivityDTO> filteredList);
    double calculateAverageDailyTrainingLoad(List<ActivityDTO> filteredList);
}
