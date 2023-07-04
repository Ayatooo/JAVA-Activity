package fr.ayato.activity.gui;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;

@Getter
@Setter
public class UserForm {
    private JPanel userPanel;
    private JTextField textFieldName;
    private JTextField textFieldFirstName;
    private JTextField textFieldDate;
    private JRadioButton hommeRadioButton;
    private JRadioButton femmeRadioButton;
    private JButton createButton;
    private ButtonGroup buttonGroupRadioButton;
}
