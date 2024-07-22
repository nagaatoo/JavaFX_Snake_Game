package ru.numbdev.snake.component;

public class Element {

    protected int fieldX;
    protected int fieldY;

    public Element() {
    }

    public int getFieldX() {
        return fieldX;
    }

    public Element setFieldX(int fieldX) {
        this.fieldX = fieldX;
        return this;
    }

    public int getFieldY() {
        return fieldY;
    }

    public Element setFieldY(int fieldY) {
        this.fieldY = fieldY;
        return this;
    }

}
