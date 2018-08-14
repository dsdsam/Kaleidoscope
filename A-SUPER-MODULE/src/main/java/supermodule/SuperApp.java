package supermodule;

import dsdsse.app.DSDSSEApp;
import sem.app.SEMApp;


import javax.swing.*;

/**
 * Created by Admin on 10/1/2016.
 * <p>
 * <p>
 * ADF    MATHEMATICS     VECTOR-WORKS        MCLN          LED
 * / \        / \              / \            / \           / \
 * |          |                |              |             |
 * |          +------------+   |              |             |
 * |                       |   |              |             |
 * |          +--------+---|---+---------+    |             |
 * |          |        |   |   |         |    |             |
 * |      MCLN-MODEL   |  ADF-CSYS-VIEW  |    |             |
 * |         / \       |       / \  / \  |    |             |
 * |          |        |        |    |   |    |             |
 * |          |        |        |    |   |    |             |
 * |          +----- DSDSSE ----+    +---+----+----- SE ----+
 * |                  |                              |
 * +------------------+                              |
 * +-------------------------------------------------+
 * <p>
 * <p>
 * P a c k a g e s   d e p e n d e n c y
 */
public class SuperApp {

    public static void main(String[] args) {
        Runnable runnable = () -> {
            try {
                DSDSSEApp.createDSDSSEApp();
                SEMApp.createSEMApp();
            } catch (Throwable e) {
                System.out.println("DSDSSEApp.main: Throwable cought ! " + e.toString());
                System.out.println("DSDSSEApp.main: See Error Log for details.");
                e.printStackTrace();
            }
        };
        SwingUtilities.invokeLater(runnable);
    }
}
