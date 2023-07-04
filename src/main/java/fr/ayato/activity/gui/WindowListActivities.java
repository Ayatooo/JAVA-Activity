package fr.ayato.activity.gui;

import com.mongodb.client.MongoCollection;
import fr.ayato.activity.Connection;
import fr.ayato.activity.controller.ActivityControllerImpl;
import fr.ayato.activity.model.ActivityDTO;
import fr.ayato.activity.repository.ActivityRepositoryImpl;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.*;
import java.util.List;

import static fr.ayato.activity.mapper.ActivityMapper.documentToActivity;

@Slf4j
public class WindowListActivities extends JFrame {

    Dotenv dotenv = Dotenv.configure().load();
    private final ActivityControllerImpl activityController;
    private final ActivityRepositoryImpl activityRepository;
    private final MongoCollection<Document> collection;
    private boolean ascendingOrder = true;

    public WindowListActivities() {
        super("Liste des activités");
        this.collection = Connection.client(this.dotenv.get("DB_NAME"), this.dotenv.get("DB_COLLECTION_ACT"));
        this.activityRepository = new ActivityRepositoryImpl(collection);
        this.activityController = new ActivityControllerImpl(activityRepository);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setLocationRelativeTo(null);

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        JButton refreshButton = new JButton("Actualiser");
        refreshButton.addActionListener(e -> refreshActivityList(textArea));


        JButton buttonBack = new JButton("Retour");
        buttonBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Home();
                dispose();
            }
        });
        contentPane.add(buttonBack, BorderLayout.SOUTH);
        JButton sortButton = new JButton("Trier par date");
        sortButton.addActionListener(e -> sortActivitiesByDate(textArea));

        JButton addButton = new JButton("Créer");
        addButton.addActionListener(e -> {
            dispose();
            new WindowCreateActivity();
        });

        JButton filterButton = new JButton("Filtrer par semaine");
        filterButton.addActionListener(e -> filterActivitiesByWeek(textArea));

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(refreshButton);
        buttonPanel.add(sortButton);
        buttonPanel.add(addButton);
        buttonPanel.add(filterButton);
        buttonPanel.add(buttonBack);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        refreshActivityList(textArea);
        setVisible(true);
    }

    private void refreshActivityList(JTextArea textArea) {
        textArea.setText("");

        try {
            List<ActivityDTO> activityDTOList = getActivityList();
            displayActivities(textArea, activityDTOList);
        } catch (Exception e) {
            log.error("Failed to fetch activity list: {}", e.getMessage());
            JOptionPane.showMessageDialog(this, "Erreur lors de la récupération de la liste des activités.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private List<ActivityDTO> getActivityList() {
        MongoCollection<Document> activities = activityController.getAll();
        List<ActivityDTO> activityDTOList = new ArrayList<>();
        for (Document activity : activities.find()) {
            activityDTOList.add(documentToActivity(activity));
        }
        return activityDTOList;
    }

    private void displayActivities(JTextArea textArea, List<ActivityDTO> activityDTOList) {
        for (ActivityDTO activityDTO : activityDTOList) {
            textArea.append(
                    "Activité : " + activityDTO.getName() + "\n"
                            + "Durée : " + activityDTO.getDuration() + "\n"
                            + "RPE : " + activityDTO.getRpe() + "\n"
                            + "Charge : " + activityDTO.getCharge() + "\n"
                            + "Date : " + activityDTO.getDate() + "\n"
                            + "------------------------------------------------------\n"
            );
        }
    }

    private void sortActivitiesByDate(JTextArea textArea) {
        textArea.setText("");
        try {
            List<ActivityDTO> activityDTOList = getActivityList();
            sortActivitiesByDate(activityDTOList);
            displayActivities(textArea, activityDTOList);
        } catch (Exception e) {
            log.error("Failed to sort activity list: {}", e.getMessage());
            JOptionPane.showMessageDialog(this, "Erreur lors du tri de la liste des activités.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void sortActivitiesByDate(List<ActivityDTO> activityDTOList) {
        if (ascendingOrder) {
            Collections.sort(activityDTOList, Comparator.comparing(ActivityDTO::getDate));
        } else {
            Collections.sort(activityDTOList, Comparator.comparing(ActivityDTO::getDate).reversed());
        }
        ascendingOrder = !ascendingOrder;
    }

    private void filterActivitiesByWeek(JTextArea textArea) {
        try {
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);

            List<Integer> weekNumbers = new ArrayList<>();
            for (int i = 1; i <= 52; i++) {
                weekNumbers.add(i);
            }

            Integer selectedWeekNumber = (Integer) JOptionPane.showInputDialog(this,
                    "Sélectionnez le numéro de semaine :", "Filtrer par semaine",
                    JOptionPane.QUESTION_MESSAGE, null, weekNumbers.toArray(), weekNumbers.get(0));

            if (selectedWeekNumber != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, currentYear);
                calendar.set(Calendar.WEEK_OF_YEAR, selectedWeekNumber);
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                Date startDate = calendar.getTime();
                calendar.add(Calendar.DAY_OF_WEEK, 6);
                Date endDate = calendar.getTime();

                List<ActivityDTO> activityDTOList = getActivityList();
                List<ActivityDTO> filteredList = new ArrayList<>();

                for (ActivityDTO activityDTO : activityDTOList) {
                    Date activityDate = activityDTO.getDate();
                    if (!activityDate.before(startDate) && !activityDate.after(endDate)) {
                        filteredList.add(activityDTO);
                    }
                }

                textArea.setText("");
                if (filteredList.isEmpty()) {
                    textArea.setText("Aucune activité trouvée pour la semaine sélectionnée.");
                } else {
                    displayActivities(textArea, filteredList);

                    int totalLoad = calculateTotalLoad(filteredList);
                    double monotonie = calculateMonotony(filteredList);

                    textArea.append("\n");
                    textArea.append("Total Load: " + totalLoad + "\n");
                    textArea.append("Monotonie: " + monotonie + "\n");
                    textArea.append("calcule de charge d entrainement quotidien moyen : " + calculateAverageDailyTrainingLoad(filteredList) + "\n");
                }

            }
        } catch (Exception e) {
            log.error("Failed to filter activity list: {}", e.getMessage());
            JOptionPane.showMessageDialog(this, "Erreur lors du filtrage de la liste des activités.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
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

    private double calculateAverageDailyTrainingLoad(List<ActivityDTO> activityDTOList) {
        int totalCharge = 0;
        Set<Date> trainingDays = new HashSet<>();

        for (ActivityDTO activityDTO : activityDTOList) {
            int duration = activityDTO.getDuration();
            int rpe = activityDTO.getRpe();
            int charge = duration * rpe;

            totalCharge += charge;
            trainingDays.add(activityDTO.getDate());
        }

        int numberOfDays = getNumberOfDaysInWeek(activityDTOList);
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

    private double calculateMonotony(List<ActivityDTO> activityDTOList) {
        int totalDuration = 0;
        int totalCharge = 0;

        for (ActivityDTO activityDTO : activityDTOList) {
            int duration = activityDTO.getDuration();
            int rpe = activityDTO.getRpe();
            int charge = duration * rpe;

            totalDuration += duration;
            totalCharge += charge;
        }

            double monotony = totalCharge / Math.sqrt(totalDuration);

        return monotony;
    }

    private double calculateACWR(List<ActivityDTO> currentWeekActivities, List<ActivityDTO> previousFourWeeksActivities) {
        int currentWeekLoad = calculateTotalLoad(currentWeekActivities);
        double averageFourWeeksLoad = calculateAverageLoad(previousFourWeeksActivities);

        double acwr = currentWeekLoad / averageFourWeeksLoad;

        return acwr;
    }

    private int calculateTotalLoad(List<ActivityDTO> activityDTOList) {
        int totalLoad = 0;

        for (ActivityDTO activityDTO : activityDTOList) {
            int duration = activityDTO.getDuration();
            int rpe = activityDTO.getRpe();
            int load = duration * rpe;

            totalLoad += load;
        }

        return totalLoad;
    }

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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(WindowListActivities::new);
    }
}
