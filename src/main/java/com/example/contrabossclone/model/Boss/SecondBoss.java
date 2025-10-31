package com.example.contrabossclone.model.Boss;

import com.example.contrabossclone.model.MachanicShoot.ShootingStrategy;
import com.example.contrabossclone.model.Player;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public class SecondBoss extends Boss {

    public SecondBoss(double x, double y, double width, double height,Player player, ShootingStrategy shootingStrategy) {
        super(x, y, width, height,player, shootingStrategy);
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
