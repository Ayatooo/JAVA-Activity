package fr.ayato.activity.controller;

import fr.ayato.activity.model.ActivityDTO;
import fr.ayato.activity.services.CalculService;

import java.util.List;

public class CalculControllerImpl implements CalculController {

    CalculService calculService;

    public CalculControllerImpl() {
        this.calculService = new CalculService();
    }

    @Override
    public int calculateTotalLoad(List<ActivityDTO> activityDTOList) {
        return this.calculService.calculateTotalLoad(activityDTOList);
    }

    @Override
    public double calculateMonotony(List<ActivityDTO> filteredList) {
        return this.calculService.calculateMonotony(filteredList);
    }

    @Override
    public double calculateAverageLoad(List<ActivityDTO> filteredList) {
        return this.calculService.calculateAverageLoad(filteredList);
    }

    @Override
    public double calculateConstraint(int totalLoad, double monotony) {
        return this.calculService.calculateConstraint(totalLoad, monotony);
    }

    @Override
    public double calculateFitness(int totalLoad, double constraint) {
        return this.calculService.calculateFitness(totalLoad, constraint);
    }
}
