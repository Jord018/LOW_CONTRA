package com.example.contrabossclone.model.MachanicShoot;

import com.example.contrabossclone.model.Player;

import java.util.List;

public interface ShootingStrategy {
    List<Bullet> shoot(double x, double y, Player player, double screenWidth, double screenHeight, double bulletSpeed);
}
