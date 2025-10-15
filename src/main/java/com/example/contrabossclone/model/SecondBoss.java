package com.example.contrabossclone.model;

import com.example.contrabossclone.model.Player;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public class SecondBoss extends Boss {

    public SecondBoss(double x, double y, Player player) {
        super(x, y, player);
    }

    @Override
    public void render(GraphicsContext gc) {
        // A simple placeholder for the second boss
        gc.setFill(Color.PURPLE);
        gc.fillRect(getX(), getY(), getWidth(), getHeight());

        // Health bar
        gc.setFill(Color.WHITE);
        gc.fillRect(getX(), getY() - 20, getWidth(), 10);
        gc.setFill(Color.RED);
        gc.fillRect(getX(), getY() - 20, getWidth() * (getHealth() / 100.0), 10);
    }
}
