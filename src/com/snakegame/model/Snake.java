package com.snakegame.model;

import java.awt.Point;
import java.util.LinkedList;

public class Snake {
    private LinkedList<Point> body;
    private int dx = 1; // Startet nach rechts
    private int dy = 0;

    public Snake(int startX, int startY, int initialLength) {
        body = new LinkedList<>();
        for (int i = 0; i < initialLength; i++) {
            body.add(new Point(startX - i, startY));
        }
    }

    public LinkedList<Point> getBody() {
        return body;
    }

    public Point getHead() {
        return body.getFirst();
    }

    public void move() {
        Point head = getHead();
        Point newHead = new Point(head.x + dx, head.y + dy);
        body.addFirst(newHead);
        body.removeLast();
    }

    public void grow() {
        Point head = getHead();
        Point newHead = new Point(head.x + dx, head.y + dy);
        body.addFirst(newHead);
    }

    public void setDirection(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }
}
