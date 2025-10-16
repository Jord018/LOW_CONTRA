package com.example.contrabossclone.model;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class AimShoot implements ShootingStrategy {
    @Override
    public List<Bullet> shoot(double x, double y, Player player) {
        List<Bullet> bullets = new ArrayList<>();
        double angle = Math.toDegrees(Math.atan2(player.getY() - y, player.getX() - x));
        double bulletSpeed = 5;
        double velocityX = Math.cos(Math.toRadians(angle)) * bulletSpeed;
        double velocityY = Math.sin(Math.toRadians(angle)) * bulletSpeed;
        bullets.add(new Bullet(x, y, velocityX, velocityY, Color.RED));
        return bullets;
    }
}
