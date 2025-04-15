package com.tsd.workshop.workmanship.data;

public enum TaskType {

    REMOVE("removal of unused spare parts"),
    WELD("weld using the machine"),
    PRESS("accident repair work"),
    INSPECT("checking of condition"),
    REPAIR("could include removal of spare part and repair"),
    REPLACE("removal of spare part and install a working one"),
    MODIFY("modify existing spare parts to suit the work"),
    INSTALL("just install without removal work"),
    ADJUST("adjust clutch, brake, wheel"),
    PUMP("pump or refill grease, oil and fuel"),
    CLEAN("clean spare part"),
    OVERHAUL("major overhaul of engine or transmission box"),
    SHIFT("shift tires including rotation of tires too"),
    WASH("wash cabin, trailer, tires"),
    PAINT("paint cabin, trailer, tires");

    final String description;

    TaskType(String description) {
        this.description = description;
    }
}
