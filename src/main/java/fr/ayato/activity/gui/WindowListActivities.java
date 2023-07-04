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
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
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
        setSize(600, 400);
        setLocationRelativeTo(null);

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        JButton refreshButton = new JButton("Actualiser");
        refreshButton.addActionListener(e -> refreshActivityList(textArea));

        JButton sortButton = new JButton("Trier par date");
        sortButton.addActionListener(e -> sortActivitiesByDate(textArea));

        JButton addButton = new JButton("Créer");
        addButton.addActionListener(e -> {
            dispose();
            new WindowCreateActivity();
        });

        JButton filterButton = new JButton("Cette semaine");
        filterButton.addActionListener(e -> filterActivitiesByWeek(textArea));

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(refreshButton);
        buttonPanel.add(sortButton);
        buttonPanel.add(addButton);
        buttonPanel.add(filterButton);
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
            LocalDate currentDate = LocalDate.now();
            LocalDate startDate = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            LocalDate endDate = currentDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

            List<ActivityDTO> activityDTOList = getActivityList();
            List<ActivityDTO> filteredList = new ArrayList<>();

            Date startDateAsDate = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date endDateAsDate = Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

            for (ActivityDTO activityDTO : activityDTOList) {
                Date activityDate = activityDTO.getDate();
                if (activityDate.after(startDateAsDate) && activityDate.before(endDateAsDate)) {
                    filteredList.add(activityDTO);
                }
            }

            textArea.setText("");
            if (filteredList.isEmpty()) {
                textArea.setText("Aucune activité trouvée pour la semaine sélectionnée.");
            } else {
                displayActivities(textArea, filteredList);
            }
        } catch (Exception e) {
            log.error("Failed to filter activity list: {}", e.getMessage());
            JOptionPane.showMessageDialog(this, "Erreur lors du filtrage de la liste des activités.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(WindowListActivities::new);
    }
}
