package com.example.contrabossclone.controller;

import com.example.contrabossclone.model.GameModel;
import com.example.contrabossclone.model.Player;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

import java.util.HashSet;
import java.util.Set;

public class GameController {

    private GameModel model;
    private Set<KeyCode> activeKeys = new HashSet<>();

    public GameController(GameModel model, Scene scene) {
        this.model = model;

        scene.setOnKeyPressed(e -> activeKeys.add(e.getCode()));
        scene.setOnKeyReleased(e -> activeKeys.remove(e.getCode()));
    }

    public void handleInput() {
        if (model.isGameOver()) return;

        Player player = model.getPlayer();

        if (activeKeys.contains(KeyCode.LEFT)) {
            player.moveLeft();
        } else if (activeKeys.contains(KeyCode.RIGHT)) {
            player.moveRight();
        } else {
            player.stop();
        }

        if (activeKeys.contains(KeyCode.SPACE)) {
            player.jump();
        }

        if (activeKeys.contains(KeyCode.J)) {
            if (player.canShoot()) {
                model.getPlayerBullets().addAll(player.shoot());
            }
        }

        if (activeKeys.contains(KeyCode.DOWN)) {
            player.setPressingDown(true);
        } else {
            player.setPressingDown(false);
        }
    }
}
