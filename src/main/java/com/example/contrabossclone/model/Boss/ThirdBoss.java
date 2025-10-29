package com.example.contrabossclone.model.Boss;

import com.example.contrabossclone.model.MachanicShoot.Bullet;
import com.example.contrabossclone.model.MachanicShoot.ShootingStrategy;
import com.example.contrabossclone.model.Player;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.List;

public class ThirdBoss extends Boss{
    public ThirdBoss(double x, double y, Player player, ShootingStrategy shootingStrategy) {
        super(x, y, player, shootingStrategy);
    }
    public void update() {
        super.update();
    }
    public List<Bullet> shoot(double screenWidth, double screenHeight) {
        return super.shoot(screenWidth, screenHeight);
    }
    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Color.web("#A9A9A9"));
        gc.fillRect(x, y + 20, getWidth(), getHeight() - 20);
    }
}
