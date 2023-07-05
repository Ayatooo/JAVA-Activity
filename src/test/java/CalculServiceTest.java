import fr.ayato.activity.model.ActivityDTO;
import fr.ayato.activity.services.CalculService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class CalculServiceTest {
    private CalculService calculService;

    @BeforeEach
    void setUp() {
        calculService = new CalculService();
    }

    @Test
    void calculateChargeAigue_ShouldReturnTotalLoad() {
        // Arrange
        List<ActivityDTO> activityDTOList = Arrays.asList(
                new ActivityDTO("Activity 1", 60, new Date(), 7, 420),
                new ActivityDTO("Activity 2", 45, new Date(), 6, 270),
                new ActivityDTO("Activity 3", 30, new Date(), 5, 150)
        );

        // Act
        int result = calculService.calculateChargeAigue(activityDTOList);

        // Assert
        assertEquals(840, result);
    }

    @Test
    void calculateMonotony_ShouldReturnMonotonyValue() {
        // Arrange
        List<ActivityDTO> activityDTOList = Arrays.asList(
                new ActivityDTO("Activity 1", 60, new Date(), 7, 420),
                new ActivityDTO("Activity 2", 45, new Date(), 6, 270),
                new ActivityDTO("Activity 3", 30, new Date(), 5, 150)
        );
        List<ActivityDTO> formattedWeekTrain = Arrays.asList(
                new ActivityDTO("Activity 4", 120, new Date(), 7, 840),
                new ActivityDTO("Activity 5", 90, new Date(), 6, 540),
                new ActivityDTO("Activity 6", 60, new Date(), 5, 300)
        );
        double expected = 0.4714045207910317;

        // Act
        double result = calculService.calculateMonotony(activityDTOList, formattedWeekTrain);

        // Assert
        assertEquals(expected, result, 0.001);
    }

    @Test
    void calculateConstraint_ShouldReturnConstraintValue() {
        // Arrange
        int totalLoad = 840;
        double monotony = 1.832;
        double expected = 1538.88;

        // Act
        double result = calculService.calculateConstraint(totalLoad, monotony);

        // Assert
        assertEquals(expected, result, 0.01);
    }

    @Test
    void calculateFitness_ShouldReturnFitnessValue() {
        // Arrange
        int totalLoad = 840;
        double constraint = 1538.88;
        double expected = -698.88;

        // Act
        double result = calculService.calculateFitness(totalLoad, constraint);

        // Assert
        assertEquals(expected, result, 0.01);
    }

    @Test
    void calculateAcwr_ShouldReturnAcwrValue() {
        // Arrange
        int chargeAigue = 840;
        int chargeChronique = 1000;
        double expected = 0.84;

        // Act
        double result = calculService.calculateAcwr(chargeAigue, chargeChronique);

        // Assert
        assertEquals(expected, result, 0.001);
    }

    @Test
    void calculateHealthIndicator_ShouldReturnHealthIndicator() {
        // Arrange
        double monotony = 1.5;
        double constraint = 5000;
        double acwr = 1.1;
        List<String> expected = Arrays.asList("green", "Entrainement optimal ✨");

        // Act
        List<String> result = calculService.calculateHealthIndicator(monotony, constraint, acwr);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    void averageDailyLoad_ShouldReturnAverageDailyLoad() {
        // Arrange
        List<ActivityDTO> activityDTOList = Arrays.asList(
                new ActivityDTO("Activity 1", 60, new Date(), 7, 420),
                new ActivityDTO("Activity 2", 45, new Date(), 6, 270),
                new ActivityDTO("Activity 3", 30, new Date(), 5, 150)
        );
        double expected = 120.0;

        // Act
        double result = calculService.averageDailyLoad(activityDTOList);

        // Assert
        assertEquals(expected, result, 0.001);
    }

    @Test
    void ecartType_ShouldReturnEcartType() {
        // Arrange
        List<ActivityDTO> activityDTOList = Arrays.asList(
                new ActivityDTO("Activity 1", 60, new Date(), 7, 420),
                new ActivityDTO("Activity 2", 45, new Date(), 6, 270),
                new ActivityDTO("Activity 3", 30, new Date(), 5, 150)
        );
        double expected = 127.27922061357856;

        // Act
        double result = calculService.ecartType(activityDTOList);

        // Assert
        assertEquals(expected, result, 0.001);
    }
}
