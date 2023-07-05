package fr.ayato.activity.controller;

import fr.ayato.activity.model.ActivityDTO;

import java.util.List;

public interface CalculController {
    int calculateTotalLoad(List<ActivityDTO> activityDTOList);
    double calculateAverageLoad(List<ActivityDTO> activityDTOList);
    double calculateMonotony(List<ActivityDTO> activityDTOList, List<ActivityDTO> formattedWeekTrain);
    double calculateConstraint(int totalLoad, double monotony);
    double calculateFitness(int totalLoad, double constraint);
    List<ActivityDTO> formattedWeekTrain(List<ActivityDTO> activityDTOList);
}
