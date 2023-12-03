package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class Map extends JPanel {
    private int cellWidth;
    private int cellHeight;
    private int fieldSizeX;
    private int fieldSizeY;
    private int mode;
    private int wLen;

    private final Random RANDOM = new Random();
    private final int HUMAN_DOT = 1;
    private final int AI_DOT = 2;
    private final int EMPTY_DOT = 0;
    private int[][] field;
    private static final int DOT_PADDING = 5;
    private static final String MSG_WIN_HUMAN = "Победил Игрок!";
    private static final String MSG_WIN_AI = "Победил Компьютер!";
    private static final String MSG_DRAW = "Ничья!";
    private int gameOverType;
    private final int STATE_DRAW = 0;
    private final int STATE_WIN_HUMAN = 1;
    private final int STATE_WIN_AI = 2;
    private boolean isGameOver;
    private boolean isInitialized;

    Map() {
        isInitialized = false;
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                update(e);
            }
        });
    }

    private void update(MouseEvent e) {
        if (isGameOver || !isInitialized) return;
        int cellX = e.getX() / cellWidth;
        int cellY = e.getY() / cellHeight;
        if (!isCellValid(cellX, cellY) || !isCellEmpty(cellX, cellY)) {
            return;
        }
        field[cellY][cellX] = HUMAN_DOT;
        if (checkEndGame(HUMAN_DOT, STATE_WIN_HUMAN)) return;
        aiTurn();
        if (checkEndGame(AI_DOT, STATE_WIN_AI)) return;
        repaint();
    }

    void startNewGame(int mode, int fSzX, int fSzY, int wLen) {
        this.mode= mode;
        this.fieldSizeX = fSzX;
        this.fieldSizeY = fSzY;
        this.wLen = wLen;
        initMap();
        isGameOver = false;
        isInitialized = true;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        render(g);
    }

    private void render(Graphics g) {
        if (!isInitialized) return;
        int panelHeight = getHeight();
        int panelWidth = getWidth();
        cellHeight = panelHeight / fieldSizeY;
        cellWidth = panelWidth / fieldSizeX;
        for (int h = 0; h < fieldSizeX; h++) {
            int y = h * cellHeight;
            g.drawLine(0, y, panelWidth, y);
        }
        for (int w = 0; w < fieldSizeY; w++) {
            int x = w * cellWidth;
            g.drawLine(x, 0, x, panelHeight);
        }

        for (int y = 0; y < fieldSizeY; y++) {
            for (int x = 0; x < fieldSizeX; x++) {
                if (field[y][x] == EMPTY_DOT) continue;

                if (field[y][x] == HUMAN_DOT) {
                    g.setColor(Color.BLUE);
                    g.fillOval(x * cellWidth + DOT_PADDING,
                            y * cellHeight + DOT_PADDING,
                            cellWidth - DOT_PADDING * 2,
                            cellHeight - DOT_PADDING * 2);
                } else if (field[y][x] == AI_DOT) {
                    g.setColor(Color.RED);
                    g.fillOval(x * cellWidth + DOT_PADDING,
                            y * cellHeight + DOT_PADDING,
                            cellWidth - DOT_PADDING * 2,
                            cellHeight - DOT_PADDING * 2);
                } else {
                    throw new RuntimeException("Unexpected value " + field[y][x] +
                            " in cell: x=" + x + " y=" + y);
                }
            }
        }
        if (isGameOver) showMessageGameOver(g);
        repaint();
    }

    private void initMap() {
        field = new int[fieldSizeY][fieldSizeX];
        for (int y = 0; y < fieldSizeY; y++) {
            for (int x = 0; x < fieldSizeX; x++) {
                field[y][x] = EMPTY_DOT;
            }
        }
    }

    private boolean isCellValid(int x, int y) {
        return x >= 0 && x < fieldSizeX && y >= 0 && y < fieldSizeY;
    }

    private boolean isCellEmpty(int x, int y) {
        return field[y][x] == EMPTY_DOT;
    }

    private void aiTurn() {
        int x;
        int y;
        do {
            x = RANDOM.nextInt(fieldSizeX);
            y = RANDOM.nextInt(fieldSizeY);
        }
        while (!isCellEmpty(x, y));
        field[y][x] = AI_DOT;
    }

    private boolean checkWin(int dot) {
        // Проверка по трем горизонталям
        if (field[0][0] == dot && field[0][1] == dot && field[0][2] == dot)
            return true;
        if (field[1][0] == dot && field[1][1] == dot && field[1][2] == dot)
            return true;
        if (field[2][0] == dot && field[2][1] == dot && field[2][2] == dot)
            return true;

        // Проверка по трем вертикалям
        if (field[0][0] == dot && field[1][0] == dot && field[2][0] == dot)
            return true;
        if (field[0][1] == dot && field[1][1] == dot && field[2][1] == dot)
            return true;
        if (field[0][2] == dot && field[1][2] == dot && field[2][2] == dot)
            return true;

        // Проверка по диагоналям
        if (field[0][0] == dot && field[1][1] == dot && field[2][2] == dot)
            return true;
        return field[0][2] == dot && field[1][1] == dot && field[2][0] == dot;
    }

    private boolean isMapFull() {
        for (int y = 0; y < fieldSizeY; y++) {
            for (int x = 0; x < fieldSizeX; x++) {
                if (isCellEmpty(x, y))
                    return false;
            }
        }
        return true;
    }

    private boolean checkEndGame(int dot, int gameOverType) {
        if (checkWin(dot)) {
            this.gameOverType = gameOverType;
            isGameOver = true;
            repaint();
            return true;
        }
        if (isMapFull()) {
            this.gameOverType = STATE_DRAW;
            isGameOver = true;
            repaint();
            return true;
        }
        return false;
    }

    private void showMessageGameOver(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 200, getWidth(), 70);
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Times new roman", Font.BOLD, 48));
        switch (gameOverType) {
            case STATE_DRAW:
                g.drawString(MSG_DRAW, 180, getHeight() / 2);
                break;
            case STATE_WIN_AI:
                g.drawString(MSG_WIN_AI, 20, getHeight() / 2);
                break;
            case STATE_WIN_HUMAN:
                g.drawString(MSG_WIN_HUMAN, 70, getHeight() / 2);
                break;
            default:
                throw new RuntimeException("Unexpected gameOver state: " + gameOverType);
        }
    }
}
