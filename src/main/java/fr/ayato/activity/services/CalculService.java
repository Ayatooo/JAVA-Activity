package fr.ayato.activity.services;

import fr.ayato.activity.model.ActivityDTO;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class CalculService {

    /**
     * Charge totale hebdomadaire
     */
    public int calculateTotalLoad(List<ActivityDTO> activityDTOList) {
        int totalLoad = 0;

        for (ActivityDTO activityDTO : activityDTOList) {
            int duration = activityDTO.getDuration();
            int rpe = activityDTO.getRpe();
            int charge = duration * rpe;

            totalLoad += charge;
        }

        return totalLoad;
    }

    /**
     * Monotonie
     */
    public double calculateMonotony(List<ActivityDTO> activityDTOList) {
        int totalLoad = calculateTotalLoad(activityDTOList);
        double ecartType = ecartType(activityDTOList);

        return totalLoad / ecartType;
    }

    /**
     * Contrainte
     */
    public double calculateConstraint(int totalLoad, double monotony) {
        return totalLoad * monotony;
    }

    /**
     * Fitness
     */
    public double calculateFitness(int totalLoad, double constraint) {
        return totalLoad - constraint;
    }

    // ----------------------------------------------------------
    // Utils
    //-----------------------------------------------------------

    /**
     * Charge totale hebdomadaire
     */
    public double averageDailyLoad(List<ActivityDTO> activityDTOList) {
        int totalLoad = calculateTotalLoad(activityDTOList);
        return totalLoad / 7.0;
    }

    /**
     * Ecart type
     */
    private double ecartType(List<ActivityDTO> activityDTOList) {
        int[] dailyCharges = activityDTOList.stream().mapToInt(ActivityDTO::getCharge).toArray();
        double averageCharges = this.averageDailyLoad(activityDTOList);

        double sumSquareDifference = 0.0;
        for (double charge : dailyCharges) {
            double diff = charge - averageCharges;
            double squareDiff = diff * diff;
            sumSquareDifference += squareDiff;
        }

        double sumSquareDiff = sumSquareDifference / dailyCharges.length;
        return Math.sqrt(sumSquareDiff);
    }

}
