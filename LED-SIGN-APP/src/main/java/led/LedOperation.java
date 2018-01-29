package led;

import java.net.URL;
import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: vlakin
 * Date: 4/17/13
 * Time: 11:28 AM
 * To change this template use File | Settings | File Templates.
 */
public class LedOperation {

    public static enum IDs {
        NONE, DO, REPEAT, RELOAD,
        BLINK, SLEEP, CLEAR,
        APPEAR, SCROLL_CENTER, OVER_CENTER, PIXELS,
        SCROLL_UP, SCROLL_DOWN, SCROLL_LEFT, SCROLL_RIGHT,
        OVER_UP, OVER_DOWN, OVER_LEFT, OVER_RIGHT,
    }

    public IDs ID;
    static final String COLOR_MARK = "*";
    long delay;
    int startSpace, endSpace;
    public int times, remaining;
    boolean centered;
    String color;
    String text;
    String store;  // store the original text line
    Pixel[] pixels;
    URL url;       // The url associated with this message
    public ScriptLink ret;  // pointer to the return place in the script (for loops);

    private LedText message;
    int stepCounter; // The place where we are in each transition.  Howwe know when we are done.

    public LedOperation() {

        // Assign the defaults
        ID = LedOperation.IDs.NONE;
        delay = 40;
        startSpace = 10;
        endSpace = 20;
        times = -1;
        remaining = 0;
        centered = false;
        color = new String("");
        text = new String("No text specified");
        url = null;
        ret = null;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public void setStartSpace(int startSpace) {
        this.startSpace = startSpace;
    }

    public void setEndSpace(int endSpace) {
        this.endSpace = endSpace;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public void setCentered(boolean centered) {
        this.centered = centered;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public int decreaseRemaining() {
        return remaining--;
    }

    public void setRemaining(int remaining) {
        this.remaining = remaining;
    }

    public boolean isCentered() {
        return centered;
    }

    public String getColor() {
        return color;
    }

    public int getColor(int index) {
        return message.getColor(index);
    }

    public boolean inRange(int position) {
        return message.inRange(position);
    }

    public boolean hasText() {
        return text != null && text.length() != 0;
    }

    public String getText() {
        return text;
    }

    public String getStore() {
        return store;
    }

    public URL getUrl() {
        return url;
    }

    public ScriptLink getRet() {
        return ret;
    }

    public void setMessage(LedText message) {
        this.message = message;
        message.setmsg(this);
        stepCounter = 0;
    }

    public int getMessageLength() {
        return message.length();
    }

    public boolean getLED(int x, int y) {
        return message.getLED(x, y);
    }

    public int getStepCounter() {
        return stepCounter;
    }

    public void setStepCounter(int stepCounter) {
        this.stepCounter = stepCounter;
    }

    public String toString() {
        return "Operation: type = " + ID + ", delay = " + delay + ", centered = " + centered +
                ", color " + color + ", store = " + store + ", text = " + text;
    }
}

