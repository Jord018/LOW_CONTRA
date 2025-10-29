package com.example.contrabossclone.model;



import java.util.ArrayList;
import java.util.List;

public class GameModel {

    private Player player;
    private List<Level> levels = new ArrayList<>();
    private int currentLevelIndex = 0;
    private int currentStage = 1; // Track current stage (1-3)

    private List<Bullet> playerBullets = new ArrayList<>();
    private List<Bullet> bossBullets = new ArrayList<>();

    private boolean gameOver = false;
    private String gameOverMessage = "";

    private double width;
    private double height;

    public GameModel(double width, double height) {
        this.width = width;
        this.height = height;
        player = new Player(width / 2 - 25, height - 50);

        // Initialize all stages
        initializeStage3();
    }

    /**
     * Stage 1: Tutorial stage with multiple weak bosses and all power-ups available
     */
    private void initializeStage1() {
        List<Platform> platforms = new ArrayList<>();
        platforms.add(new Platform(150, height - 150, 100, 20));
        platforms.add(new Platform(width - 250, height - 150, 100, 20));
        
        List<PowerUp> powerUps = new ArrayList<>();
        powerUps.add(new PowerUp(100, 300, PowerUp.PowerUpType.MACHINE_GUN));
        powerUps.add(new PowerUp(200, 300, PowerUp.PowerUpType.BARRIER));
        powerUps.add(new PowerUp(300, 300, PowerUp.PowerUpType.SPREAD_GUN));
        powerUps.add(new PowerUp(400, 300, PowerUp.PowerUpType.LASER));
        powerUps.add(new PowerUp(500, 300, PowerUp.PowerUpType.FIRE));
        
        List<Boss> bosses = new ArrayList<>();
        bosses.add(new Boss(width - 120, height - 120, player, new AimShoot()));
        bosses.add(new Boss(width - 240, height - 120, player, new DirectShoot()));
        bosses.add(new Boss(width - 360, height - 120, player, new AimShoot()));
        
        levels.add(new Level(bosses, platforms, powerUps, "/level1_bg.jpg"));
    }

    /**
     * Stage 2: Intermediate stage with stronger boss and more platforms
     */
    private void initializeStage2() {
        List<Platform> platforms = new ArrayList<>();
        platforms.add(new Platform(100, height - 100, 100, 20));
        platforms.add(new Platform(width - 200, height - 100, 100, 20));
        platforms.add(new Platform(350, height - 200, 100, 20));
        
        List<PowerUp> powerUps = new ArrayList<>();
        // No power-ups in stage 2 - player must rely on what they collected in stage 1
        
        List<Boss> bosses = new ArrayList<>();
        bosses.add(new SecondBoss(width - 120, height - 120, player, new AimShoot()));
        
        levels.add(new Level(bosses, platforms, powerUps, "/level2_bg.png"));
    }

    /**
     * Stage 3: Final stage with challenging boss configuration
     */
    private void initializeStage3() {
        List<PowerUp> powerUps = new ArrayList<>();
        List<Platform> platforms = new ArrayList<>();
        
        List<Boss> bosses = new ArrayList<>();
        
        levels.add(new Level(bosses, platforms, powerUps, "/level3_bg.png"));
    }

    public void update() {
        Level currentLevel = levels.get(currentLevelIndex);
        player.update(currentLevel.getPlatforms(), height);
        for (Boss boss : currentLevel.getBosses()) {
            boss.update();
        }

        // Boss shooting
        for (Boss boss : currentLevel.getBosses()) {
            List<Bullet> newBossBullets = boss.shoot(width, height); // <--- เพิ่ม width, height
            if (newBossBullets != null && !newBossBullets.isEmpty()) {
                bossBullets.addAll(newBossBullets);
            }
        }

        // Update player bullets
        List<Bullet> playerBulletsToRemove = new ArrayList<>();
        for (Bullet bullet : playerBullets) {
            bullet.update();
            if (bullet.isOutOfBounds(width, height)) {
                playerBulletsToRemove.add(bullet);
            }
        }
        playerBullets.removeAll(playerBulletsToRemove);

        // Update boss bullets
        List<Bullet> bossBulletsToRemove = new ArrayList<>();
        for (Bullet bullet : bossBullets) {
            bullet.update();
            if (bullet.isOutOfBounds(width, height)) {
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
/*
        if (currentLevel.getBosses().isEmpty()) {
            if (currentLevelIndex < levels.size() - 1) {
                currentLevelIndex++;
                currentStage = currentLevelIndex + 1; // Update stage number
                player.setX(width / 2 - 25);
                player.setY(height - 50);
            } else {
                gameOver = true;
                gameOverMessage = "You Win! All 3 Stages Completed!";
            }
        }
*/
        if (player.getLives() <= 0) {
            gameOver = true;
            gameOverMessage = "Game Over";
        }
    }

    public void resize(double newWidth, double newHeight) {
        double scaleX = newWidth / this.width;
        double scaleY = newHeight / this.height;

        player.setX(player.getX() * scaleX);
        player.setY(player.getY() * scaleY);
        player.setRespawnPosition(newWidth / 2 - 25, newHeight - 50);

        // Adjust positions of existing bullets (optional, can also clear and re-add)
        for (Bullet bullet : playerBullets) {
            bullet.setX(bullet.getX() * scaleX);
            bullet.setY(bullet.getY() * scaleY);
        }
        for (Bullet bullet : bossBullets) {
            bullet.setX(bullet.getX() * scaleX);
            bullet.setY(bullet.getY() * scaleY);
        }

        // Adjust positions of platforms, bosses, powerups in current level
        Level currentLevel = levels.get(currentLevelIndex);
        for (Platform platform : currentLevel.getPlatforms()) {
            platform.setX(platform.getX() * scaleX);
            platform.setY(platform.getY() * scaleY);
        }
        for (Boss boss : currentLevel.getBosses()) {
            boss.setX(boss.getX() * scaleX);
            boss.setY(boss.getY() * scaleY);
        }
        for (PowerUp powerUp : currentLevel.getPowerUps()) {
            powerUp.setX(powerUp.getX() * scaleX);
            powerUp.setY(powerUp.getY() * scaleY);
        }

        this.width = newWidth;
        this.height = newHeight;
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

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    /**
     * Returns the current stage number (1-3)
     */
    public int getCurrentStage() {
        return currentStage;
    }
}
