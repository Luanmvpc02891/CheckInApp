/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.nhutin.app;

import com.nhutin.JFrame.Demo;

/**
 *
 * @author MaiVuLuan
 */
public class CheckInApp {

    public static void main(String[] args) {
       java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() { 
                new Demo().setVisible(true); // MyJFrame thay bằng tên JFrame của bạn
            }
        });
    }
}
