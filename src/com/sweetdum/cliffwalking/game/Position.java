package com.sweetdum.cliffwalking.game;

/**
 * Created by Mengxiao Lin on 2016/10/19.
 */
public class Position implements Cloneable {
    private int x,y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Position position = (Position) o;

        if (x != position.x) return false;
        return y == position.y;

    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    protected Position clone() {
        return new Position(x,y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ')';
    }
}
