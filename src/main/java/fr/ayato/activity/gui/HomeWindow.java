package fr.ayato.activity.gui;

import javax.swing.*;
import java.awt.*;

public class HomeWindow extends JFrame {
    public HomeWindow(){
        super("Fenêtre d'accueil");
        Toolkit tk = Toolkit.getDefaultToolkit();
        int screenHeightSize = tk.getScreenSize().height;
        int screenWidthSize = tk.getScreenSize().width;

        setBounds(0,0, screenWidthSize/2, screenHeightSize/2);
        setLocationRelativeTo(null);

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        Dimension buttonDimension = new Dimension(200, 50);

        JButton buttonUser = new JButton("Créer un utilisateur");
        buttonUser.setPreferredSize(buttonDimension);
        buttonUser.addActionListener(e -> {
            new CreateUserWindow();
            dispose();
        });
        contentPane.add(buttonUser, BorderLayout.NORTH);

        JButton buttonListActivities = new JButton("Lister les activités");
        buttonListActivities.setPreferredSize(buttonDimension);
        buttonListActivities.addActionListener(e -> {
            new ActivityListWindow();
            dispose();
        });
        contentPane.add(buttonListActivities, BorderLayout.CENTER);

        JButton buttonCreateActivity = new JButton("Créer une activité");
        buttonCreateActivity.setPreferredSize(buttonDimension);
        buttonCreateActivity.addActionListener(e -> {
            new CreateActivityWindow();
            dispose();
        });
        contentPane.add(buttonCreateActivity, BorderLayout.SOUTH);

        pack();
        setVisible(true);
    }

}
