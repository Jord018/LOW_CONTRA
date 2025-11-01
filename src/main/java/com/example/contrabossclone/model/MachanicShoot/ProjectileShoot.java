package com.example.contrabossclone.model.MachanicShoot;

import com.example.contrabossclone.model.Player;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class ProjectileShoot implements ShootingStrategy {
    private double gravity = 0.3; // Gravity effect for arc trajectory
    
    @Override
    public List<Bullet> shoot(double x, double y, Player player, double screenWidth, double screenHeight, double bulletSpeed) {
        List<Bullet> bullets = new ArrayList<>();
        
        // Fixed velocity to move straight to the left, using the provided bulletSpeed
        // Negative speed for left movement
        double velocityX = -Math.abs(bulletSpeed); 
        double velocityY = 0;  // No vertical movement
        
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
