package fr.ayato.activity.gui;

import com.mongodb.client.MongoCollection;
import fr.ayato.activity.controller.ActivityControllerImpl;
import fr.ayato.activity.controller.UserController;
import fr.ayato.activity.controller.UserControllerImpl;
import fr.ayato.activity.model.UserDTO;
import fr.ayato.activity.repository.ActivityRepositoryImpl;
import fr.ayato.activity.Connection;
import fr.ayato.activity.repository.UserRepositoryImpl;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

@Slf4j
public class WindowCreateUser extends JFrame {
    public WindowCreateUser(){
        super("Petite fenêtre bien sympa");
        Toolkit tk = Toolkit.getDefaultToolkit();
        int screenHeightSize = tk.getScreenSize().height;
        int screenWidthSize = tk.getScreenSize().width;

        setBounds(0,0, screenWidthSize/2, screenHeightSize/2);
        setLocationRelativeTo(null);

        Container contentPane = getContentPane();

        UserForm userForm = new UserForm();
        userForm.getCreateButton().addActionListener(new ButtonListener());

        JPanel panel = new JPanel();
        panel.add(userForm.getUserPanel());
        contentPane.add(panel, BorderLayout.CENTER);


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    class ButtonListener implements ActionListener {
        Dotenv dotenv = Dotenv.configure().load();
        UserController userController;
        UserRepositoryImpl userRepository;
        MongoCollection<Document> collection;
        UserForm userForm;
        private String name;
        private String firstname;
        private Date birthdate;
        public ButtonListener() {
            this.collection = Connection.client(this.dotenv.get("DB_NAME"), this.dotenv.get("DB_COLLECTION_USER"));
            this.userRepository = new UserRepositoryImpl(this.collection);
            this.userController = new UserControllerImpl(this.userRepository);
            this.userForm = new UserForm();
        }
        public void actionPerformed(ActionEvent e) {
            this.name = userForm.getTextFieldName().getText();
            this.firstname = userForm.getTextFieldFirstName().getText();
            this.birthdate = new Date();

            UserDTO user = new UserDTO(
                    this.name,
                    this.firstname,
                    this.birthdate,
                    "homme"
            );
            String id = userController.saveUser(user);
            log.info("User created: {}", id);
        }
    }

    public static void main(String[] args) {
        JFrame window = new WindowCreateUser();
    }
}
