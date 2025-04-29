package com.tsd.workshop.maps.render;

public enum Scale {

    ONE(1), TWO(2);

    int val;

    Scale(int val) {
        this.val = val;
    }

    String toUrl() {
        return "scale=" + val;
    }
}
