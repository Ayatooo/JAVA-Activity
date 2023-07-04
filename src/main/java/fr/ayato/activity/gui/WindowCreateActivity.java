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
public class WindowCreateActivity extends JFrame {
    public WindowCreateActivity(){
        super("Petite fenêtre bien sympa");
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
        activityForm.getValiderButton().addActionListener(new ButtonFormListner(activityForm, this));
        activityForm.getFermerButton().setActionCommand("Fermer");
        activityForm.getFermerButton().addActionListener(new ButtonListner());

        contentPane.add(activityForm.getRootPanel(), BorderLayout.CENTER);

        JButton buttonBack = new JButton("Retour");
        buttonBack.setPreferredSize(new Dimension(200, 50));
        buttonBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Home();  // Ouvre la fenêtre Home
                dispose();   // Ferme la fenêtre WindowCreateUser
            }
        });
        contentPane.add(buttonBack, BorderLayout.SOUTH);

        setVisible(true);
    }

    class ButtonListner implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if(e.getActionCommand().equals("Fermer")){
                System.exit(0);
            }
        }
    }

    class ButtonFormListner implements ActionListener {
        Dotenv dotenv = Dotenv.configure().load();
        ActivityControllerImpl activityController;
        ActivityRepositoryImpl activityRepository;
        MongoCollection<Document> collection;
        ActivityForm activityForm;
        private String name;
        private int duration;
        private Date date;
        private int rpe;
        private int marge;
        JFrame frame;
        public ButtonFormListner(ActivityForm activityForm, JFrame frame) {
            this.collection = Connection.client(this.dotenv.get("DB_NAME"), this.dotenv.get("DB_COLLECTION_ACT"));
            this.activityRepository = new ActivityRepositoryImpl(this.collection);
            this.activityController = new ActivityControllerImpl(this.activityRepository);
            this.activityForm = activityForm;
            this.frame = frame;
        }
        public void actionPerformed(ActionEvent e) {
            this.name = this.activityForm.getTextFieldName().getText();
            this.duration = (int)this.activityForm.getSpinnerDuration().getValue();
            String dateString = this.activityForm.getTextFieldDate().getText();
            try {
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate localDate = LocalDate.parse(dateString, dateFormatter);
                this.date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                log.info("Date valide : " + this.date);
            } catch (DateTimeParseException exception) {
                log.error("Date invalide", exception);
                return;
            }
            this.rpe = this.activityForm.getComboBoxRpe().getSelectedIndex();
            this.marge = this.rpe * this.duration;

            ActivityDTO activity = new ActivityDTO(
                    this.name,
                    this.duration,
                    this.date,
                    this.rpe,
                    this.marge
            );
            String id = activityController.saveActivity(activity);
            log.info("Actvity created", id);
            JDialog dialog = new JDialog();
            JPanel panel = new JPanel();
            JLabel label = new JLabel("Activité créée avec succès");
            JButton button = new JButton("Fermer");
            button.setActionCommand("Fermer");
            button.addActionListener(new ButtonDialogListner(dialog, this.frame));
            panel.add(label);
            panel.add(button);
            dialog.add(panel);
            dialog.setSize(200, 100);
            dialog.setVisible(true);
        }
    }

    class ButtonDialogListner implements ActionListener {
        JDialog dialog;
        JFrame window;
        public ButtonDialogListner(JDialog dialog, JFrame window) {
            this.dialog = dialog;
            this.window = window;
        }
        public void actionPerformed(ActionEvent e) {
            if(e.getActionCommand().equals("Fermer")){
                this.window.dispose();
                this.dialog.dispose();
                JFrame window = new WindowCreateActivity();
            }
        }
    }

    public static void main(String[] args) {
        JFrame window = new WindowCreateActivity();
    }
}
