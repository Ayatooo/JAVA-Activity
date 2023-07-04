package fr.ayato.activity.gui;

import com.mongodb.client.MongoCollection;
import fr.ayato.activity.controller.UserController;
import fr.ayato.activity.controller.UserControllerImpl;
import fr.ayato.activity.model.UserDTO;
import fr.ayato.activity.Connection;
import fr.ayato.activity.repository.UserRepositoryImpl;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class WindowCreateUser extends JFrame {
    UserForm userForm;


    public WindowCreateUser(){
        super("Petite fenêtre bien sympa");
        Toolkit tk = Toolkit.getDefaultToolkit();
        int screenHeightSize = tk.getScreenSize().height;
        int screenWidthSize = tk.getScreenSize().width;

        setBounds(0,0, screenWidthSize/2, screenHeightSize/2);
        setLocationRelativeTo(null);

        Container contentPane = getContentPane();

        this.userForm = new UserForm();
        userForm.getCreateButton().addActionListener(new ButtonListener(userForm, this));
        userForm.getHommeRadioButton().addItemListener(new RadioButtonListener(userForm));
        userForm.getFemmeRadioButton().addItemListener(new RadioButtonListener(userForm));

        JPanel panel = new JPanel();
        panel.add(userForm.getUserPanel());
        contentPane.add(panel, BorderLayout.CENTER);

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


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    class ButtonListener implements ActionListener {
        Dotenv dotenv = Dotenv.configure().load();
        UserController userController;
        UserRepositoryImpl userRepository;
        MongoCollection<Document> collection;
        private String name;
        private String firstname;
        private String sexe;
        private Date birthdate;
        UserForm userForm;
        JFrame frame;

        public ButtonListener(UserForm userForm, JFrame frame) {
            this.collection = Connection.client(this.dotenv.get("DB_NAME"), this.dotenv.get("DB_COLLECTION_USER"));
            this.userRepository = new UserRepositoryImpl(this.collection);
            this.userController = new UserControllerImpl(this.userRepository);
            this.userForm = userForm;
            this.frame = frame;
        }
        public void actionPerformed(ActionEvent e) {
            this.name = this.userForm.getTextFieldName().getText();
            this.firstname = this.userForm.getTextFieldFirstName().getText();
            try {
                this.birthdate = new SimpleDateFormat("dd/MM/yyyy").parse(this.userForm.getTextFieldDate().getText());
            } catch (ParseException ex) {
                throw new RuntimeException("Error during date conversion", ex);
            }
            if (this.userForm.getHommeRadioButton().isSelected() && this.userForm.getFemmeRadioButton().isSelected()){
                JOptionPane optionPane = new JOptionPane("Veuillez ne selectionner qu'un seul sexe",JOptionPane.WARNING_MESSAGE);
                JDialog dialog = optionPane.createDialog("Warning!");
                dialog.setAlwaysOnTop(true); // to show top of all other application
                dialog.setVisible(true);
            } else if (this.userForm.getHommeRadioButton().isSelected()) {
                this.sexe = "Homme";
            } else if (this.userForm.getFemmeRadioButton().isSelected()){
                this.sexe = "Femme";
            }
            UserDTO user = new UserDTO(
                    this.name,
                    this.firstname,
                    this.birthdate,
                    this.sexe
            );
            String id = userController.saveUser(user);
            log.info("User created: {}", id);

            JDialog dialog = new JDialog();
            JPanel panel = new JPanel();
            JLabel label = new JLabel("User créé avec succès");
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
                JFrame window = new WindowCreateUser();
            }
        }
    }

    class RadioButtonListener implements ItemListener {
        UserForm userForm;
        JRadioButton hommeRadioButton;
        JRadioButton femmeRadioButton;
        public RadioButtonListener(UserForm userForm) {
            this.userForm = userForm;
            this.hommeRadioButton = this.userForm.getHommeRadioButton();
            this.femmeRadioButton = this.userForm.getFemmeRadioButton();
        }
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                JRadioButton selectedRadioButton = (JRadioButton) e.getSource();

                if (selectedRadioButton == hommeRadioButton) {
                    femmeRadioButton.setSelected(false);
                } else if (selectedRadioButton == femmeRadioButton) {
                    hommeRadioButton.setSelected(false);
                }
            }
        }
    }


    public static void main(String[] args) {
        JFrame window = new WindowCreateUser();
    }
}
