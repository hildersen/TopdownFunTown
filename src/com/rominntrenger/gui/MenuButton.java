package com.rominntrenger.gui;

import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class MenuButton extends StackPane {
    private Text text;

    public MenuButton(String name) {
        text = new Text(name);
        text.setFont(text.getFont().font(20));
        text.setFill(Color.WHITE);

        Rectangle bg = new Rectangle(250, 30);
        bg.setOpacity(0.6);
        bg.setFill(Color.BLACK);
        bg.setEffect(new GaussianBlur(3.5));

        setAlignment(Pos.CENTER_LEFT);
        setRotate(-0.5);
        getChildren().addAll(bg, text);

        setOnMouseEntered(event -> {
            bg.setTranslateX(10);
            text.setTranslateX(10);
            bg.setFill(Color.WHITE);
            text.setFill(Color.BLACK);
        });

        setOnMouseExited(event -> {
            bg.setTranslateX(0);
            text.setTranslateX(0);
            bg.setFill(Color.BLACK);
            text.setFill(Color.WHITE);
        });

        DropShadow drop = new DropShadow(50, Color.WHITE);
        drop.setInput(new Glow());

        setOnMousePressed(event -> setEffect(drop));
        setOnMouseReleased(event -> setEffect(null));
    }


    public void setText(String format) {
        this.text.setText(format);
    }
}
