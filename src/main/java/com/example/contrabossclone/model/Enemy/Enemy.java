package com.example.contrabossclone.model.Enemy;

import com.example.contrabossclone.model.Player;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Enemy  {
    double x, y;
    double width, height;
    Player player;
    double speed = 2.0;
    public Enemy(double x, double y, Player player) {
        this.x = x;
        this.y = y;
        this.width = 50;
        this.height = 50;
        this.player = player;
    }
    public void render(GraphicsContext g) {
        g.setFill(Color.RED);
        g.fillRect((int)x, (int)y, (int)width, (int)height);
    }
    public void update() {
        move();
    }
    public void move() {
        if (player == null) return;

        // Calculate direction to player
        double dx = player.getX() - x;
        double dy = player.getY() - y;
        double distance = Math.sqrt(dx * dx + dy * dy);

        // Normalize and move toward player
        if (distance > 0) {
            x += (dx / distance) * speed;
            y += (dy / distance) * speed;
        }
    }

}
