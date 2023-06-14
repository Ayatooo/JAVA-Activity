package fr.ayato.activity.gui;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;

@Getter
@Setter
public class ActivityForm {
    private JButton validerButton;
    private JTextField textFieldDate;
    private JTextField textFieldName;
    private JTextField textFieldDuration;
    private JComboBox comboBoxRpe;
    private JPanel rootPanel;
    private JButton fermerButton;
}