package com.example.contrabossclone.controller;

import com.example.contrabossclone.model.GameModel;
import com.example.contrabossclone.model.Player;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GameController {

    private GameModel model;
    private Set<KeyCode> activeKeys = new HashSet<>();
    private List<KeyCode> contraSequence = new ArrayList<>();
    private final KeyCode[] CONTRA_CODE = {
            KeyCode.UP, KeyCode.UP,
            KeyCode.DOWN, KeyCode.DOWN,
            KeyCode.LEFT, KeyCode.RIGHT,
            KeyCode.LEFT, KeyCode.RIGHT,
            KeyCode.B, KeyCode.A
    };
    public GameController(GameModel model, Scene scene) {
        this.model = model;

        scene.setOnKeyPressed(e -> {
            activeKeys.add(e.getCode());
            checkContraCode(e.getCode());
        });

        scene.setOnKeyReleased(e -> activeKeys.remove(e.getCode()));
    }
    private void checkContraCode(KeyCode key) {
        // Add the new key to the sequence
        contraSequence.add(key);

        // Remove the oldest key if we exceed the sequence length
        if (contraSequence.size() > CONTRA_CODE.length) {
            contraSequence.remove(0);
        }

        // Only check when we have enough keys
        if (contraSequence.size() == CONTRA_CODE.length) {
            boolean matched = true;
            for (int i = 0; i < CONTRA_CODE.length; i++) {
                if (contraSequence.get(i) != CONTRA_CODE[i]) {
                    matched = false;
                    break;
                }
            }

            if (matched) {
                activateContraCheat();
                contraSequence.clear();
            }
        }
    }
    private void activateContraCheat() {
        System.out.println("CONTRA CHEAT ACTIVATED!");
        model.getPlayer().setInvincible(true); // ตัวอย่างเพิ่มชีวิต

    }
    public void handleInput() {
        if (model.isGameOver()) return;

        Player player = model.getPlayer();

        boolean up = activeKeys.contains(KeyCode.W);
        boolean down = activeKeys.contains(KeyCode.S);
        boolean left = activeKeys.contains(KeyCode.A);
        boolean right = activeKeys.contains(KeyCode.D);

        if (left) {
            player.moveLeft();
        } else if (right) {
            player.moveRight();
        } else {
            player.stop();
        }

        if (up && !left && !right) {
            player.setAimAngle(90);
        } else if (up && right) {
            player.setAimAngle(45);
        } else if (right && !up && !down) {
            player.setAimAngle(0);
        } else if (right && down) {
            player.setAimAngle(315);
        } else if (down && left) {
            player.setAimAngle(225);
        } else if (left && !up && !down) {
            player.setAimAngle(180);
        } else if (left && up) {
            player.setAimAngle(135);
        } else {
            player.setAimAngle(player.isFacingRight() ? 0 : 180);
        }

        if (activeKeys.contains(KeyCode.K)) {
            player.jump();
        }

        if (activeKeys.contains(KeyCode.J)) {
            if (player.canShoot()) {
                model.getPlayerBullets().addAll(player.shoot(model.getWidth(), model.getHeight()));
            }
        }

        if (down && player.isOnGround()) {
            player.setPressingDown(true);
        } else {
            player.setPressingDown(false);
        }
    }
}

