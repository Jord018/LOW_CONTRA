package com.example.contrabossclone.model.MachanicShoot;

import com.example.contrabossclone.model.Player;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class DirectShoot implements ShootingStrategy {
    @Override
    public List<Bullet> shoot(double x, double y, Player player, double screenWidth, double screenHeight) { // <--- เพิ่มพารามิเตอร์
        List<Bullet> bullets = new ArrayList<>();
        double bulletSpeed = 5;
        // Shoot straight to the right for simplicity, can be modified
        double velocityX = bulletSpeed;
        double velocityY = 0;
        bullets.add(new Bullet(x, y, velocityX, velocityY, Color.ORANGE, screenWidth, screenHeight));
        return bullets;
    }
}
