package com.tsd.workshop.workmanship.data;

public enum TaskType {

    REMOVAL("removal of unused spare parts"),
    WELD("weld using the machine"),
    INSPECT("checking of condition"),
    REPAIR("could include removal of spare part and repair"),
    CHANGE("removal of spare part and install a working one"),
    INSTALL("just install without removal work"),
    ADJUST("adjust clutch or brake"),
    PUMP("pump or refill grease, oil and fuel"),
    CLEAN("clean spare part"),
    OVERHAUL("major overhaul of engine or transmission box"),
    SHIFT("shift tires"),
    WASH("wash cabin, trailer, tires"),
    PAINT("paint cabin, trailer, tires");

    final String description;

    TaskType(String description) {
        this.description = description;
    }
}
