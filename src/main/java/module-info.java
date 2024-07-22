module ru.numbdev.snake {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires com.almasb.fxgl.all;

    opens ru.numbdev.snake to javafx.fxml;
    exports ru.numbdev.snake;
    exports ru.numbdev.snake.controller;
    opens ru.numbdev.snake.controller to javafx.fxml;
}