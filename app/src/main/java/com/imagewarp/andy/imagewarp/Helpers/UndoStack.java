package com.imagewarp.andy.imagewarp.Helpers;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class UndoStack {

    private ArrayList<Bitmap> stack;
    private int size;

    public UndoStack (int size){
        this.size = size;
        stack = new ArrayList<Bitmap>();
    }

    public void push(Bitmap b) {
        stack.add(b);

        while (stack.size() > size) {
            stack.remove(0);
        }
    }

    public Bitmap pop() {
        return stack.remove(stack.size()-1);
    }

    public int size() {
        return stack.size();
    }

    public void changeSize(int newSize) {
        this.size = newSize;
    }
}
