package com.vrptw;

import com.vrptw.forms.VRPTW;

import javax.swing.*;

public class App_Main {

    public static void main(String[] args) {
        // Launch the App Window
        VRPTW v = new VRPTW();
        v.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        v.setVisible(true);

    }
}
