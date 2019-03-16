package com.andrius.hills.model;

import java.util.Objects;

public class AscFile {
    private final String name;
    private final double x;
    private final double y;
    private final double cellSize;
    private final short[][] cells;

    public AscFile(String name, double x, double y, double cellSize, short[][] cells) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.cellSize = cellSize;
        this.cells = cells;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getCellSize() {
        return cellSize;
    }

    public short[][] getCells() {
        return cells;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "com.andrius.hills.model.AscFile{" +
                "name=" + name +
                "x=" + x +
                ", y=" + y +
                ", cellSize=" + cellSize +
                ", cells=" + Objects.hashCode(cells) +
                '}';
    }
}
