package com.tsd.workshop.maps.render;

public class Size {

    private int horizontal;

    private int vertical;

    public static Size of(int horizontal, int vertical) {
        Size size = new Size();
        size.horizontal = horizontal;
        size.vertical = vertical;
        return size;
    }

    String toUrl() {
        return "size=%sx%s".formatted(horizontal, vertical);
    }
}
