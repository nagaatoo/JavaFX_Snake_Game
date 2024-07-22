package ru.numbdev.snake.component;

public enum Direction {
    UP, DOWN, LEFT, RIGHT;

    public static Direction getDirection(final String direction) {
        try {
            return Direction.valueOf(direction.toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }
}
