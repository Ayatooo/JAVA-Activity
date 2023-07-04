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
        int numberOfDays = getNumberOfDaysInWeek(activityDTOList);
        return totalLoad / (double) numberOfDays;
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


    /**
     * Nombre de jours dans la semaine
     */
    private int getNumberOfDaysInWeek(List<ActivityDTO> activityDTOList) {
        Set<Date> allDays = new HashSet<>();

        for (ActivityDTO activityDTO : activityDTOList) {
            allDays.add(activityDTO.getDate());
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(Collections.min(allDays));
        int startDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        calendar.setTime(Collections.max(allDays));
        int endDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        int numberOfDays = (endDayOfWeek - startDayOfWeek + 1) % 7;
        if (numberOfDays < 0) {
            numberOfDays += 7;
        }

        return numberOfDays;
    }

}
