package  led;

/**
 * Created by IntelliJ IDEA.
 * User: vlakin
 * Date: 4/17/13
 * Time: 11:28 AM
 * To change this template use File | Settings | File Templates.
 */
public class LedText {
    private int letcol[];
    private boolean msg[][];
    LedOperation ledOperation;
    int h, w;
    int WIDTH, HEIGHT, TOTAL;
    Letters let;
    Index index;

    // The constructor
    // set up some variables that we need
    public LedText(int widthInLEDs, int heightInLEDs, Letters letters) {
        w = widthInLEDs;
        h = heightInLEDs;

        HEIGHT = 5 * h;
        WIDTH = 5 * w;
        let = letters;
    }

    // Set the messege for the current text
    public void setmsg(LedOperation f) {
        int a, b;
        int  j, k;
        int p;
        int len;
        char c;

        ledOperation = f;

        // Find the length of the text in "LED's"
        len = 0;
        for (int i = 0; i < ledOperation.getText().length(); i++) {
            len += (let.getLetter(ledOperation.getText().charAt(i))).width + 1;
        }

        // Can we center the text?
        if (ledOperation.isCentered() && len <= w) {
            // Yes! Calculate the centered text.
            a = w;
            a = a - len;
            a = a / 2;
            ledOperation.setStartSpace(a);
            ledOperation.setEndSpace(a);
            if (a * 2 < w)
                ledOperation.startSpace++;  // integer division by 2 can only have an error of 1
        }

        // TOTAL = total length of message (white space included)
        TOTAL = len + ledOperation.startSpace + ledOperation.endSpace;

        // The message in boolean (LED) format structure
        msg = new boolean[TOTAL][h];

        // Make sure the new message is empty to start
        for (int i = 0; i < TOTAL; i++)
            for (j = 0; j < h; j++)
                msg[i][j] = false;

        // The color of each column of LEDs
        letcol = new int[TOTAL];
        int length = letcol.length;

        for (int i = 0; i < TOTAL; i++){
            letcol[i] = 1;  // The default red
        }
        p = ledOperation.startSpace;
        c = 'r';

        int textLength = ledOperation.getText().length();
        for (int i = 0; i < textLength; i++) {
            // get letter i in ledOperation.text in LED format
            index = let.getLetter(ledOperation.getText().charAt(i));
            String color = ledOperation.getColor();
            if (color.length() > 0) {
                try {
                    c = ledOperation.getColor().charAt(i);
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("Out of bounds in LEDMessage.setmsg");
                }
            }

            k = index.width;
            for (a = 0; a < k; a++) {
                for (b = 0; b < h; b++) {
                    // Fill the message structure
                    try {
                        msg[p + a][b] = index.letter[a][b];
                    } catch (IndexOutOfBoundsException e) {
                    }

                    // Set the colors
                    if (c == 'r')
                        letcol[p + a] = 1;
                    else if (c == 'g')
                        letcol[p + a] = 2;
                    else if (c == 'b')
                        letcol[p + a] = 3;
                    else if (c == 'y')
                        letcol[p + a] = 4;
                    else if (c == 'o')
                        letcol[p + a] = 5;
                    else if (c == 'p')
                        letcol[p + a] = 6;
                    else if (c == 'w')
                        letcol[p + a] = 7;
                    else if (c == 'c')
                        letcol[p + a] = 8;
                }
            }
            p += index.width + 1;
        }
        System.out.println();
    }

    // return the state of the LED (on/off)
    public boolean getLED(int x, int y) {
        if (x >= 0 && x < TOTAL && y >= 0 && y < h)
            return msg[x][y];
        else
            return false;
    }

    // returns the color of the LED
    public int getColor(int x) {
        if (x >= 0 && x < TOTAL)
            return letcol[x];
        else
            return 1;  // default red
    }

    // get the length of the messege in LEDs
    public int length() {
        return TOTAL;
    }

    // Check and see if we're still in the message
    public boolean inRange(int x) {
        if (x >= 0 && x < TOTAL)
            return true;
        else
            return false;
    }
}



