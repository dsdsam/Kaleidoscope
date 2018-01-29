package adf.utils;

/**
 * Created with IntelliJ IDEA.
 * User: XP Admin
 * Date: 3/7/14
 * Time: 8:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class RingArray {

    private int buffSize;
    private Object[] buff;
    private int head;
    private int tail;
    private int size;

    /**
     *
     */
    public RingArray() {
        this(10);
    }

    /**
     *
     * @param buffSize
     */
    public RingArray(int buffSize) {
        this.buffSize = buffSize;
        buff = new Object[buffSize];
        head = 0;
        tail = 0;
        size = 0;
    }

    /**
     *
     * @param element
     */
    public void add(int element) {
        buff[tail] = element;
        tail = (++tail) % buffSize;
        size++;
// System.out.println("  curHead = "+curHead+
//                    "  curTail = "+curTail+
//                    "  buffSize = "+ buffSize+
//                    "  curNum = "+ curNum+
//                    "  state  = "+entry);
    }

    /**
     *
     * @param ind
     * @return
     */
    private int getIndex(int ind) {
        ind = head + ind;
        if (ind < buffSize){
            return ind;
        }
        return ind - buffSize;
    }

    public Object getHead() {
        if (size == 0){
            return null;
        }
        Object element = buff[head];
        head = (++head) % buffSize;
        size--;
        return element;
    }

    public Object lookUp(int ind){
        if (size == 0){
            return null;
        }
        ind = getIndex(ind);
        return buff[ind];
    }
}
