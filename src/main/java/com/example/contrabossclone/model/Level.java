package com.example.contrabossclone.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.List;

public class Level {

    private List<Boss> bosses;
    private List<Platform> platforms;
    private List<PowerUp> powerUps;
    private Image backgroundImage;

    public Level(List<Boss> bosses, List<Platform> platforms, List<PowerUp> powerUps, String backgroundImagePath) {
        this.bosses = bosses;
        this.platforms = platforms;
        this.powerUps = powerUps;
        try {
            this.backgroundImage = new Image(getClass().getResourceAsStream(backgroundImagePath));
        } catch (Exception e) {
            System.out.println("Error loading background image: " + backgroundImagePath);
            this.backgroundImage = null;
        }
    }

    public void render(GraphicsContext gc) {
        if (backgroundImage != null) {
            gc.drawImage(backgroundImage, 0, 0, 800, 600);
        } else {
            gc.setFill(Color.BLACK);
            gc.fillRect(0, 0, 800, 600);
        }

        for (Boss boss : bosses) {
            boss.render(gc);
        }
        for (Platform platform : platforms) {
            platform.render(gc);
        }
        for (PowerUp powerUp : powerUps) {
            powerUp.render(gc);
        }
    }

    public List<Boss> getBosses() {
        return bosses;
    }

    public List<Platform> getPlatforms() {
        return platforms;
    }

    public List<PowerUp> getPowerUps() {
        return powerUps;
    }
}
