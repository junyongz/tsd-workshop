package com.tsd.workshop.maps.render;

public class Size {

    public static final Size GOOGLE_MAP_MAX_SIZE = Size.of(640, 640);

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
