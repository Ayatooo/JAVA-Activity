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
import java.util.Date;

@Slf4j
public class Window extends JFrame {
    public Window(){
        super("Petite fenêtre bien sympa");
        Toolkit tk = Toolkit.getDefaultToolkit();
        int screenHeightSize = tk.getScreenSize().height;
        int screenWidthSize = tk.getScreenSize().width;

        setBounds(0,0, screenWidthSize/2, screenHeightSize/2);
        setLocationRelativeTo(null);

        Container contentPane = getContentPane();

        ActivityForm activityForm = new ActivityForm();
        activityForm.getTextFieldDuration().setText("Coucou");
        for (Effort effort : Effort.values()) {
            activityForm.getComboBoxRpe().addItem(effort);
        }
        activityForm.getValiderButton().setActionCommand("Valider");
        activityForm.getValiderButton().addActionListener(new ButtonFormListner());
        activityForm.getFermerButton().setActionCommand("Fermer");
        activityForm.getFermerButton().addActionListener(new ButtonListner());


        contentPane.add(activityForm.getRootPanel(), BorderLayout.CENTER);


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        public ButtonFormListner() {
            this.collection = Connection.client(this.dotenv.get("DB_NAME"), this.dotenv.get("DB_COLLECTION"));
            this.activityRepository = new ActivityRepositoryImpl(this.collection);
            this.activityController = new ActivityControllerImpl(this.activityRepository);
        }
        public void actionPerformed(ActionEvent e) {
            ActivityDTO activity = new ActivityDTO(
                    "rommmmm",
                    60,
                    new Date(),
                    8,
                    5
            );
            String id = activityController.saveActivity(activity);
            log.info("Actvity created", id);
        }
    }

    public static void main(String[] args) {
        JFrame window = new Window();
    }
}
