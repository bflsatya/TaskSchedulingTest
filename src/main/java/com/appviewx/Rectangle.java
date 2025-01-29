package com.appviewx;

public class Rectangle implements TwoDimensionalShape {
    private int length;
    private int breadth;

    public Rectangle(int length, int breadth) {
        this.length = length;
        this.breadth = breadth;
    }

    @Override
    public final void area() {
        System.out.println("Area : " + (this.length * this.breadth));
    }
}
