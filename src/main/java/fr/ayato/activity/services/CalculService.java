package fr.ayato.activity.services;

import fr.ayato.activity.model.ActivityDTO;

import java.util.*;

public class CalculService {
    public int totalLoadCalcul(List<ActivityDTO> activityDTOList){
        int totalLoad = 0;

        for (ActivityDTO activityDTO : activityDTOList) {
            int duration = activityDTO.getDuration();
            int rpe = activityDTO.getRpe();
            int load = duration * rpe;

            totalLoad += load;
        }

        return totalLoad;
    }

    public double monotonyCalcul(List<ActivityDTO> filteredList) {
        int totalDuration = 0;
        int totalCharge = 0;

        for (ActivityDTO activityDTO : filteredList) {
            int duration = activityDTO.getDuration();
            int rpe = activityDTO.getRpe();
            int charge = duration * rpe;

            totalDuration += duration;
            totalCharge += charge;
        }

        double monotony = totalCharge / Math.sqrt(totalDuration);

        return monotony;
    }

    public double averageDailyTrainingLoadCalcul(List<ActivityDTO> filteredList) {
        int totalCharge = 0;
        Set<Date> trainingDays = new HashSet<>();

        for (ActivityDTO activityDTO : filteredList) {
            int duration = activityDTO.getDuration();
            int rpe = activityDTO.getRpe();
            int charge = duration * rpe;

            totalCharge += charge;
            trainingDays.add(activityDTO.getDate());
        }

        int numberOfDays = getNumberOfDaysInWeek(filteredList);
        double averageDailyTrainingLoad = totalCharge / (double) numberOfDays;

        return averageDailyTrainingLoad;
    }

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

    /*    private double calculateACWR(List<ActivityDTO> currentWeekActivities, List<ActivityDTO> previousFourWeeksActivities) {
        int currentWeekLoad = calculateTotalLoad(currentWeekActivities);
        double averageFourWeeksLoad = calculateAverageLoad(previousFourWeeksActivities);

        double acwr = currentWeekLoad / averageFourWeeksLoad;

        return acwr;
    }*/

    private double calculateAverageLoad(List<ActivityDTO> activityDTOList) {
        int totalLoad = 0;

        for (ActivityDTO activityDTO : activityDTOList) {
            int duration = activityDTO.getDuration();
            int rpe = activityDTO.getRpe();
            int load = duration * rpe;

            totalLoad += load;
        }

        double averageLoad = totalLoad / 4.0; // Divide by 4 weeks

        return averageLoad;
    }

    private void calculateFitness(List<ActivityDTO> activityDTOList) {
        int totalDuration = 0;
        int totalRPE = 0;
        int totalCharge = 0;

        for (ActivityDTO activityDTO : activityDTOList) {
            int duration = activityDTO.getDuration();
            int rpe = activityDTO.getRpe();
            int charge = duration * rpe;

            totalDuration += duration;
            totalRPE += rpe;
            totalCharge += charge;
        }

        System.out.println("Total Duration: " + totalDuration);
        System.out.println("Total RPE: " + totalRPE);
        System.out.println("Total Charge: " + totalCharge);
    }

}
