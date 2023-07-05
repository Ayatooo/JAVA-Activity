package fr.ayato.activity.controller;

import fr.ayato.activity.model.ActivityDTO;
import fr.ayato.activity.services.CalculService;

import java.util.ArrayList;
import java.util.List;

public class CalculControllerImpl implements CalculController {

    CalculService calculService;

    public CalculControllerImpl() {
        this.calculService = new CalculService();
    }

    @Override
    public int calculateChargeAigue(List<ActivityDTO> activityDTOList) {
        return this.calculService.calculateChargeAigue(activityDTOList);
    }

    @Override
    public double calculateMonotony(List<ActivityDTO> activityDTOList, List<ActivityDTO> formattedWeekTrain) {
        return this.calculService.calculateMonotony(activityDTOList, formattedWeekTrain);
    }

    @Override
    public double calculateAverageLoad(List<ActivityDTO> filteredList) {
        return this.calculService.averageDailyLoad(filteredList);
    }

    @Override
    public double calculateConstraint(int totalLoad, double monotony) {
        return this.calculService.calculateConstraint(totalLoad, monotony);
    }

    @Override
    public double calculateFitness(int totalLoad, double constraint) {
        return this.calculService.calculateFitness(totalLoad, constraint);
    }

    @Override
    public List<ActivityDTO> formattedWeekTrain(List<ActivityDTO> activityDTOList) {
        return this.calculService.formattedWeekTrain(activityDTOList);
    }

    @Override
    public double calculateAcwr(int chargeAigue, int chargeChronique) {
        return this.calculService.calculateAcwr(chargeAigue, chargeChronique);
    }

    @Override
    public ArrayList<String> calculateHealthIndicator(double monotony, double constraint, double acwr) {
        return this.calculService.calculateHealthIndicator(monotony, constraint, acwr);
    }
}
