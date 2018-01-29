/////////////////////////////////////////////////////////////////////
//  Letters.java   -- LED Sign V2.5
//
//  This class parses the font file and stores
//  each letter in an array of boolean (on/off).
//  It takes care of all the storage and
//  retrieval of letters data structure.
//
//  Revisions:
//     V2.5: Fixed all known bugs in previous versions!  Added
//           the new feature of ledsize, which allows the user
//           to specify in pixels how big the LED's (1-4).
//           Thanks to Robert B. Denny (rdenny@dc3.com) for
//           code and input!
//           Modified Dec 20-26, 1995
//
//     V2.0beta: Modified V1.0 to comply with Pre-Beta java.
//               A problem with delay causes a jerky display.
//               Modified Oct 20 - 29, 1995
//
//     V1.0: Written July 13 - 14, 1995
//
//  By Darrick Brown
//     dbrown@cs.hope.edu
//     http://www.cs.hope.edu/~dbrown/
//
//   Copyright 1995
/////////////////////////////////////////////////////////////////////

package  led;

import java.io.*;
import java.net.*;

//////////////////////////////////////////////////////////////////
// The Letters Class
//////////////////////////////////////////////////////////////////

public class Letters {
    int HEIGHT, TOTAL;
    String let;
    URL url;
    InputStream inputStream;
    DataInputStream dis;
    int w, h, num, place, len, space, swidth;
    Index index[];


    //////////////////////////////////////////////////////////////////
    // The class constructor
    public Letters(String fileName, int width) {
        try {
//            fileName = "/fonts/default.font";
            // Set some initial variables
            URL url = Letters.class.getResource(fileName);
            System.out.println("Letters, get font url \"" + fileName + "\", url = " + url);
            inputStream = url.openStream();
//        file = (new File(fileName).toURL()).openStream();
//            file = new FileInputStream(new File(fileName));
            System.out.println("file " + new File(fileName).getAbsolutePath());
            // file = new InputStream( File() );//.getI(new URL(baseURL,URLfile)).openStream();
            dis = new DataInputStream(inputStream);
            swidth = width;
            initLetters();

            // Set some initial variables
//         file = (new URL(url,URLfile)).openStream();
//         dis = new DataInputStream(file);
//         swidth = width;
//         initLetters();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    //////////////////////////////////////////////////////////////////
    public int height() {
        return HEIGHT;
    }

    //////////////////////////////////////////////////////////////////
    // Read in the letters
    void initLetters() {
        int a, b, c;
        byte ch;     // the character of the letter
        int i, j, k;
        String s;    // A line in the font file
        boolean done;
        int width;

        // Just to make the compiler shut up about
        // these "may not be initialized".
        w = 5;
        h = 5;
        num = 100;

        try {
            // find the height
            done = false;
            while (!done) {
                s = dis.readLine();
                if (!s.startsWith("!!")) // If is not a comment line
                {
                    h = (new Integer(s)).intValue();
                    HEIGHT = h;
                    done = true;
                }
            }

            // find the width
            done = false;
            while (!done) {
                s = dis.readLine();
                if (!s.startsWith("!!")) // If is not a comment line
                {
                    w = (new Integer(s)).intValue();
                    done = true;
                }
            }

            // Find the number of characters
            done = false;
            while (!done) {
                s = dis.readLine();
                if (!s.startsWith("!!")) // If is not a comment line
                {
                    num = (new Integer(s)).intValue();
                    done = true;
                }
            }

            // The "num+1" allocates the extra array position for " " (space)
            index = new Index[num + 1];

            // Ok we gots the data, lets read in the characters!
            for (i = 0; i < num; i++) {
                // to make the compiler shut up about how
                // these "may not have been initialized"
                ch = 2;
                width = 10;

                //read the header for the letter
                done = false;
                while (!done) {
                    s = dis.readLine();
                    if (!s.startsWith("!!")) // If is not a comment line
                    {
                        ch = (byte) s.charAt(0);
                        done = true;
                    }
                }
                done = false;
                while (!done) {
                    s = dis.readLine();
                    if (!s.startsWith("!!")) // If is not a comment line
                    {
                        width = (new Integer(s)).intValue();
                        done = true;
                    }
                }

                // initialize the struct
                index[i] = new Index(ch, width, h);

                // read in the character
                for (j = 0; j < h; j++) {
                    done = false;
                    s = "";
                    while (!done) {
                        s = dis.readLine();

                        if (s.length() > 0) {
                            if (!s.startsWith("!!")) // If is not a comment line
                            {
                                done = true;
                            }
                        } else {
                            s = " ";
                            done = true;
                        }
                    }

                    for (k = 0; k < index[i].width; k++) {
                        if (k >= s.length()) {
                            index[i].letter[k][j] = false;
                        } else {
                            if (s.charAt(k) == '#')
                                index[i].letter[k][j] = true;
                            else
                                index[i].letter[k][j] = false;
                        }
                    }
                }
            } // end reading in the letters

            index[num] = new Index((byte) 32, swidth, h);

            // close the datastreams
            inputStream.close();
            dis.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    } // end of InitLetters()

    //////////////////////////////////////////////////////////////////
    // find the LED letter and return it
    public Index getLetter(char c) {
        int j;

        if (c == (char) (32)) {
            j = num; // I know where this one is!
        } else {
            // look for it
            j = 0;
            while (c != index[j].ch && j < num)
                j++;
        }

        return index[j];
    } // End getLetter()
} // End Letters Class
