package com.example.contrabossclone.model.MachanicShoot;

import com.example.contrabossclone.model.Player;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class ProjectileShoot implements ShootingStrategy {
    private double gravity = 0.3; // Gravity effect for arc trajectory

    private Image spriteSheet;
    private Rectangle2D spriteFrame;

    public ProjectileShoot(Image spriteSheet, Rectangle2D spriteFrame) {
        this.spriteSheet = spriteSheet;
        this.spriteFrame = spriteFrame;
    }

    public ProjectileShoot() {
        this.spriteSheet = null;
        this.spriteFrame = null;
    }

    @Override
    public List<Bullet> shoot(double x, double y, Player player, double screenWidth, double screenHeight, double bulletSpeed) {
        List<Bullet> bullets = new ArrayList<>();

        double velocityX = -Math.abs(bulletSpeed);
        double velocityY = 0;

        ProjectileBullet projectile = new ProjectileBullet(x, y, velocityX, velocityY,
                spriteSheet, spriteFrame, // ⭐️ ส่ง Sprite
                12, 12, // ⭐️ ขนาดกระสุน
                screenWidth, screenHeight, gravity);
        bullets.add(projectile);

        return bullets;
    }

    private static class ProjectileBullet extends Bullet {
        private double gravity;
        private double velX;
        private double velY;

        public ProjectileBullet(double x, double y, double velocityX, double velocityY,
                                Image sheet, Rectangle2D frame,
                                double width, double height,
                                double screenWidth, double screenHeight, double gravity) {
            super(x, y, velocityX, velocityY, sheet, frame, width, height, screenWidth, screenHeight);
            this.gravity = gravity;
            this.velX = velocityX;
            this.velY = velocityY;

            if (sheet == null || frame == null) {
                System.err.println("Fallback: ProjectileBullet using Color");
            }
        }

        public ProjectileBullet(double x, double y, double velocityX, double velocityY,
                                Color color, double width, double height,
                                double screenWidth, double screenHeight, double gravity) {
            super(x, y, velocityX, velocityY, color, width, height, screenWidth, screenHeight);
            this.gravity = gravity;
            this.velX = velocityX;
            this.velY = velocityY;
        }

        @Override
        public void update() {
            velY += gravity;
            setX(getX() + velX);
            setY(getY() + velY);
        }
    }
}

