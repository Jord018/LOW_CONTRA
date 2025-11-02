import com.example.contrabossclone.model.GameModel;
import com.example.contrabossclone.model.Player;
import com.example.contrabossclone.model.Stage.Level;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
    private Player player;
    private GameModel gameModel;

    // Test constants
    private static final int SCREEN_WIDTH = 900;
    private static final int SCREEN_HEIGHT = 600;
    private static final int PLAYER_START_X = 400;
    private static final int PLAYER_START_Y = 500;
    private static final int PLAYER_WIDTH = 40;
    private static final int PLAYER_HEIGHT = 60;
    private static final double PLAYER_SPEED = 2.0;
    
    @BeforeEach
    void setUp() {
        // Initialize game model and player
        gameModel = new GameModel(SCREEN_WIDTH, SCREEN_HEIGHT);
        player = gameModel.getPlayer();
        
        // Set player to a known position and state
        player.setX(PLAYER_START_X);
        player.setY(PLAYER_START_Y);
        player.setWidth(PLAYER_WIDTH);
        player.setHeight(PLAYER_HEIGHT);
        player.setSpeed(PLAYER_SPEED);
        player.setLives(3);
    }

/*    @Test
    void PlayerShouldDeath_WhenHealthIsZero() {
        player.setLives(0);
        assertTrue(player.isDefeated());
    }

 */
    @Test
    void PlayerShouldMoveRight_WhenMoveRightIsCalled() {
        // Act
        player.moveRight();
        gameModel.update();

        // Assert
        double expectedX = PLAYER_START_X + PLAYER_SPEED;
        assertEquals(expectedX, player.getX(), 0.001,
                String.format("Player should move right from %d to %f (speed: %f)",
                        PLAYER_START_X, expectedX, PLAYER_SPEED));
    }
    @Test
    void PlayerShouldMoveLeft_WhenMoveLeftIsCalled() {
        // Act
        player.moveLeft();
        gameModel.update();

        // Assert
        double expectedX = PLAYER_START_X - PLAYER_SPEED;
        assertEquals(expectedX, player.getX(), 0.001,
                String.format("Player should move left from %d to %f (speed: %f)",
                        PLAYER_START_X, expectedX, PLAYER_SPEED));
    }
    @Test
    void PlayerShouldGoProne_WhenPressingDown() {
        // Arrange - Get initial dimensions and position
        double initialHeight = player.getHeight();
        double initialY = player.getY();
        
        // Act - Set player to prone (crouch) position
        player.setPressingDown(true);
        
        // Assert - Check if player height is reduced
        double proneHeight = 30.0; // PRONE_HEIGHT from Player class
        assertEquals(proneHeight, player.getHeight(), 0.001,
            "Player height should be reduced when prone");
            
        // Assert - Check if player position is adjusted correctly
        double expectedY = initialY + (initialHeight - proneHeight);
        assertEquals(expectedY, player.getY(), 0.001,
            "Player Y position should be adjusted when going prone");
            
        // Act - Return to standing position
        player.setPressingDown(false);
        
        // Assert - Check if player returns to normal height and position
        assertEquals(initialHeight, player.getHeight(), 0.001,
            "Player should return to normal height when standing");
        assertEquals(initialY, player.getY(), 0.001,
            "Player should return to original Y position when standing");
    }
    
    @Test
    void PlayerShouldJump_WhenJumpIsCalled() {
        // Arrange - Ensure player is on ground
        player.setOnGround(true);
        double initialY = player.getY();
        
        // Act - jump and update
        player.jump();
        
        // Assert - velocity should be set to jump strength
        double expectedVelocity = -7.0; // Matches the jump strength in Player class
        assertEquals(expectedVelocity, player.getVelocityY(), 0.001,
            "Player should have negative Y velocity when jumping");
        
        // Update to apply velocity to position
        gameModel.update();
        
        // Assert - player should have moved up
        assertTrue(player.getY() < initialY, 
            "Player should move up after jumping");
    }
    @Test
    void PlayerShouldShoot_WhenShootIsCalled() {
        Player player = gameModel.getPlayer();
        
        // Arrange: Player starts with ability to shoot
        assertTrue(player.canShoot(), "Player should be able to shoot initially");
        
        // Act: Player shoots
        var bullets = player.shoot(900, 600);
        
        // Assert: Bullets are created and cooldown is set
        assertNotNull(bullets, "Bullets list should not be null");
        assertEquals(1, bullets.size(), "Normal weapon should create 1 bullet");
        assertFalse(player.canShoot(), "Player should not be able to shoot immediately after (cooldown active)");
        
        // Test different weapon types
        player.setWeaponType(Player.WeaponType.SPREAD_GUN);
        // Wait for cooldown to reset
        for (int i = 0; i < 30; i++) {
            player.update(gameModel.getLevels().get(0).getPlatforms(), 600);
        }
        assertTrue(player.canShoot(), "Player should be able to shoot after cooldown");
        
        bullets = player.shoot(900, 600);
        assertEquals(3, bullets.size(), "Spread gun should create 3 bullets");
    }
    /*
    @Test
    void testScoreIncreasesAndLevelAdvancesWhenBossDefeated() {
        Player player = gameModel.getPlayer();
        int oldScore = player.getScore();
        int oldLevelIndex = gameModel.getCurrentLevelIndex();

        // Make all bosses in current level defeated
        Level currentLevel = gameModel.getLevels().get(oldLevelIndex);
        currentLevel.getBosses().clear(); // No bosses left

        // Update game state
        gameModel.update();

        // Verify score increased
        assertEquals(oldScore + 1, player.getScore(), "Player score should increase when bosses are all defeated");

        // Verify level advances (if not last level)
        if (oldLevelIndex < gameModel.getLevels().size() - 1) {
            assertEquals(oldLevelIndex + 1, gameModel.getCurrentLevelIndex(), "Game should advance to next level");
            assertFalse(gameModel.isGameOver(), "Game should not be over yet");
        } else {
            assertTrue(gameModel.isGameOver(), "Game should be over after last level");
            assertTrue(gameModel.getGameOverMessage().contains("You Win!"), "Should show win message");
        }
    }

    @Test
    void testPlayerTakesDamage() {
        int initialHealth = player.getLives();
        player.hit();
        assertEquals(initialHealth - 1, player.getLives(), "Player health should decrease when taking damage");
    }

    @Test
    void testPlayerDiesWhenHealthReachesZero() {
        player.setLives(0);
        assertTrue(player.isDefeated(), "Player should be defeated when health reaches zero");
    }



    @Test
    void testPlayerRespawns() {
        player.hit();
        assertFalse(player.isDefeated(), "Player should still have lives left");
        assertEquals(2, player.getLives(), "Player should lose one life when health reaches zero");
    }
    */

    @Test
    void testPlayerFacingDirection() {
        // Initially facing right
        assertTrue(player.isFacingRight(), "Player should face right by default");
        
        // Change direction
        player.moveLeft();
        assertFalse(player.isFacingRight(), "Player should face left after moving left");
        
        player.moveRight();
        assertTrue(player.isFacingRight(), "Player should face right after moving right");
    }

    @Test
    void testPlayerWeaponUpgrade() {
        // Test default weapon
        assertEquals(Player.WeaponType.NORMAL, player.getWeaponType(), "Player should start with normal weapon");
        
        // Upgrade to spread gun
        player.setWeaponType(Player.WeaponType.SPREAD_GUN);
        assertEquals(Player.WeaponType.SPREAD_GUN, player.getWeaponType(), "Weapon type should be updated to SPREAD_GUN");
    }

    /*
    @Test
    void testPlayerAimAngle() {
        // Test initial aim angle (facing right)
        assertEquals(0, player.getAimAngle(), "Default aim angle should be 0 (right)");
        
        // Test setting aim angle
        player.setAimAngle(45);
        assertEquals(45, player.getAimAngle(), "Aim angle should be updated to 45 degrees");
    }

     */

    @Test
    void testPlayerGroundCollision() {
        // Place player in the air
        player.setY(100);
        player.setVelocityY(5);
        
        // Simulate gravity
        player.update(gameModel.getLevels().get(0).getPlatforms(), 600);
        
        // Player should fall and eventually hit the ground
        for (int i = 0; i < 100; i++) {
            player.update(gameModel.getLevels().get(0).getPlatforms(), 600);
            if (player.isOnGround()) break;
        }
        
        assertTrue(player.isOnGround(), "Player should be on ground after falling");
        assertEquals(540, player.getY(), 0.1, "Player should be at ground level");
    }

}
