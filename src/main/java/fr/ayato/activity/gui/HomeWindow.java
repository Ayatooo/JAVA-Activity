package fr.ayato.activity.gui;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class HomeWindow extends JFrame {
    public HomeWindow() {
        super("Fenêtre d'accueil");
        Toolkit tk = Toolkit.getDefaultToolkit();
        int screenHeightSize = tk.getScreenSize().height;
        int screenWidthSize = tk.getScreenSize().width;

        setBounds(0, 0, screenWidthSize / 2, screenHeightSize / 2);
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
        buttonUser.setFocusPainted(false);
        buttonUser.setBackground(new Color(63, 81, 181));
        buttonUser.setForeground(Color.WHITE);
        buttonUser.setFont(new Font("Arial", Font.BOLD, 14));
        buttonUser.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        buttonUser.setUI(new BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Shape shape = new RoundRectangle2D.Double(0, 0, c.getWidth(), c.getHeight(), 5, 5);
                g2.setColor(c.getBackground());
                g2.fill(shape);
                g2.setColor(c.getForeground());
                g2.draw(shape);
                g2.dispose();
                super.paint(g, c);
            }
        });
        contentPane.add(buttonUser, BorderLayout.NORTH);

        JButton buttonListActivities = new JButton("Lister les activités");
        buttonListActivities.setPreferredSize(buttonDimension);
        buttonListActivities.addActionListener(e -> {
            new ActivityListWindow();
            dispose();
        });
        buttonListActivities.setFocusPainted(false);
        buttonListActivities.setBackground(new Color(63, 81, 181));
        buttonListActivities.setForeground(Color.WHITE);
        buttonListActivities.setFont(new Font("Arial", Font.BOLD, 14));
        buttonListActivities.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        buttonListActivities.setUI(new BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Shape shape = new RoundRectangle2D.Double(0, 0, c.getWidth(), c.getHeight(), 5, 5);
                g2.setColor(c.getBackground());
                g2.fill(shape);
                g2.setColor(c.getForeground());
                g2.draw(shape);
                g2.dispose();
                super.paint(g, c);
            }
        });
        contentPane.add(buttonListActivities, BorderLayout.CENTER);

        JButton buttonCreateActivity = new JButton("Créer une activité");
        buttonCreateActivity.setPreferredSize(buttonDimension);
        buttonCreateActivity.addActionListener(e -> {
            new CreateActivityWindow();
            dispose();
        });
        buttonCreateActivity.setFocusPainted(false);
        buttonCreateActivity.setBackground(new Color(63, 81, 181));
        buttonCreateActivity.setForeground(Color.WHITE);
        buttonCreateActivity.setFont(new Font("Arial", Font.BOLD, 14));
        buttonCreateActivity.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        buttonCreateActivity.setUI(new BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Shape shape = new RoundRectangle2D.Double(0, 0, c.getWidth(), c.getHeight(), 5, 5);
                g2.setColor(c.getBackground());
                g2.fill(shape);
                g2.setColor(c.getForeground());
                g2.draw(shape);
                g2.dispose();
                super.paint(g, c);
            }
        });
        contentPane.add(buttonCreateActivity, BorderLayout.SOUTH);

        pack();
        setVisible(true);
    }
}
