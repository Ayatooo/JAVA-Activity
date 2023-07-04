package fr.ayato.activity.controller;

import fr.ayato.activity.model.ActivityDTO;
import fr.ayato.activity.services.CalculService;

import java.util.List;

public class CalculControllerImpl implements CalculController {
    CalculService calculService;
    @Override
    public int calculatTotalLoad(List<ActivityDTO> activityDTOList) {
        this.calculService = new CalculService();
        return this.calculService.totalLoadCalcul(activityDTOList);
    }

    @Override
    public double calculateMonotony(List<ActivityDTO> filteredList) {
        this.calculService = new CalculService();
        return this.calculService.monotonyCalcul(filteredList);
    }

    @Override
    public double calculateAverageDailyTrainingLoad(List<ActivityDTO> filteredList) {
        this.calculService = new CalculService();
        return this.calculService.averageDailyTrainingLoadCalcul(filteredList);
    }
}
