package ru.numbdev.snake.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import ru.numbdev.snake.Start;

import java.io.IOException;

public class HelloController {

    @FXML
    protected void startGame() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Start.class.getResource("game.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            var controller = (GameController) fxmlLoader.getController();
            scene.setOnKeyPressed(controller::setDirection);
            Start.primaryStage.setScene(scene);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}