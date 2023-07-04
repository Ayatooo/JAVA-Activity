package fr.ayato.activity.gui;

import com.mongodb.client.MongoCollection;
import fr.ayato.activity.Connection;
import fr.ayato.activity.controller.ActivityControllerImpl;
import fr.ayato.activity.enums.Effort;
import fr.ayato.activity.model.ActivityDTO;
import fr.ayato.activity.repository.ActivityRepositoryImpl;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

@Slf4j
public class CreateActivityWindow extends JFrame {
    public CreateActivityWindow(){
        super("Création d'un entrainement");
        Toolkit tk = Toolkit.getDefaultToolkit();
        int screenHeightSize = tk.getScreenSize().height;
        int screenWidthSize = tk.getScreenSize().width;

        setBounds(0,0, screenWidthSize/2, screenHeightSize/2);
        setLocationRelativeTo(null);

        Container contentPane = getContentPane();

        ActivityForm activityForm = new ActivityForm();
        for (Effort effort : Effort.values()) {
            activityForm.getComboBoxRpe().addItem(effort);
        }
        activityForm.getValiderButton().setActionCommand("Valider");
        activityForm.getValiderButton().addActionListener(new ButtonFormListener(activityForm, this));
        activityForm.getFermerButton().setActionCommand("Fermer");
        activityForm.getFermerButton().addActionListener(new ButtonListener());

        contentPane.add(activityForm.getRootPanel(), BorderLayout.CENTER);

        JButton buttonBack = new JButton("Retour");
        buttonBack.setPreferredSize(new Dimension(200, 50));
        buttonBack.addActionListener(e -> {
            new HomeWindow();
            dispose();
        });
        contentPane.add(buttonBack, BorderLayout.SOUTH);

        setVisible(true);
    }

    static class ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if(e.getActionCommand().equals("Fermer")){
                System.exit(0);
            }
        }
    }

    static class ButtonFormListener implements ActionListener {
        Dotenv dotenv = Dotenv.configure().load();
        ActivityControllerImpl activityController;
        ActivityRepositoryImpl activityRepository;
        MongoCollection<Document> collection;
        ActivityForm activityForm;
        JFrame frame;
        public ButtonFormListener(ActivityForm activityForm, JFrame frame) {
            this.collection = Connection.client(this.dotenv.get("DB_NAME"), this.dotenv.get("DB_COLLECTION_ACT"));
            this.activityRepository = new ActivityRepositoryImpl(this.collection);
            this.activityController = new ActivityControllerImpl(this.activityRepository);
            this.activityForm = activityForm;
            this.frame = frame;
        }
        public void actionPerformed(ActionEvent e) {
            String name = this.activityForm.getTextFieldName().getText();
            int duration = (int) this.activityForm.getSpinnerDuration().getValue();
            String dateString = this.activityForm.getTextFieldDate().getText();
            Date date;
            try {
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate localDate = LocalDate.parse(dateString, dateFormatter);
                date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                log.info("Date valide : " + date);
            } catch (DateTimeParseException exception) {
                log.error("Date invalide", exception);
                return;
            }
            int rpe = this.activityForm.getComboBoxRpe().getSelectedIndex();
            int marge = rpe * duration;

            ActivityDTO activity = new ActivityDTO(
                    name,
                    duration,
                    date,
                    rpe,
                    marge
            );
            String id = activityController.saveActivity(activity);
            log.info("Activity created : " + id);
            JDialog dialog = new JDialog();
            JPanel panel = new JPanel();
            JLabel label = new JLabel("Activité créée avec succès");
            panel.add(label);
            panel.add(new Buttons().Close(dialog, frame));
            dialog.add(panel);
            dialog.setSize(200, 100);
            dialog.setVisible(true);
        }
    }

    static class ButtonDialogListener implements ActionListener {
        JDialog dialog;
        JFrame window;
        public ButtonDialogListener(JDialog dialog, JFrame window) {
            this.dialog = dialog;
            this.window = window;
        }
        public void actionPerformed(ActionEvent e) {
            if(e.getActionCommand().equals("Fermer")){
                this.window.dispose();
                this.dialog.dispose();
                new CreateActivityWindow();
            }
        }
    }
}
