package com.example.contrabossclone.model.MachanicShoot;

import com.example.contrabossclone.model.Player;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color; // ⭐️ (เก็บไว้สำหรับ Fallback)

import java.util.ArrayList;
import java.util.List;

public class DirectShoot implements ShootingStrategy {

    private Image spriteSheet;
    private Rectangle2D spriteFrame;

    public DirectShoot(Image spriteSheet, Rectangle2D spriteFrame) {
        this.spriteSheet = spriteSheet;
        this.spriteFrame = spriteFrame;
    }

    public DirectShoot() {
        this.spriteSheet = null;
        this.spriteFrame = null;
    }

    @Override
    public List<Bullet> shoot(double x, double y, Player player, double screenWidth, double screenHeight, double bulletSpeed) {
        List<Bullet> bullets = new ArrayList<>();

        double angle = Math.atan2(player.getY() - y, player.getX() - x);
        double velocityX = Math.cos(angle) * bulletSpeed;
        double velocityY = Math.sin(angle) * bulletSpeed;

        double bulletWidth = 10;
        double bulletHeight = 10;

        if (spriteSheet != null && spriteFrame != null) {
            bullets.add(new Bullet(x, y, velocityX, velocityY,
                    spriteSheet, spriteFrame,
                    bulletWidth, bulletHeight,
                    screenWidth, screenHeight));
        } else {
            bullets.add(new Bullet(x, y, velocityX, velocityY,
                    Color.ORANGE,
                    bulletWidth, bulletHeight,
                    screenWidth, screenHeight));
        }

        return bullets;
    }
}

