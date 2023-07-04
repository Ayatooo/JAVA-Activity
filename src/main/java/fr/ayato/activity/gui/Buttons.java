package fr.ayato.activity.gui;

import javax.swing.*;

public class Buttons {

    public JButton Close(JDialog dialog, JFrame frame) {
        JButton button = new JButton("Fermer");
        button.setActionCommand("Fermer");
        button.addActionListener(new CreateActivityWindow.ButtonDialogListener(dialog, frame));
        return button;
    }
}
