package fr.ayato.activity.gui;

import fr.ayato.activity.enums.Effort;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {
    public Window(){
        super("Petite fenêtre bien sympa");
        Toolkit tk = Toolkit.getDefaultToolkit();
        int screenHeightSize = tk.getScreenSize().height;
        int screenWidthSize = tk.getScreenSize().width;

        setBounds(0,0, screenWidthSize/2, screenHeightSize/2);
        setLocationRelativeTo(null);

        Container contentPane = getContentPane();

        JPanel panelClose = new JPanel();
        JButton buttonClose = new JButton("Fermer");
        buttonClose.addActionListener(e -> System.exit(0));
        panelClose.add(buttonClose);
        panelClose.setSize(10,100);

        ActivityForm activityForm = new ActivityForm();
        activityForm.getTextFieldDuration().setText("Coucou");
        for (Effort effort : Effort.values()) {
            activityForm.getComboBoxRpe().addItem(effort);
        }


        contentPane.add(activityForm.getRootPanel(), BorderLayout.CENTER);
        contentPane.add(panelClose, BorderLayout.SOUTH);


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        JFrame window = new Window();
    }
}
