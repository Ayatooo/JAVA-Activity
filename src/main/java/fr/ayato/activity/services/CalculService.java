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
    public double calculateMonotony(List<ActivityDTO> activityDTOList, List<ActivityDTO> formattedWeekTrain) {
        double ceqm = averageDailyLoad(activityDTOList);
        double ecartType = ecartType(formattedWeekTrain);

        return ceqm / ecartType;
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

    public List<ActivityDTO> formattedWeekTrain(List<ActivityDTO> activityDTOList) {
        Map<Date, ActivityDTO> formattedMap = new HashMap<>();

        for (ActivityDTO activityDTO : activityDTOList) {
            Date activityDate = activityDTO.getDate();

            // Récupérer la date du jour sans l'heure
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(activityDate);
            Date dayDate = calendar.getTime();

            // Vérifier si l'entraînement pour ce jour existe déjà dans le map
            if (formattedMap.containsKey(dayDate)) {
                // Ajouter la charge à l'entraînement existant
                ActivityDTO existingActivity = formattedMap.get(dayDate);
                existingActivity.setCharge(existingActivity.getCharge() + activityDTO.getCharge());
            } else {
                // Ajouter l'entraînement au map
                formattedMap.put(dayDate, activityDTO);
            }
        }

        // Créer une liste à partir des valeurs du map
        List<ActivityDTO> formattedList = new ArrayList<>(formattedMap.values());

        return formattedList;
    }

    /**
     * Ecart type
     */
    private double ecartType(List<ActivityDTO> activityDTOList) {
        // foreach activity, if the day is the same, add the charge to the daily charge
        int[] dailyCharges = activityDTOList.stream().mapToInt(ActivityDTO::getCharge).toArray();
        double sum = 0.0, standardDeviation = 0.0;
        int length = 7;

        for(double num : dailyCharges) {
            sum += num;
        }

        double mean = sum/length;

        for(double num: dailyCharges) {
            standardDeviation += Math.pow(num - mean, 2);
        }

        return Math.sqrt(standardDeviation/length);
    }

}
