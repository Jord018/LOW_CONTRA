package com.example.contrabossclone.model;

import java.util.List;

public interface ShootingStrategy {
    List<Bullet> shoot(double x, double y, Player player, double screenWidth, double screenHeight); // <--- เพิ่มพารามิเตอร์
}
