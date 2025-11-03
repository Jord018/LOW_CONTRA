package com.example.contrabossclone.model.MachanicShoot;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class CircleBullet extends Bullet {
    public CircleBullet(double x, double y, double velocityX, double velocityY,
                          Color color, double size, double screenWidth, double screenHeight) {
        super(x, y, velocityX, velocityY, color, size, size, screenWidth, screenHeight);
    }

    @Override
    public void render(GraphicsContext gc) {
        if (isFinished()) return;
        gc.setFill(getColor() != null ? getColor() : Color.MAGENTA);
        gc.fillOval(getX(), getY(), getWidth(), getHeight());
    }
}
