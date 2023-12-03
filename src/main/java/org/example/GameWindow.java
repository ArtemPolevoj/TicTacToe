package org.example;

import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame {
    private static final int WINDOW_HEIGHT = 555;
    private static final int WINDOW_WIDTH = 507;
    private static final int WINDOW_POSX = 500;
    private static final int WINDOW_POSY = 200;
    JButton btnStart = new JButton("New game");
    JButton btnExit = new JButton("EXIT");
    Map map;
    SettingsWindow settings;

    GameWindow(){
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocation(WINDOW_POSX, WINDOW_POSY);

        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setTitle("TIC TAC TOE");
        setResizable(false);

        map = new Map();
        settings = new SettingsWindow(this);

        btnStart.addActionListener(e -> settings.setVisible(true));
        btnExit.addActionListener(e -> System.exit(0));

        JPanel panButton = new JPanel(new GridLayout(1,2));
        panButton.add(btnStart);
        panButton.add(btnExit);
        add(panButton, BorderLayout.SOUTH);

         add(map);

        setVisible(true);
    }
   void startNewGame(int mode, int fSzX, int fSzY, int wLen){
        map.startNewGame(mode, fSzX, fSzY, wLen);

    }
}
