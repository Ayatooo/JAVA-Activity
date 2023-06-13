import fr.ayato.activity.controller.ActivityControllerImpl;
import fr.ayato.activity.model.ActivityDTO;
import fr.ayato.activity.repository.ActivityRepositoryImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Date;
import static com.mongodb.assertions.Assertions.fail;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Group of units tests for the activity controller")
public class ActivityControllerImplTest {

    @Mock
    ActivityRepositoryImpl activityRepository;
    ActivityDTO squat = new ActivityDTO(
            "Squat",
            120,
            new Date(),
            5,
            5
    );
    ActivityDTO deadlift = new ActivityDTO(
            "Deadlift",
            120,
            new Date(),
            6,
            7
    );
    String id = "idSquat";

    ActivityControllerImpl classUnderTest;

    @BeforeEach
    void setUp() {
        classUnderTest = new ActivityControllerImpl(activityRepository);
    }

    @Disabled
    @Test
    @DisplayName("Test an action")
    void voidAction_isTested_shouldFail () {
        fail("Not yet implemented");
    }

    @Test
    @DisplayName("Test if the save method of the repository is called with an activity")
    void saveActivity_isCalled_shouldCallSaveActivity () {
        //Arrange
        when(activityRepository.save(squat)).thenReturn(id);

        //Act
        String result = classUnderTest.saveActivity(squat);

        //Assert
        verify(activityRepository).save(squat);
        verify(activityRepository).save(any(ActivityDTO.class));
        verify(activityRepository, times(1))
                .save(any(ActivityDTO.class));
        verify(activityRepository, never()).getAll();
        assertThat(result).isEqualTo(id);
    }
}
