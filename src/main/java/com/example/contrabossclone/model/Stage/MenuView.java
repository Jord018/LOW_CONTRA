package com.example.contrabossclone.model.Stage;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class MenuView extends StackPane {

    public MenuView(Runnable onPlayClicked) {

        Image bgImage = null;
        try {
            bgImage = new Image(getClass().getResourceAsStream("/GameAssets/Menu.png"));
        } catch (Exception e) {
            System.err.println("Error");
        }

        ImageView bgView = new ImageView(bgImage);
        bgView.setPreserveRatio(true);
        bgView.fitWidthProperty().bind(this.widthProperty());
        bgView.fitHeightProperty().bind(this.heightProperty());

        Text playSelectText = new Text("PLAY SELECT");
        playSelectText.setFont(Font.font("Press Start 2P", 20));
        playSelectText.setFill(Color.WHITE);

        Text cursorText = new Text("► ");
        cursorText.setFont(Font.font("Press Start 2P", 20));
        cursorText.setFill(Color.WHITE);

        Text playText = new Text("PLAY");
        playText.setFont(Font.font("Press Start 2P", 20));
        playText.setFill(Color.WHITE);

        HBox playOption = new HBox(cursorText, playText);
        playOption.setAlignment(Pos.CENTER_LEFT);
        playOption.setPadding(new Insets(0, 0, 0, 100)); // (ดัน ►PLAY มาทางขวาหน่อย)

        VBox menuBox = new VBox(20); // (ระยะห่าง 20px)
        menuBox.getChildren().addAll(playSelectText, playOption);
        menuBox.setAlignment(Pos.CENTER_LEFT);
        menuBox.setPadding(new Insets(0, 0, 0, 200)); // (ดัน VBox ทั้งหมดมาทางขวา)

        playOption.setOnMouseClicked(event -> {
            System.out.println("PLAY clicked!");
            onPlayClicked.run();
        });

        playOption.setOnMouseEntered(e -> cursorText.setFill(Color.ORANGE));
        playOption.setOnMouseExited(e -> cursorText.setFill(Color.WHITE));

        this.getChildren().addAll(bgView, menuBox);
        this.setStyle("-fx-background-color: black;");
    }
}
