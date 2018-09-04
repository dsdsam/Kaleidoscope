package adf.utils;

/**
 * Created with IntelliJ IDEA.
 * User: XP Admin
 * Date: 3/7/14
 * Time: 8:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class RingStack {

    public int buffSize;
    private Object[] buff;
    private int top;
    private int size;

    /**
     *
     */
    public RingStack() {
        this(10);
    }

    /**
     * @param buffSize
     */
    public RingStack(int buffSize) {
        initBuffer(buffSize);
    }

    public void initBuffer(int buffSize) {
        this.buffSize = buffSize;
        buff = new Object[buffSize];
        top = -1;
        size = 0;
    }

    public int getSize() {
        return buffSize;
    }

    public int getCurrentSize() {
        return size;
    }


    /**
     * @param element
     */
    public void add(Object element) {
        top = (++top) % buffSize;
        buff[top] = element;
        if (size < buffSize) {
            size++;
        }
    }

    /**
     * @param ind
     * @return
     */
    private int getIndex(int ind) {
        ind = top - ind;
        if (ind < 0) {
            return buffSize + ind;
        }
        return ind;
    }

    /**
     * @return
     */
    public Object getLast() {
        if (size == 0) {
            return null;
        }
        int last = (top + 1) % buffSize;
//        if(last >= buffSize){
//            last =  last - buffSize ;
//        }
//        System.out.println("RingStack last index = " + last);
        Object element = buff[last];
        return element;
    }

    /**
     * @param ind
     * @return
     */
    public Object getElementAt(int ind) {
        if (size == 0) {
            return null;
        }
        ind = getIndex(ind);
        return buff[ind];
    }

    public void clear() {
        top = -1;
        size = 0;
    }

    public void printStack() {
        System.out.println("\n\n Printing Ring Steck: size = " + size);
        int startIndex = top;
        for (int ind = 0; ind < size; ind++) {
            Object object = getElementAt(ind);
            System.out.println(" ind = " + ind + " data = " + object);
        }
    }
}

