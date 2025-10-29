package com.example.contrabossclone.model.MachanicShoot;

import com.example.contrabossclone.model.Player;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class AimShoot implements ShootingStrategy {
    @Override
    // 1. เพิ่ม screenWidth และ screenHeight ตรงนี้
    public List<Bullet> shoot(double x, double y, Player player, double screenWidth, double screenHeight) {
        List<Bullet> bullets = new ArrayList<>();
        double angle = Math.toDegrees(Math.atan2(player.getY() - y, player.getX() - x));
        double bulletSpeed = 5;
        double velocityX = Math.cos(Math.toRadians(angle)) * bulletSpeed;
        double velocityY = Math.sin(Math.toRadians(angle)) * bulletSpeed;

        // 2. เพิ่ม screenWidth และ screenHeight ตอนสร้าง Bullet
        bullets.add(new Bullet(x, y, velocityX, velocityY, Color.RED, screenWidth, screenHeight));
        return bullets;
    }
}