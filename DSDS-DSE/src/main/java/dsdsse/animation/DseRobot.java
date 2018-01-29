package dsdsse.animation;

import java.awt.*;

/**
 * Created by Admin on 7/11/2017.
 */
public class DseRobot extends Robot{

    private boolean ignoreDelay;

    DseRobot() throws Exception {
           super();
    }

    void setIgnoreDelay(){
        ignoreDelay = true;
    }

    public synchronized void delay(int ms) {
        if(ignoreDelay){
            return;
        }
        super.delay(ms);
    }

}
