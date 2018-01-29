package led;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: vlakin
 * Date: 4/9/13
 * Time: 4:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class OperationBuilder {

    OperationBuilder() {

    }

    public static LedOperation buildOperation(String operationAttributes) {
        LedOperation ledOperation = createOperation(operationAttributes);
        // Parse the text line to expand any time/date tags
        ledOperation = finalizeText(ledOperation);
        return ledOperation;
    }

    private static LedOperation createOperation(String operationAttributes) {

        LedOperation ledOperation = new LedOperation();
        int i;
        String tmp;



        //get rid of any starting (and ending) white space, just to be sure.
        operationAttributes = operationAttributes.trim();

        ////////////////////////////////////////////////////
        // Any parameters that might exist.  This will
        // read in any command line parameters for each
        // function.  For example: Sleep text=blah blah
        // is accepted, but the text will never be used

        tmp = getParam(operationAttributes, "delay");
        if (tmp != null)
            ledOperation.delay = (new Integer(tmp)).intValue();

        tmp = getParam(operationAttributes, "clear");
        if (tmp != null && tmp.compareTo("true") == 0) {
            ledOperation.centered = true;
            ledOperation.text = new String("");
        } else {
            tmp = getParam(operationAttributes, "center");
            if (tmp != null && tmp.compareTo("true") == 0)
                ledOperation.centered = true;
            else {
                ledOperation.centered = false;
                tmp = getParam(operationAttributes, "startspace");
                if (tmp != null)
                    ledOperation.startSpace = (new Integer(tmp)).intValue();

                tmp = getParam(operationAttributes, "endspace");
                if (tmp != null)
                    ledOperation.endSpace = (new Integer(tmp)).intValue();
            }

            tmp = getParam(operationAttributes, "text");
            if (tmp != null)
                ledOperation.text = tmp;
        }

        tmp = getParam(operationAttributes, "times");
        if (tmp != null) {
            ledOperation.times = (new Integer(tmp)).intValue();
            ledOperation.remaining = ledOperation.times;
        }

        tmp = getParam(operationAttributes, "pixels");
        if (tmp != null) {
            ledOperation.times = (new Integer(tmp)).intValue();
            ledOperation.remaining = ledOperation.times;
        }

        tmp = getParam(operationAttributes, "URL");
        if (tmp != null) {
            try {
                ledOperation.url = new URL(tmp);
            } catch (MalformedURLException e) {
                System.out.println("Bad URL: " + tmp);
                ledOperation.url = null;
            }
        } else {
            ledOperation.url = null;
        }

        ////////////////////////////////////////////////////
        // set the function number (and some minor
        // tweeks/precautions)
        i = operationAttributes.indexOf(" ");
        if (i != -1)
            tmp = operationAttributes.substring(0, i);
        else
            tmp = operationAttributes;

        if (tmp.compareTo("Appear") == 0) {
            ledOperation.ID = LedOperation.IDs.APPEAR;
        } else if (tmp.compareTo("Sleep") == 0) {
            ledOperation.ID = LedOperation.IDs.SLEEP;
        } else if (tmp.compareTo("ScrollLeft") == 0) {
            ledOperation.ID = LedOperation.IDs.SCROLL_LEFT;
        } else if (tmp.compareTo("ScrollRight") == 0) {
            ledOperation.ID = LedOperation.IDs.SCROLL_RIGHT;
        } else if (tmp.compareTo("ScrollUp") == 0) {
            ledOperation.ID = LedOperation.IDs.SCROLL_UP;
        } else if (tmp.compareTo("ScrollDown") == 0) {
            ledOperation.ID = LedOperation.IDs.SCROLL_DOWN;
        } else if (tmp.compareTo("Pixel") == 0) {
            ledOperation.ID = LedOperation.IDs.PIXELS;

            // Just for precautions dealing with a delay problem.
            // This shouldn't be noticable.
            if (ledOperation.delay < 1)
                ledOperation.delay = 1;

            // Can't allow "times" to be 0 or less, it will cause
            // the sign to freeze (not procede).
            if (ledOperation.times < 1)
                ledOperation.times = 15;

        } else if (tmp.compareTo("Blink") == 0) {
            ledOperation.ID = LedOperation.IDs.BLINK;

            if (ledOperation.times < 1)
                ledOperation.times = 2;
        } else if (tmp.compareTo("OverRight") == 0) {
            ledOperation.ID = LedOperation.IDs.OVER_RIGHT;
        } else if (tmp.compareTo("ScrollCenter") == 0) {
            ledOperation.ID = LedOperation.IDs.SCROLL_CENTER;
        } else if (tmp.compareTo("OverCenter") == 0) {
            ledOperation.ID = LedOperation.IDs.OVER_CENTER;
        } else if (tmp.compareTo("OverLeft") == 0) {
            ledOperation.ID = LedOperation.IDs.OVER_LEFT;
        } else if (tmp.compareTo("OverUp") == 0) {
            ledOperation.ID = LedOperation.IDs.OVER_UP;
        } else if (tmp.compareTo("OverDown") == 0) {
            ledOperation.ID = LedOperation.IDs.OVER_DOWN;
        } else if (tmp.compareTo("Do") == 0) {
            ledOperation.ID = LedOperation.IDs.DO; // This marks a place for the "repeats" to go back to.
        } else if (tmp.compareTo("Repeat") == 0) {
            ledOperation.ID = LedOperation.IDs.REPEAT;
        } else if (tmp.compareTo("Reload") == 0) {
            ledOperation.ID = LedOperation.IDs.RELOAD;
        }

        ledOperation.store = ledOperation.text;

        return ledOperation;
    }

    /**
     * @param operationAttributes
     * @param sub
     * @return
     */
    private static String getParam(String operationAttributes, String sub) {
        int i, j;
        String tmp;

        i = operationAttributes.indexOf(sub);
        j = operationAttributes.indexOf("text");

        if (j == -1 || i <= j) {
            // if the first occurance of "sub" is before
            // the "text=" (ie not in the message)
            if (i == -1) {
                return null;
            } else {
                tmp = operationAttributes.substring(i);  // forget everything before the sub
                i = tmp.indexOf("=");
                if (i == -1) {
                    System.out.println("Error in '" + sub + "' parameter in " + operationAttributes);
                    return null;
                } else {
                    i++;  // one spot after the "="
                    if (sub.compareTo("text") == 0)
                        tmp = tmp.substring(i);
                    else {
                        tmp = tmp.substring(i);
                        if (tmp.indexOf(" ") != -1)
                            tmp = tmp.substring(0, tmp.indexOf(" "));
                    }
                    tmp.trim();
                    return tmp;
                }
            }
        } else {
            return null;
        }
    }

    //////////////////////////////////////////////////////////////////
    // create the final text line from parsing the store line
    //   Add any codes (ie \t, \r, \g, \b, etc.) here to parse
    //   out of the text line.
    private static LedOperation finalizeText(LedOperation ledOperation) {
        String tmp;
        String time;
        String month[] = {"Jan", "Feb", "Mar", "Apr", "May", "Jun",
                "Jul", "Aug", "Sept", "Oct", "Nov", "Dec"};
        String Month[] = {"January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"};
        String day[] = {"Sun", "Mon", "Tues", "Wed", "Thur", "Fri", "Sat"};
        String Day[] = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        String ddmmyy;
        int min;
        int pm;
        Date date = new Date();
        int a, b;
        int i;
        char character;
        String t;         // The tag  (eg. text=Hello \ythere,  t=y)

        tmp = ledOperation.store;
        ledOperation.color = "";

        if (ledOperation.ID == LedOperation.IDs.APPEAR ||
                (ledOperation.ID != LedOperation.IDs.SLEEP &&
                        ledOperation.ID != LedOperation.IDs.DO &&
                        ledOperation.ID != LedOperation.IDs.REPEAT &&
                        ledOperation.ID != LedOperation.IDs.RELOAD)) {
            character = 'r';  // the default color
            b = 0;
            while (b < tmp.length()) {
                if (tmp.charAt(b) == '^' || tmp.charAt(b) == '\\' || tmp.charAt(b) == '#') { // if there is a '\' does the following
                                                 // letter indicate a color.
                    b++;
                    // Get the tag!
                    if (tmp.charAt(b) == '{') {
                        t = tmp.substring(b + 1);

                        // cut out the \{XX}
                        tmp = tmp.substring(0, b - 1).concat(t.substring(t.indexOf('}') + 1));
                        t = t.substring(0, t.indexOf('}'));
                        b -= 1;
                    } else {
                        t = tmp.substring(b, b + 1);
                        tmp = (tmp.substring(0, b - 1)).concat(tmp.substring(b + 1));  // take the "\r" out
                        b -= 1;
                    }

                    // set the
                    if (t.length() == 1 && isColor(t.charAt(0))) {
                        character = t.charAt(0);
                    } else if (t.compareTo("tt") == 0) {
                        // it is the "time" variable!!
                        if (date.getHours() >= 12)
                            pm = 1;
                        else
                            pm = 0;

                        if (pm == 1) {
                            a = date.getHours();
                            if (a == 12)
                                time = String.valueOf(12);
                            else
                                time = String.valueOf(date.getHours() - 12);
                        } else {
                            a = date.getHours();
                            if (a == 0)
                                time = String.valueOf(12);
                            else
                                time = String.valueOf(a);
                        }

                        time = time.concat(":");

                        min = date.getMinutes();
                        if (min >= 10)
                            time = time.concat(String.valueOf(min));
                        else {
                            time = time.concat("0");
                            time = time.concat(String.valueOf(min));
                        }

                        if (pm == 1)
                            time = time.concat(" pm");
                        else
                            time = time.concat(" am");

                        tmp = ((tmp.substring(0, b)).concat(time)).concat(tmp.substring(b));

                        b += time.length();

                        for (i = 0; i < time.length(); i++)
                            ledOperation.color = (ledOperation.color).concat((new Character(character)).toString());

                    } // End time
                    else if (t.compareTo("dd") == 0 || t.compareTo("DD") == 0)   // Set the current date
                    {
                        if (t.compareTo("dd") == 0)
                            ddmmyy = day[date.getDay()];
                        else
                            ddmmyy = Day[date.getDay()];

                        // Set up the color
                        for (i = 0; i < ddmmyy.length(); i++)
                            ledOperation.color = (ledOperation.color).concat((new Character(character)).toString());

                        tmp = ((tmp.substring(0, b)).concat(ddmmyy)).concat(tmp.substring(b));
                        b += ddmmyy.length();
                    } else if (t.compareTo("dn") == 0) {
                        ddmmyy = String.valueOf(date.getDate());

                        // Set up the color
                        for (i = 0; i < ddmmyy.length(); i++)
                            ledOperation.color = (ledOperation.color).concat((new Character(character)).toString());

                        tmp = ((tmp.substring(0, b)).concat(ddmmyy)).concat(tmp.substring(b));
                        b += ddmmyy.length();
                    } else if (t.compareTo("mm") == 0 || t.compareTo("MM") == 0) {
                        if (t.compareTo("mm") == 0)
                            ddmmyy = month[date.getMonth()];
                        else
                            ddmmyy = Month[date.getMonth()];

                        // Set up the color
                        for (i = 0; i < ddmmyy.length(); i++)
                            ledOperation.color = (ledOperation.color).concat((new Character(character)).toString());

                        tmp = ((tmp.substring(0, b)).concat(ddmmyy)).concat(tmp.substring(b));
                        b += ddmmyy.length();
                    } else if (t.compareTo("mn") == 0) {
                        ddmmyy = String.valueOf(date.getMonth() + 1);

                        // Set up the color
                        for (i = 0; i < ddmmyy.length(); i++)
                            ledOperation.color = (ledOperation.color).concat((new Character(character)).toString());

                        tmp = ((tmp.substring(0, b)).concat(ddmmyy)).concat(tmp.substring(b));
                        b += ddmmyy.length();
                    } else if (t.compareTo("yy") == 0 || t.compareTo("YY") == 0) {
                        if (t.compareTo("YY") == 0)
                            ddmmyy = String.valueOf(date.getYear() + 1900);
                        else
                            ddmmyy = String.valueOf(date.getYear() % 100);

                        // Set up the color
                        for (i = 0; i < ddmmyy.length(); i++)
                            ledOperation.color = (ledOperation.color).concat((new Character(character)).toString());

                        tmp = ((tmp.substring(0, b)).concat(ddmmyy)).concat(tmp.substring(b));
                        b += ddmmyy.length();

                    }  // End short date
                    else if (t.compareTo("\\") == 0)  // Are they trying to delimit the backslash?
                    {
                        tmp = (tmp.substring(0, b)).concat(tmp.substring(b + 1));  // delimit the '\'
                        b--;
                    } else {
                        // A little error output
                        System.out.println("Backslash (\\) error in text line: " + ledOperation.store);
                    }

                }  // END - if(tmp.charAt(b) == '\\')
                else {
                    b++;
                    ledOperation.color = ledOperation.color.concat((new Character(character)).toString());
                }

            }  // END - for(...)

        } // END - if(ledOperation.func == ...)

        ledOperation.text = tmp;

        return ledOperation;

    }

    //////////////////////////////////////////////////////////////////
    // just a simple function to see if it is a color code
    private static boolean isColor(char t) {
        return (t == 'r' || t == 'g' || t == 'b' || t == 'y' || t == 'o' || t == 'p' || t == 'w' || t == 'c');
    }
}
