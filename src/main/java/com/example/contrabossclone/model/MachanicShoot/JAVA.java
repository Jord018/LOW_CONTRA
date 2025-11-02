package com.example.contrabossclone.model.MachanicShoot;

import com.example.contrabossclone.model.Player;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class JAVA implements ShootingStrategy {

    private final Random random = new Random();

    @Override
    public List<Bullet> shoot(double x, double y, Player player,
                              double screenWidth, double screenHeight, double bulletSpeed) {

        List<Bullet> bullets = new ArrayList<>();

        double angleDeg = 90 + (random.nextDouble() * 180 - 90);
        double angle = Math.toRadians(angleDeg);

        double vx = Math.cos(angle) * bulletSpeed;
        double vy = Math.sin(angle) * bulletSpeed;

        Bullet bullet = new FloatingBullet(
                x, y, vx, vy, Color.ORANGE, screenWidth, screenHeight
        );
        bullets.add(bullet);

        return bullets;
    }

    /**
     * Bullet แบบลอยสุ่มวนไปเรื่อย ๆ (Floating / Wobble motion)
     */
    public static class FloatingBullet extends Bullet {
        private final Random rand = new Random();
        private double angle;
        private double speed;
        private double changeTimer = 0;
        private double targetAngle;
        private double rotationSpeed = Math.toRadians(1.5);

        public FloatingBullet(double x, double y, double velocityX, double velocityY,
                              Color color, double screenWidth, double screenHeight) {
            super(x, y, velocityX, velocityY, color, screenWidth, screenHeight);
            this.speed = Math.sqrt(velocityX * velocityX + velocityY * velocityY);
            this.angle = Math.atan2(velocityY, velocityX);
            this.targetAngle = angle;
        }

        public void update(double deltaTime) {
            changeTimer += deltaTime;

            if (changeTimer > 0.5 + rand.nextDouble()) {
                changeTimer = 0;
                targetAngle = angle + Math.toRadians(rand.nextDouble() * 180 - 90);
            }

            double diff = targetAngle - angle;
            diff = Math.atan2(Math.sin(diff), Math.cos(diff));

            if (diff > rotationSpeed) diff = rotationSpeed;
            if (diff < -rotationSpeed) diff = -rotationSpeed;

            angle += diff;

            setVelocityX(Math.cos(angle) * speed);
            setVelocityY(Math.sin(angle) * speed);

            setX(getX() + getVelocityX() * deltaTime);
            setY(getY() + getVelocityY() * deltaTime);

            if (getX() < 0 || getX() > 1280) {
                setVelocityX(-getVelocityX());
                angle = Math.atan2(getVelocityY(), getVelocityX());
            }
            if (getY() < 0 || getY() > 720) {
                setVelocityY(-getVelocityY());
                angle = Math.atan2(getVelocityY(), getVelocityX());
            }
        }
    }
}
