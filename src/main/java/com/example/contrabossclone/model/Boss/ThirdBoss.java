package com.example.contrabossclone.model.Boss;

import com.example.contrabossclone.model.MachanicShoot.Bullet;
import com.example.contrabossclone.model.MachanicShoot.ShootingStrategy;
import com.example.contrabossclone.model.Player;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.List;

public class ThirdBoss extends Boss{


    public ThirdBoss(double x, double y,double width, double height, Player player, ShootingStrategy shootingStrategy, String spriteSheetPath, int score) {
        super(x, y, width, height, player, shootingStrategy, score);

        try {
            this.spriteSheet = new Image(getClass().getResourceAsStream(spriteSheetPath));
        } catch (Exception e) {
            System.err.println("!!! Error loading ThirdBoss sprite: " + spriteSheetPath);
            this.spriteSheet = null;
        }

        initializeAnimations();
    }

    protected void initializeAnimations() {
        this.animations = new HashMap<>();

        animations.put("IDLE", new Rectangle2D[]{
                new Rectangle2D(0, 0, 411, 411),
                new Rectangle2D(411, 0, 411, 411),
                new Rectangle2D(0, 411, 411, 411),
                new Rectangle2D(411, 411, 411, 411),
                new Rectangle2D(0, 822, 411, 411),
                new Rectangle2D(411, 822, 411, 411)
        });

        animations.put("DEATH", new Rectangle2D[]{
                new Rectangle2D(411, 411, 411, 411),
                new Rectangle2D(411, 411, 411, 411),
                new Rectangle2D(411, 411, 411, 411),
                new Rectangle2D(411, 411, 411, 411),
                new Rectangle2D(411, 411, 411, 411),
                new Rectangle2D(411, 411, 411, 411)
        });

        this.currentState = "IDLE";
    }

    public void update() {
        super.update();
    }
    public List<Bullet> shoot(double screenWidth, double screenHeight) {
        return super.shoot(screenWidth, screenHeight);
    }

}
