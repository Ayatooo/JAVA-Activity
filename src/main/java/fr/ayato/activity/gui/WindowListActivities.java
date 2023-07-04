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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static fr.ayato.activity.mapper.ActivityMapper.documentToActivity;

@Slf4j
public class WindowListActivities extends JFrame {

    Dotenv dotenv = Dotenv.configure().load();
    ActivityControllerImpl activityController;
    ActivityRepositoryImpl activityRepository;
    MongoCollection<Document> collection;
    boolean ascendingOrder = true;

    public WindowListActivities() {
        super("Liste des activités");
        this.collection = Connection.client(this.dotenv.get("DB_NAME"), this.dotenv.get("DB_COLLECTION_ACT"));
        this.activityRepository = new ActivityRepositoryImpl(this.collection);
        this.activityController = new ActivityControllerImpl(this.activityRepository);

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
        addButton.addActionListener(e ->
        {
            dispose();
        	new WindowCreateActivity();
        });

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(refreshButton);
        buttonPanel.add(sortButton);
        buttonPanel.add(addButton);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        refreshActivityList(textArea);

        setVisible(true);
    }

    private void refreshActivityList(JTextArea textArea) {
        this.collection = Connection.client(this.dotenv.get("DB_NAME"), this.dotenv.get("DB_COLLECTION_ACT"));
        this.activityRepository = new ActivityRepositoryImpl(this.collection);
        this.activityController = new ActivityControllerImpl(this.activityRepository);
        MongoCollection<Document> activities = activityController.getAll();
        List<ActivityDTO> activityDTOList = new ArrayList<>();
        try {
            for (Document activity : activities.find()) {
                activityDTOList.add(documentToActivity(activity));
            }
        } catch (Exception e) {
        }
        textArea.setText("");
        activityDTOList.forEach(activityDTO -> textArea.append(
                "Activité : " + activityDTO.getName() + "\n"
                        + "Durée : " + activityDTO.getDuration() + "\n"
                        + "RPE : " + activityDTO.getRpe() + "\n"
                        + "Charge : " + activityDTO.getCharge() + "\n"
                        + "Date : " + activityDTO.getDate() + "\n"
                        + "------------------------------------------------------\n"
        ));
    }

    private void sortActivitiesByDate(JTextArea textArea) {
        this.collection = Connection.client(this.dotenv.get("DB_NAME"), this.dotenv.get("DB_COLLECTION_ACT"));
        this.activityRepository = new ActivityRepositoryImpl(this.collection);
        this.activityController = new ActivityControllerImpl(this.activityRepository);
        MongoCollection<Document> activities = activityController.getAll();
        List<ActivityDTO> activityDTOList = new ArrayList<>();
        try {
            for (Document activity : activities.find()) {
                activityDTOList.add(documentToActivity(activity));
            }
        } catch (Exception e) {
        }

        // Trier par date
        if (ascendingOrder) {
            Collections.sort(activityDTOList, Comparator.comparing(ActivityDTO::getDate));
        } else {
            Collections.sort(activityDTOList, Comparator.comparing(ActivityDTO::getDate).reversed());
        }

        textArea.setText("");
        activityDTOList.forEach(activityDTO -> textArea.append(
                "Activité : " + activityDTO.getName() + "\n"
                        + "Durée : " + activityDTO.getDuration() + "\n"
                        + "RPE : " + activityDTO.getRpe() + "\n"
                        + "Charge : " + activityDTO.getCharge() + "\n"
                        + "Date : " + activityDTO.getDate() + "\n"
                        + "------------------------------------------------------\n"
        ));

        ascendingOrder = !ascendingOrder; // Inverser l'ordre de tri
    }

    public static void main(String[] args) {
        JFrame window = new WindowListActivities();
    }
}
