package mclnmatrix.app;

import java.util.*;

public class AoSUtils {

    public static boolean showMetaSymbols(){
        return true;
    }

    // "\u25A0"; - black square
    // "\u2716"; - black cross
    // "\u25CF"; - black circle
    // "\u25CB"; - while circle

    // "\u25AA"; - small black square
    // "\u25FE"; - small while square
    // "\u25c6"; - black diamond
    // "\u25C7"; - while diamond
    // "\u25CF"; - black circle
    // "\u25CB"; - while circle

    public static final String NONE = "-";
    public static final String CONT = showMetaSymbols()? "\u25A0" : "#"; // black square
    public static final String DIFF = showMetaSymbols()? "\u2716" : "$"; // black cross
    public static final String SAME = showMetaSymbols()? "\u25CF" : "!"; // black circle
    public static final String OPST = showMetaSymbols()? "\u25CB" : "~"; // white circle

    public static String getNONE() {
        return NONE;
    }

    public static String getCONT() {
        return CONT;
    }

    public static String getDIFF() {
        return DIFF;
    }

    public static String getSAME() {
        return SAME;
    }

    public static String getOPST() {
        return OPST;
    }

    private static final String[] M_SET_SYMBOLS = {NONE, CONT, DIFF, SAME, OPST, "", "", "", "", ""}; // size = 10

    private static final Set<String> M_SET = new LinkedHashSet();

    static final String[] S_SET_SYMBOLS = {
            "A", "B", "C", "D", "E",
            "F", "G", "H", "I", "J",
            "K", "L", "M", "N", "O",
            "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y",
            "Z", " ", "_"};
    static final Set<String> S_SET = new HashSet();

    //    private static String[] XSet = new String[MSet.length + SSet.length];
    private static Map<String, Integer> symbolToIndexMap = new HashMap();

    public static final void initOperations() {
        M_SET.clear();
        S_SET.clear();
        for (int i = 0; i < M_SET_SYMBOLS.length; i++) {
            M_SET.add(M_SET_SYMBOLS[i]);
        }
        for (int i = 0; i < S_SET_SYMBOLS.length; i++) {
            S_SET.add(S_SET_SYMBOLS[i]);
        }
        printSets();
    }

    static {
        M_SET.clear();
        S_SET.clear();
        for (int i = 0; i < M_SET_SYMBOLS.length; i++) {
            M_SET.add(M_SET_SYMBOLS[i]);
        }
        for (int i = 0; i < S_SET_SYMBOLS.length; i++) {
            S_SET.add(S_SET_SYMBOLS[i]);
        }
        printSets();
    }

    private static final void printSets() {
        System.out.println();
        int i = 0;
        for (String symbol : M_SET) {
            if ((i % M_SET.size()) == 0) {
//                System.out.print("\"");
                System.out.println();
            } else {
                System.out.print(", ");
            }
            System.out.print("\"" + symbol + "\"");
            i++;
        }
        System.out.println();

        i = 0;
        for (String symbol : S_SET) {
            if ((i % M_SET.size()) == 0) {
//                System.out.print("\"");
                System.out.println();
            } else {
                System.out.print(", ");
            }
            System.out.print("\"" + symbol + "\"");
            i++;
        }
    }

//    static {
//        initOperations();
//
//    }

    static String[] getSSetAsArray() {
        String[] arrayOfSymbols = AoSUtils.S_SET.toArray(new String[AoSUtils.S_SET.size()]);
        return arrayOfSymbols;
    }

    private static final boolean isMetaSymbol(String symbol) {
        return M_SET.contains(symbol);
    }

    public static final boolean isStateSymbol(String symbol) {
        return S_SET.contains(symbol);
    }

//    static {
//        int XSetSize = MSet.length + SSet.length;
//        for (int i = 0; i < XSetSize; i++) {
//            if (i < MSet.length) {
//                XSet[i] = MSet[i].isEmpty()? " ":MSet[i];
//            } else {
//                XSet[i] = SSet[i-MSet.length];
//            }
//            symbolToIndexMap.put(XSet[i], i);
//        }
//
//        System.out.println();
//        for (int i = 0; i < XSetSize; i++) {
//            if ((i %  MSet.length) ==0) {
////                System.out.print("\"");
//                System.out.println();
//            }else{
//                System.out.print(", ");
//            }
//            System.out.print("\""+XSet[i]+"\"");
////            System.out.print("\""+symbolToIndexMap.get(XSet[i])+"\"");
//        }
//        System.out.println();
//    }

    /**
     * @param arg1
     * @param arg2
     * @return
     */
    public static String equals(String arg1, String arg2) {
        if (arg1.equalsIgnoreCase(NONE) || arg2.equalsIgnoreCase(NONE)) {
            return NONE;
        }
        if (arg1.equalsIgnoreCase(arg2)) {
            return SAME;
        } else {
            return DIFF;
        }
    }

    public static String applied(String arg1, String arg2) {
        if (arg1.equalsIgnoreCase(NONE) || arg2.equalsIgnoreCase(NONE)) {
            return NONE;
        }
        if (!arg1.equalsIgnoreCase(arg2)) {
            return SAME;
        } else {
            return DIFF;
        }
    }

    /**
     * @param arg1
     * @param arg2
     * @return
     */
    public static String conjunction(String arg1, String arg2) {

        if (arg1.equalsIgnoreCase(NONE) && arg2.equalsIgnoreCase(NONE)) {
            return NONE;
        }
        if (arg1.equalsIgnoreCase(NONE) && !arg2.equalsIgnoreCase(NONE)) {
            return arg2;
        }
        if (arg2.equalsIgnoreCase(NONE) && !arg1.equalsIgnoreCase(NONE)) {
            return arg1;
        }

        if (arg1.equalsIgnoreCase(SAME) && arg2.equalsIgnoreCase(SAME)) {
            return SAME;
        } else {
            return DIFF;
        }
    }

    /**
     * @param arg1
     * @param arg2
     * @return
     */
    public static String production(String arg1, String arg2) {
        if (arg1.equalsIgnoreCase(NONE) || arg2.equalsIgnoreCase(NONE)) {
            return NONE;
        }
        if (arg1.equalsIgnoreCase(SAME)) {
            return arg2;
        }
        if (arg2.equalsIgnoreCase(SAME)) {
            return arg1;
        }
        if (arg1.equalsIgnoreCase(arg2)) {
            return arg1;
        } else {
            return DIFF;
        }
    }

    /**
     * @param arg1
     * @param arg2
     * @return
     */
    public static String disjunction(String arg1, String arg2) {

        if (arg1.equalsIgnoreCase(NONE) && arg2.equalsIgnoreCase(NONE)) {
            return NONE;
        }
        if (arg1.equalsIgnoreCase(NONE) && !arg2.equalsIgnoreCase(NONE)) {
            return arg2;
        }
        if (arg2.equalsIgnoreCase(NONE) && !arg1.equalsIgnoreCase(NONE)) {
            return arg1;
        }

        if (arg1.equalsIgnoreCase(DIFF) && arg2.equalsIgnoreCase(DIFF)) {
            return DIFF;
        }
        if (arg1.equalsIgnoreCase(DIFF) && isStateSymbol(arg2)) {
            return arg2;
        }
        if (isStateSymbol(arg1) && arg2.equalsIgnoreCase(DIFF)) {
            return arg1;
        }
        if (arg1.equalsIgnoreCase(CONT) || arg2.equalsIgnoreCase(CONT)) {
            return CONT;
        }

        if (isStateSymbol(arg1) && isStateSymbol(arg2)) {
            if (arg1.equalsIgnoreCase(arg2)) {
                return arg1;
            } else {
                return CONT;
            }
        }
        return CONT;
    }

    /**
     * @param currentState
     * @param replacingState
     * @return
     */
    public static String application(String currentState, String replacingState) {
        return isMetaSymbol(replacingState) ? currentState : replacingState;
    }
}
