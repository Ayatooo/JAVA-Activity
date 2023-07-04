package fr.ayato.activity.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Home extends JFrame {
    public Home(){
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
        buttonUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new WindowCreateUser();
                dispose();  // Ferme la fenêtre Home
            }
        });
        contentPane.add(buttonUser, BorderLayout.NORTH);

        JButton buttonListActivities = new JButton("Lister les activités");
        buttonListActivities.setPreferredSize(buttonDimension);
        buttonListActivities.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new WindowListActivities();
                dispose();  // Ferme la fenêtre Home
            }
        });
        contentPane.add(buttonListActivities, BorderLayout.CENTER);

        JButton buttonCreateActivity = new JButton("Créer une activité");
        buttonCreateActivity.setPreferredSize(buttonDimension);
        buttonCreateActivity.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new WindowCreateActivity();
                dispose();  // Ferme la fenêtre Home
            }
        });
        contentPane.add(buttonCreateActivity, BorderLayout.SOUTH);

        pack();
        setVisible(true);
    }

    public static void main(String[] args) {
        JFrame window = new Home();
    }
}
