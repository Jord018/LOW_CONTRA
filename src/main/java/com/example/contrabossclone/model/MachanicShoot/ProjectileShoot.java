package com.example.contrabossclone.model.MachanicShoot;

import com.example.contrabossclone.model.Player;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class ProjectileShoot implements ShootingStrategy {
    private double gravity = 0.3; // Gravity effect for arc trajectory
    
    @Override
    public List<Bullet> shoot(double x, double y, Player player, double screenWidth, double screenHeight) {
        List<Bullet> bullets = new ArrayList<>();
        
        // Calculate direction to player
        double dx = player.getX() - x;
        double dy = player.getY() - y;
        double distance = Math.sqrt(dx * dx + dy * dy);
        
        // Normalize direction
        double dirX = dx / distance;
        double dirY = dy / distance;
        
        // Projectile speed
        double speed = 8;
        
        // Launch at an upward angle to create arc trajectory
        // Add upward velocity component for parabolic motion
        double launchAngle = -30; // Launch 30 degrees upward from direction to player
        double angleRad = Math.atan2(dirY, dirX) + Math.toRadians(launchAngle);
        
        double velocityX = Math.cos(angleRad) * speed;
        double velocityY = Math.sin(angleRad) * speed;
        
        // Create projectile bullet with custom size for visibility
        ProjectileBullet projectile = new ProjectileBullet(x, y, velocityX, velocityY, 
                                                           Color.ORANGE, 12, 12, 
                                                           screenWidth, screenHeight, gravity);
        bullets.add(projectile);
        
        return bullets;
    }
    
    // Special bullet class with gravity effect for projectile motion
    private static class ProjectileBullet extends Bullet {
        private double gravity;
        private double velX;
        private double velY;
        
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
            // Apply gravity to vertical velocity for parabolic arc
            velY += gravity; // Gravity increases downward velocity
            
            // Update position
            setX(getX() + velX);
            setY(getY() + velY);
        }
    }
}
