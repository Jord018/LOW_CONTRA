package com.example.contrabossclone.model.Stage;

import com.example.contrabossclone.model.Boss.Boss;
import com.example.contrabossclone.model.Enemy.Enemy;
import com.example.contrabossclone.model.Items.PowerUp;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.geometry.Rectangle2D;
import com.example.contrabossclone.util.ResourceLoader;
import java.util.List;

public class Level {

    private List<Boss> bosses;
    private List<Enemy> enemies;
    private List<Platform> platforms;
    private List<PowerUp> powerUps;
    private Image backgroundImage;

    private boolean doCrop;
    private double sourceX;
    private double sourceY;
    private double sourceWidth;
    private double sourceHeight;

    private double groundLevel;

    private double startX;
    private double startY;

    public Level(List<Boss> bosses,List<Enemy> enemies, List<Platform> platforms, List<PowerUp> powerUps, String backgroundImagePath,
                 double groundLevel, double startX, double startY) {
        this.bosses = bosses;
        this.enemies = enemies;
        this.platforms = platforms;
        this.powerUps = powerUps;
        this.backgroundImage = ResourceLoader.loadImage(backgroundImagePath);
        this.groundLevel = groundLevel;
        this.startX = startX;
        this.startY = startY;
        this.doCrop = false;
    }

    public Level(List<Boss> bosses, List<Enemy> enemies, List<Platform> platforms, List<PowerUp> powerUps, String backgroundImagePath,
                 double sourceX, double sourceY, double sourceWidth, double sourceHeight,
                 double groundLevel, double startX, double startY) {

        this(bosses, enemies, platforms, powerUps, backgroundImagePath, groundLevel, startX, startY);

        this.sourceX = sourceX;
        this.sourceY = sourceY;
        this.sourceWidth = sourceWidth;
        this.sourceHeight = sourceHeight;
        this.doCrop = true;
    }

    public void render(GraphicsContext gc) {
        if (backgroundImage != null) {

            if (doCrop) {
                gc.drawImage(backgroundImage,
                        sourceX, sourceY, sourceWidth, sourceHeight, // Source (ตัดจากตรงนี้)
                        0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight() // Destination (วาดลงตรงนี้)
                );
            } else {
                gc.drawImage(backgroundImage, 0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
            }

        } else {
            gc.setFill(Color.BLACK);
            gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        }

        for (Boss boss : bosses) {
            boss.render(gc);
        }

//        for (Platform platform : platforms) {
//            platform.render(gc);
//        }

        for (PowerUp powerUp : powerUps) {
            powerUp.render(gc);
        }
        for (Enemy enemy : enemies) {
            if (enemy.isAlive()) {
                enemy.render(gc);
            }
        }
    }

    public double getStartX() {
        return startX;
    }
    public double getStartY() {
        return startY;
    }

    public double getGroundLevel() {
        return groundLevel;
    }

    public List<Boss> getBosses() {
        return bosses;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public List<Platform> getPlatforms() {
        return platforms;
    }

    public List<PowerUp> getPowerUps() {
        return powerUps;
    }
}
