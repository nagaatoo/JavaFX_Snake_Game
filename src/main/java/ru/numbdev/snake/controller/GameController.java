package ru.numbdev.snake.controller;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import ru.numbdev.snake.component.Direction;
import ru.numbdev.snake.component.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameController {

    private final Random randnum = new Random();

    private static final long FRAME_DELAY = 100;
    private static final int ELEMENT_SIZE = 25;
    private static final int LIVE_CYCLE_LIMIT = 90;

    private static final int YAMMY_IDX = 3;
    private static final int HEAD_IDX = 2;
    private static final int TAIL_IDX = 1;
    private static final int EMPTY_IDX = 0;

    private final int[][] filed = new int[30][30];
    private final List<Element> snake = new ArrayList<>();
    private final Element yammy = new Element();

    private Direction direction = Direction.UP;
    private boolean isFinishedOnFrame = true;

    private boolean isFinished = false;
    private int scoreVal = 0;
    private int liveCycleYammyCount = 0;


    @FXML
    private Label gameOver;

    @FXML
    private Label info;

    @FXML
    private Label score;

    @FXML
    private Canvas canvas;
    private GraphicsContext gc;

    @FXML
    public void initialize() {
        isFinished = false;
        isFinishedOnFrame = true;

        scoreVal = 0;
        liveCycleYammyCount = 0;
        direction = Direction.UP;

        gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gameOver.setVisible(false);
        info.setVisible(false);
        score.setText(String.valueOf(0));
        for (int i = 0; i < 30; i++) {
            for (int j = 0; j < 30; j++) {
                filed[i][j] = 0;
            }
        }
        snake.clear();
        yammy.setFieldX(0).setFieldY(0);

        initStartFiled();
        initLoop();
    }

    private void initStartFiled() {
        var head = new Element()
                .setFieldX(15)
                .setFieldY(15);
        snake.add(head);

        var tail = new Element()
                .setFieldX(15)
                .setFieldY(16);
        snake.add(tail);

        tail = new Element()
                .setFieldX(15)
                .setFieldY(17);
        snake.add(tail);

        tail = new Element()
                .setFieldX(15)
                .setFieldY(18);
        snake.add(tail);

        do {
            yammy.setFieldX(randnum.nextInt(30));
            yammy.setFieldY(randnum.nextInt(30));
        } while (
                yammy.getFieldX() == 15 &&
                        (
                                yammy.getFieldY() == 15 ||
                                        yammy.getFieldY() == 16 ||
                                        yammy.getFieldY() == 17 ||
                                        yammy.getFieldY() == 18
                        )
        );
    }

    private void initLoop() {
        Task<Void> task = new Task<Void>() {
            @Override
            public Void call() throws InterruptedException {
                while (!isFinished) {
                    doStep();
                    printFrame(gc);
                    Thread.sleep(FRAME_DELAY);
                    isFinishedOnFrame = false;
                }

                return null;
            }
        };

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    private void printFrame(GraphicsContext gc) {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (int i = 0; i < filed.length; i++) {
            for (int j = 0; j < filed[i].length; j++) {
                int type = filed[i][j];
                switch (type) {
                    case 1 -> {
                        gc.setFill(Color.DARKGRAY);
                        gc.fillRect(ELEMENT_SIZE * i, ELEMENT_SIZE * j, ELEMENT_SIZE, ELEMENT_SIZE);
                    }
                    case 2 -> {
                        gc.setFill(Color.GREEN);
                        gc.fillRect(ELEMENT_SIZE * i, ELEMENT_SIZE * j, ELEMENT_SIZE, ELEMENT_SIZE);
                    }
                    case 3 -> {
                        gc.setFill(Color.RED);
                        gc.fillOval(ELEMENT_SIZE * i, ELEMENT_SIZE * j, ELEMENT_SIZE, ELEMENT_SIZE);
                    }
                }
            }
        }
    }

    private void doStep() {
        int prevX = 0;
        int prevY = 0;
        boolean isNeedNewTailElement = false;
        Element newTail = null;
        for (int i = 0; i < snake.size(); i++) {
            var element = snake.get(i);
            if (i == 0) { // head
                prevX = element.getFieldX();
                prevY = element.getFieldY();
                switch (direction) {
                    case UP -> {
                        var proto = element.getFieldY() - 1;

                        if (proto < 0) {
                            proto = filed.length - 1;
                        }

                        var target = filed[element.getFieldX()][proto];
                        if (target == YAMMY_IDX) {
                            isNeedNewTailElement = true;
                        } else if (target == TAIL_IDX) {
                            finish();
                            return;
                        }
                        element.setFieldY(proto);
                    }
                    case DOWN -> {
                        var proto = element.getFieldY() + 1;
                        if (proto > filed.length - 1) {
                            proto = 0;
                        }

                        var target = filed[element.getFieldX()][proto];
                        if (target == YAMMY_IDX) {
                            isNeedNewTailElement = true;
                        } else if (target == TAIL_IDX) {
                            finish();
                            return;
                        }
                        element.setFieldY(proto);
                    }
                    case LEFT -> {
                        var proto = element.getFieldX() - 1;
                        if (proto < 0) {
                            proto = filed.length - 1;
                        }

                        var target = filed[proto][element.getFieldY()];
                        if (target == YAMMY_IDX) {
                            isNeedNewTailElement = true;
                        } else if (target == TAIL_IDX) {
                            finish();
                            return;
                        }
                        element.setFieldX(proto);
                    }
                    case RIGHT -> {
                        var proto = element.getFieldX() + 1;
                        if (proto > filed.length - 1) {
                            proto = 0;
                        }

                        var target = filed[proto][element.getFieldY()];
                        if (target == YAMMY_IDX) {
                            isNeedNewTailElement = true;
                        } else if (target == TAIL_IDX) {
                            finish();
                            return;
                        }
                        element.setFieldX(proto);
                    }
                }
                filed[element.getFieldX()][element.getFieldY()] = HEAD_IDX;
            } else if (snake.size() - 1 == i) { //tail
                if (isNeedNewTailElement) {
                    newTail = new Element()
                            .setFieldX(element.getFieldX())
                            .setFieldY(element.getFieldY());
                }
                filed[element.getFieldX()][element.getFieldY()] = isNeedNewTailElement ? TAIL_IDX : EMPTY_IDX;
                element.setFieldX(prevX);
                element.setFieldY(prevY);
                filed[element.getFieldX()][element.getFieldY()] = TAIL_IDX;
            } else { // middle
                int memX = element.getFieldX();
                int memY = element.getFieldY();

                element.setFieldX(prevX);
                element.setFieldY(prevY);

                prevX = memX;
                prevY = memY;
                filed[element.getFieldX()][element.getFieldY()] = TAIL_IDX;
            }
        }

        if (isNeedNewTailElement) {
            Platform.runLater(() -> score.setText(String.valueOf(scoreVal += 1)));
            snake.add(newTail);
            regenerateYammy();
            liveCycleYammyCount = 0;
        } else {
            if (liveCycleYammyCount >= LIVE_CYCLE_LIMIT) {
                regenerateYammy();
                liveCycleYammyCount = 0;
            } else {
                liveCycleYammyCount += 1;
            }
        }
        filed[yammy.getFieldX()][yammy.getFieldY()] = YAMMY_IDX;
    }

    private void regenerateYammy() {
        filed[yammy.getFieldX()][yammy.getFieldY()] = EMPTY_IDX;
        do {
            yammy.setFieldX(randnum.nextInt(30));
            yammy.setFieldY(randnum.nextInt(30));
        } while (filed[yammy.getFieldX()][yammy.getFieldY()] != EMPTY_IDX);
    }

    public void setDirection(KeyEvent event) {
        if (isFinished && event.getCode() == KeyCode.SPACE) {
            initialize();
        } else if (isFinishedOnFrame) {
            return;
        }

        var pushedDirection = Direction.getDirection(event.getCode().getName());
        switch (pushedDirection) {
            case UP -> {
                if (direction != Direction.DOWN) {
                    direction = pushedDirection;
                }
            }
            case DOWN -> {
                if (direction != Direction.UP) {
                    direction = pushedDirection;
                }
            }
            case LEFT -> {
                if (direction != Direction.RIGHT) {
                    direction = pushedDirection;
                }
            }
            case RIGHT -> {
                if (direction != Direction.LEFT) {
                    direction = pushedDirection;
                }
            }
            case null -> {
                return;
            }
        }
        isFinishedOnFrame = true;
    }

    private void finish() {
        isFinished = true;
        gameOver.setVisible(true);
        info.setVisible(true);
    }
}
