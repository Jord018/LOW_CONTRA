package com.example.contrabossclone.model;



import java.util.ArrayList;
import java.util.List;

public class GameModel {

    private Player player;
    private List<Level> levels = new ArrayList<>();
    private int currentLevelIndex = 0;

    private List<Bullet> playerBullets = new ArrayList<>();
    private List<Bullet> bossBullets = new ArrayList<>();

    private boolean gameOver = false;
    private String gameOverMessage = "";

    public GameModel() {
        player = new Player(400 - 25, 600 - 50);

        // Level 1
        List<Platform> level1Platforms = new ArrayList<>();
        level1Platforms.add(new Platform(150, 600 - 150, 100, 20));
        level1Platforms.add(new Platform(800 - 250, 600 - 150, 100, 20));
        List<PowerUp> level1PowerUps = new ArrayList<>();
        level1PowerUps.add(new PowerUp(100, 300, PowerUp.PowerUpType.MACHINE_GUN));
        level1PowerUps.add(new PowerUp(200, 300, PowerUp.PowerUpType.BARRIER));
        level1PowerUps.add(new PowerUp(300, 300, PowerUp.PowerUpType.SPREAD_GUN));
        level1PowerUps.add(new PowerUp(400, 300, PowerUp.PowerUpType.LASER));
        level1PowerUps.add(new PowerUp(500, 300, PowerUp.PowerUpType.FIRE));
        List<Boss> level1Bosses = new ArrayList<>();
        level1Bosses.add(new Boss(800 - 120, 600 - 120, player));
        level1Bosses.add(new Boss(800 - 240, 600 - 120, player));
        level1Bosses.add(new Boss(800 - 360, 600 - 120, player));
        levels.add(new Level(level1Bosses, level1Platforms, level1PowerUps, "/level1_bg.jpg"));

        // Level 2
        List<Platform> level2Platforms = new ArrayList<>();
        level2Platforms.add(new Platform(100, 600 - 100, 100, 20));
        level2Platforms.add(new Platform(800 - 200, 600 - 100, 100, 20));
        level2Platforms.add(new Platform(350, 600 - 200, 100, 20));
        List<PowerUp> level2PowerUps = new ArrayList<>();
        List<Boss> level2Bosses = new ArrayList<>();
        level2Bosses.add(new SecondBoss(800 - 120, 600 - 120, player));
        levels.add(new Level(level2Bosses, level2Platforms, level2PowerUps, "/level2_bg.png"));
    }

    public void update() {
        Level currentLevel = levels.get(currentLevelIndex);
        player.update(currentLevel.getPlatforms());
        for (Boss boss : currentLevel.getBosses()) {
            boss.update();
        }

        // Boss shooting
        for (Boss boss : currentLevel.getBosses()) {
            Bullet bossBullet = boss.shoot();
            if (bossBullet != null) {
                bossBullets.add(bossBullet);
            }
        }

        // Update player bullets
        List<Bullet> playerBulletsToRemove = new ArrayList<>();
        for (Bullet bullet : playerBullets) {
            bullet.update();
            if (bullet.isOutOfBounds(800, 600)) {
                playerBulletsToRemove.add(bullet);
            }
        }
        playerBullets.removeAll(playerBulletsToRemove);

        // Update boss bullets
        List<Bullet> bossBulletsToRemove = new ArrayList<>();
        for (Bullet bullet : bossBullets) {
            bullet.update();
            if (bullet.isOutOfBounds(800, 600)) {
                bossBulletsToRemove.add(bullet);
            }
        }
        bossBullets.removeAll(bossBulletsToRemove);

        // Collision detection: player bullets vs boss
        for (Bullet bullet : playerBullets) {
            for (Boss boss : currentLevel.getBosses()) {
                if (bullet.getBounds().intersects(boss.getBounds())) {
                    boss.hit();
                    playerBulletsToRemove.add(bullet);
                }
            }
        }
        playerBullets.removeAll(playerBulletsToRemove);

        // Collision detection: boss bullets vs player
        for (Bullet bullet : bossBullets) {
            if (bullet.getBounds().intersects(player.getBounds())) {
                player.hit();
                bossBulletsToRemove.add(bullet);
            }
        }
        bossBullets.removeAll(bossBulletsToRemove);

        // Collision detection: player vs power-ups
        List<PowerUp> powerUpsToRemove = new ArrayList<>();
        for (PowerUp powerUp : currentLevel.getPowerUps()) {
            if (player.getBounds().intersects(powerUp.getBounds())) {
                switch (powerUp.getType()) {
                    case BARRIER:
                        player.activateBarrier();
                        break;
                    case MACHINE_GUN:
                        player.setWeaponType(Player.WeaponType.MACHINE_GUN);
                        break;
                    case SPREAD_GUN:
                        player.setWeaponType(Player.WeaponType.SPREAD_GUN);
                        break;
                    case LASER:
                        player.setWeaponType(Player.WeaponType.LASER);
                        break;
                    case FIRE:
                        player.setWeaponType(Player.WeaponType.FIRE);
                        break;
                }
                powerUpsToRemove.add(powerUp);
            }
        }
        currentLevel.getPowerUps().removeAll(powerUpsToRemove);

        // Remove defeated bosses
        currentLevel.getBosses().removeIf(Boss::isDefeated);

        if (currentLevel.getBosses().isEmpty()) {
            if (currentLevelIndex < levels.size() - 1) {
                currentLevelIndex++;
                player.setX(400 - 25);
                player.setY(600 - 50);
            } else {
                gameOver = true;
                gameOverMessage = "You Win!";
            }
        }

        if (player.isDefeated()) {
            gameOver = true;
            gameOverMessage = "Game Over";
        }
    }

    public Player getPlayer() {
        return player;
    }

    public List<Level> getLevels() {
        return levels;
    }

    public int getCurrentLevelIndex() {
        return currentLevelIndex;
    }

    public List<Bullet> getPlayerBullets() {
        return playerBullets;
    }

    public List<Bullet> getBossBullets() {
        return bossBullets;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public String getGameOverMessage() {
        return gameOverMessage;
    }

}
