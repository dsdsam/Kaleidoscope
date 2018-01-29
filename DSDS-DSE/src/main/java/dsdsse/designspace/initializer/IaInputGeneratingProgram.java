package dsdsse.designspace.initializer;

import java.io.PrintWriter;
import java.util.Vector;

/**
 * Created by Admin on 6/6/2016.
 */
public class IaInputGeneratingProgram {

    public static final int TIME_PRG = 1;

    public static final int RULE_PRG = 2;

    private int prgType;

    private int[][] program = null;

    private Vector vecData = new Vector();

    private boolean editing = false;

    private int prgCurStep = -1;

    private int prgCurTicks = -1;

    private int prgTicksMax = -1;

    private boolean programHasPhase;

    public IaInputGeneratingProgram(boolean timeDrivenProgram) {
        super();
        if (timeDrivenProgram) {
            prgType = TIME_PRG;
        } else {
            prgType = RULE_PRG;
        }
    }

    // ===============================================================
    public IaInputGeneratingProgram(int[][] prog, boolean stateDriven) {
        if (stateDriven)
            setStateDrivenISProgram(prog);
        else
            setTimeDrivenISProgram(prog);
    }

    // ---------------------------------------------------------------
    public void setTimeDrivenISProgram(int[][] prog) {
        prgType = TIME_PRG;

        if (prog == null || prog.length <= 0) {
            program = null;
            return;
        }

        program = new int[prog.length][3];
        for (int i = 0; i < prog.length; i++) {
            program[i][0] = -1;
            program[i][1] = prog[i][0];
            program[i][2] = (prog[i][1] < 0) ? -1 : prog[i][1] & 0xFFFFFF;

            // System.out.println("Node: setProgram "+i+" "+program[i][0]+
            // " "+program[i][1] +" "+program[i][2] );
        }

        // set Phase Exist Flag
        program[0][0] = (program[0][1] != 0) && (program[0][2] >= 0) ? 1 : 0;
    }

    // ---------------------------------------------------------------
    private void setStateDrivenISProgram(int[][] prog) {
        prgType = RULE_PRG;

        if (prog == null || prog.length <= 0) {
            program = null;
            return;
        }

        program = new int[prog.length][3];
        for (int i = 0; i < program.length; i++) {
            program[i][0] = prog[i][0] & 0xFFFFFF;
            program[i][1] = prog[i][1];
            program[i][2] = (prog[i][2] < 0) ? -1 : prog[i][2] & 0xFFFFFF;
            // System.out.println("setProgram "+i+" "+program[i][0]+"
            // "+program[i][1] +" "+program[i][2] );
        }
    }

    // ---------------------------------------------------------------
    public void save(PrintWriter pw) {
        pw.println("" + prgType);
        if (program == null || program.length <= 0) {
            pw.println("0");
            return;
        }

        pw.println("" + program.length);
        for (int i = 0; i < program.length; i++) {
            pw.println("" + program[i][0]);
            pw.println("" + program[i][1]);
            pw.println("" + program[i][2]);
        }
    }

    // ---------------------------------------------------------------
    /*
         * public int retrieve( BufferedReader br ) { Integer wInt;
         *
         * wInt = MCLNModel.getInteger( br ); if (wInt == null ) return(-1);
         * prgType = wInt.intValue();
         *
         * wInt = MCLNModel.getInteger( br ); if (wInt == null ) return(-1); int
         * progLen = wInt.intValue(); if ( progLen <= 0 ){ program = null;
         * return 0; }
         *
         * program = new int[progLen][3]; for ( int i = 0; i < progLen; i++ )
         * for ( int j = 0; j < 3; j++ ) { wInt = MCLNModel.getInteger( br ); if
         * (wInt == null ) return(-1); program[i][j] = wInt.intValue(); } return
         * 0; }
         */
    // ---------------------------------------------------------------
    /*
         * public void retrieve( BufferedReader br ) throws IOException {
         * Integer wInt;
         *
         * wInt = MCLNModel.getInteger( br ); prgType = wInt.intValue();
         *
         * wInt = MCLNModel.getInteger( br ); int progLen = wInt.intValue(); if (
         * progLen <= 0 ){ program = null; throw new IOException( "Program does
         * not retrieved. Lenngth is 0" ); }
         *
         * program = new int[progLen][3]; for ( int i = 0; i < progLen; i++ )
         * for ( int j = 0; j < 3; j++ ) { wInt = MCLNModel.getInteger( br );
         * program[i][j] = wInt.intValue(); } }
         */
    // ================================================================
    // R u n n i n g p r o g r a m
    // ================================================================
    public int getProgramType() {
        return prgType;
    }

    // ----------------------------------------------------------------
    public boolean isProgramSet() {
        return program != null && program.length > 0;
    }

    // ----------------------------------------------------------------
    public boolean isTimeDrivenProgram() {
        return prgType == TIME_PRG;
    }

    // ----------------------------------------------------------------
    public boolean isRuleDrivenProgram() {
        return prgType == RULE_PRG;
    }

    // ----------------------------------------------------------------
    public boolean isEditing() {
        return editing;
    }

    // ----------------------------------------------------------------
    public void stopEditing() {
        vecData.removeAllElements();
        editing = false;
    }

    // ----------------------------------------------------------------
    public boolean hasPhase() {
        // if (isEditing())
        // {
        int[] phase = getPhase();
        boolean ret = (phase != null && isTimeDrivenProgram() && phase[0] == 1) ? true
                : false;
        // System.out.println("has phase " + ret);
        return ret;
        // }
        // return ( isProgramSet() &&
        // isSelectedProgramTimeDrivenProgram() && program[0][0] == 1)? true : false;
    }

    // ----------------------------------------------------------------
    public int[] getPhase() {
        return getProgramItem(0);
    }

    // ----------------------------------------------------------------
    public int[] getProgramItem(int ind) {
        int[] item = null;

        if (isEditing()) {

            if (getRowCount() < 1)
                return null;

            item = new int[3];
            if (isRuleDrivenProgram()) {
                item[0] = ((Integer) getValueAt(ind, 1)).intValue();
                String ticks = (String) getValueAt(ind, 2);
                item[1] = parseTicks(ticks);
                item[2] = ((Integer) getValueAt(ind, 3)).intValue();
            } else {
                item[0] = -1;
                String ticks = (String) getValueAt(ind, 1);
                item[1] = parseTicks(ticks);
                item[2] = ((Integer) getValueAt(ind, 2)).intValue();
            }
        } else {

            if (program == null || program.length <= 0)
                return null;

            item = new int[3];
            item[0] = program[ind][0];
            item[1] = program[ind][1];
            item[2] = program[ind][2];
            ;
        }

        if (isTimeDrivenProgram() && ind == 0)
            item[0] = ((item[1] != 0) && (item[2] >= 0)) ? 1 : 0;
        // System.out.println("getP "+item[0]+" "+ item[1] +" "+ item[2]);

        return item;
    }

    // ----------------------------------------------------------------
    private int getProgramItemTicks(int ind) {
        int prgTicksMax = 0;
	/*
         * if ( !isEditing() ){
         *
         * if ( program[ind][1] == 0 ) return prgTicksMax;
         *
         * if ( program[ind][1] > 0 ) prgTicksMax = program[ind][1]; else
         * prgTicksMax = 3 + (int)(Math.random()* Math.abs(program[ind][1]));
         *  } else {
         */
        int[] item = getProgramItem(ind);

        if (item[1] == 0)
            return prgTicksMax;

        if (item[1] > 0)
            prgTicksMax = item[1];
        else
            prgTicksMax = 3 + (int) (Math.random() * Math.abs(item[1]));

        return prgTicksMax;
    }

    // ----------------------------------------------------------------
    private int getEditedProgramItemTicks(int ind) {
        int prgTicksMax = 0;
        int[] item = getEditedProgramItem(ind);
        if (item[1] == 0)
            return prgTicksMax;

        if (item[1] > 0)
            prgTicksMax = item[1];
        else
            prgTicksMax = 3 + (int) (Math.random() * Math.abs(item[1]));

        return prgTicksMax;
    }

    // ----------------------------------------------------------------
    public void findRuleAndSetState(int curState) {
        if (isTimeDrivenProgram())
            return;

        for (int i = 0; i < program.length; i++) {
            // System.out.println("inpSimProg.findRuleAndSetState "+ i+" "+
            // curState
            // +" "+ program[i][0]);
            if (curState == program[i][0]) {
                prgTicksMax = getProgramItemTicks(i);
                prgCurTicks = 0;
                prgCurStep = i;
                // System.out.println( "inpSimProg.findRuleAndSetState "+ i+" "+
                // prgTicksMax);
                return;
            }
        }
        return;
    }

    // ----------------------------------------------------------------
    public boolean resetProgram(int curState) {
        // System.out.println("inpSimProg.resetProgram");
        prgTicksMax = 0;
        prgCurTicks = -1;

        if (!isProgramSet())
            return false;

        // reset state driven program
        if (isRuleDrivenProgram()) {
            // System.out.println( "inpSimProg.resetProgram state drive");
            findRuleAndSetState(curState);
            return true;
        }

        // reset time driven program
        if (hasPhase()) {
            int[] phase = getPhase();
            prgCurStep = 0;
            prgTicksMax = getProgramItemTicks(0); // Fasa
            // System.out.println("resetProgram: from phase"+" "+
            // phase[0]+" "+ phase[1]+" "+phase[2]+" "+prgTicksMax);
        } else {
            prgCurStep = 1;
            prgTicksMax = getProgramItemTicks(1);
            // System.out.println("resetProgram: from step"+" "+
            // program[0][0]+" "+ program[1][1]+" "+program[1][2]+"
            // "+prgTicksMax);
        }

        prgCurTicks = 0;
        return true;
    }

    // -----------------------------------------------------------------
    private int getStateDrivenProgramInput() {
        int nextState = -1;

        if (prgCurStep == -1)
            return nextState;
        if (isEditing()) {
            int[] item = getEditedProgramItem(prgCurStep);
            nextState = item[2];
        } else {
            nextState = program[prgCurStep][2];
        }
        prgCurStep = -1;
        prgCurTicks = -1;
        prgTicksMax = 0;
        // System.out.println("getConditionalInput"+ pgmCurTicks );
        return nextState;
    }

    // ----------------------------------------------------------------
    private int getTimeDrivenProgramInput() {
        // System.out.println(
        // "setNewProgInput: "+program[1][0]+" "+program[1][1]);
        int nextState = -1;
        // Color newInputColor;

        if (prgCurStep == -1)
            return nextState;

        if (isEditing()) {
            int[] item = getEditedProgramItem(prgCurStep);
            nextState = item[2];
            prgCurStep++;
            if (prgCurStep > (getRowCount() - 1))
                prgCurStep = 1;
            prgTicksMax = getEditedProgramItemTicks(prgCurStep);
        } else {
            nextState = program[prgCurStep][2];
            prgCurStep++;
            if (prgCurStep > (program.length - 1))
                prgCurStep = 1;
            prgTicksMax = getProgramItemTicks(prgCurStep);
        }

        prgCurTicks = 0;
        return nextState;
        // setRunState( newInputState );
        // System.out.println("pgmCurTicks"+ pgmCurTicks );
        // pgmActiveStateCnt = 0;
    }

    // ----------------------------------------------------------------
    public int getNextState() {
        int nextState = -1;
        // System.out.println("inpSimProg.getNextState "+ prgCurTicks );
        if (prgCurTicks < 0)
            return nextState;

        prgCurTicks++;
        if (prgCurTicks != prgTicksMax)
            return nextState;

        if (isRuleDrivenProgram())
            nextState = getStateDrivenProgramInput();
        else
            nextState = getTimeDrivenProgramInput();

        return nextState;
    }

    // ---------------------------------------------------------------
    public int getTicksLeft() {
        return (prgCurTicks > 0) ? (prgTicksMax - prgCurTicks) : prgTicksMax;
    }

    // ----------------------------------------------------------------
    private int parse_Ticks(String str) {
        int a = 0;
        boolean rnd = false;
        try {
            // System.out.println("isStrValid "+str +" "+str.length());
            str.trim();
            if (str.startsWith("R=")) {
                rnd = true;
                // System.out.println("isStrValid 1 R/ "+str +" "+str.length()+"
                // "+str.indexOf("R"));
                str = str.substring(str.indexOf("R=") + 2);
                // System.out.println("isStrValid 2 R/ "+str +" "+str.length());
                if (str.length() > 0)
                    a = Integer.parseInt(str);
                // System.out.println("isStrValid F R/ "+str +" "+str.length());
            } else if (str.startsWith("R")) {
                rnd = true;
                // System.out.println("isStrValid 1 R "+str +" "+str.length()+"
                // "+str.indexOf("R"));
                str = str.substring(str.indexOf("R") + 1);
                // System.out.println("isStrValid 2 R "+str +" "+str.length());
                if (str.length() > 0)
                    a = Integer.parseInt(str);
                // System.out.println("isStrValid F R "+str +" "+str.length());
            } else {
                a = Integer.parseInt(str);
                // System.out.println("strTicksToInt A:"+str+" "+a);
            }
            // System.out.println("strTicksToInt x:"+str+" "+rnd);
        } catch (Exception e) {
            return 1000;
        }
        if (a == 0)
            return a;

        if (rnd)
            a = -a;

        return a;
    }

    /*
         * private int parseTicks( String str ) { int a = 0; boolean rnd =
         * false; try { System.out.println("strTicksToInt "+str); str.trim(); if
         * (str.startsWith("R")) { rnd = true; str =
         * str.substring(str.indexOf("R")); a = Integer.parseInt( str );
         * System.out.println("strTicksToInt R "+str); } else if
         * (str.startsWith("R:")) { rnd = true; str =
         * str.substring(str.indexOf("R:"));
         *
         * System.out.println("strTicksToInt R:"+str); } else { a =
         * Integer.parseInt( str ); System.out.println("strTicksToInt A:"+str+"
         * "+a); } System.out.println("strTicksToInt x:"+str+" "+rnd); } catch(
         * Exception e ) { a = 0; } if ( rnd ) a = -a; return a; }
         */
    // ------------------------------------------------------------
    private int parseTicks(String str) {
        int a = 0;
        boolean rnd = false;
        try { // System.out.println("isStrValid "+str +" "+str.length());
            str.trim();
            if (str.startsWith("R=")) {
                rnd = true;
                // System.out.println("isStrValid 1 r/ "+str +" "+str.length()+"
                // "+str.indexOf("R"));
                str = str.substring(str.indexOf("R=") + 2);
                // System.out.println("isStrValid 2 r/ "+str +" "+str.length());
                if (str.length() > 0)
                    a = Integer.parseInt(str);
                // System.out.println("isStrValid F r/ "+str +" "+str.length());
            } else if (str.startsWith("R")) {
                rnd = true;
                // System.out.println("isStrValid 1 R "+str +" "+str.length()+"
                // "+str.indexOf("R"));
                str = str.substring(str.indexOf("R") + 1);
                // System.out.println("isStrValid 2 R "+str +" "+str.length());
                if (str.length() > 0)
                    a = Integer.parseInt(str);
                // System.out.println("isStrValid F R "+str +" "+str.length());
            } else {
                a = Integer.parseInt(str);
                // System.out.println("strTicksToInt A:"+str+" "+a);
            }
            // System.out.println("strTicksToInt x:"+str+" "+rnd);
        } catch (Exception e) {
            return 0;
        }
        if (rnd)
            a = -a;
        return a;
    }

    // ----------------------------------------------------------------
    public IaInputGeneratingProgram getProgramCopy() {
        IaInputGeneratingProgram isp = null;
        return isp;
	/*
         * int nItems = getRowCount(); if ( nItems < 1 ) return new int[0][3];
         *
         * int[][] locProgram = new int[nItems][3]; int[] item; for ( int i = 0;
         * i < nItems; i++ ) { item = getProgramItem( i ); locProgram[i][0] =
         * item[0]; locProgram[i][1] = item[1]; locProgram[i][2] = item[2];
         *
         * System.out.println("getCompleteProgram "+locProgram[i][0]+" "+
         * locProgram[i][1] +" "+ locProgram[i][2]); } return program;
         */
    }

    // ----------------------------------------------------------------
    // ------------- E d i t i n g ----------------------------------
    // ----------------------------------------------------------------
    public int[] getEditedProgramItem(int ind) {
        int[] item = null;

        if (!isEditing())
            return null;

        if (getRowCount() < 1)
            return null;

        item = new int[3];
        if (isRuleDrivenProgram()) {
            item[0] = ((Integer) getValueAt(ind, 1)).intValue();
            String ticks = (String) getValueAt(ind, 2);
            item[1] = parseTicks(ticks);
            item[2] = ((Integer) getValueAt(ind, 3)).intValue();
        } else {
            item[0] = -1;
            String ticks = (String) getValueAt(ind, 1);
            item[1] = parseTicks(ticks);
            item[2] = ((Integer) getValueAt(ind, 2)).intValue();
        }

        if (isTimeDrivenProgram() && ind == 0)
            item[0] = ((item[1] != 0) && (item[2] >= 0)) ? 1 : 0;
        // System.out.println("getItem "+item[0]+" "+ item[1] +" "+ item[2]);

        return item;
    }

    // ----------------------------------------------------------------
    private int[] getEditedProgramPhase() {
        return getEditedProgramItem(0);
    }

    // ----------------------------------------------------------------
    public boolean hasEditedProgramPhase() {
        if (!isEditing())
            return false;

        int[] item = getEditedProgramPhase();
        if (item == null)
            return false;
        // System.out.println("getPase "+item[0]+" "+ item[1] +" "+ item[2]);
        return (isTimeDrivenProgram() && item[0] == 1) ? true : false;

    }

    // ----------------------------------------------------------------
    public Vector getNewRow(int ind, int cond, int ticks, int state) {
        Vector row = new Vector();

        Integer intInd = new Integer(ind);
        row.addElement(intInd);

        if (isRuleDrivenProgram()) {
            Integer intCond = new Integer(cond);
            row.addElement(intCond);
        }
        String strTicks;
        if (ticks >= 0)
            strTicks = new String("" + ticks);
        else
            strTicks = new String("R=" + Math.abs(ticks));
        row.addElement(strTicks);
        Integer intState = new Integer(state);
        row.addElement(intState);

        return row;
    }

    // ----------------------------------------------------------------
    public Vector prepareForEditing_() {
        editing = true;
        if (vecData.size() > 0) {
            return vecData;
        }

        if (program == null) {
            // System.out.println("initProgramTableData: NO DATA
            // "+vecData.size() );
            return vecData;
        }

        int nRows = program.length;
        Vector row;
        // System.out.println("initProgramTableData: nRows = "+nRows );
        for (int i = 0; i < nRows; i++) {
            row = getNewRow(i, program[i][0], program[i][1], program[i][2]);
            vecData.insertElementAt(row, i);
            // System.out.println("initProgramTableData: = "+
            // program[i][0]+" "+program[i][1]+" "+program[i][2] );
        }
        // System.out.print
        return vecData;
    }

    // ----------------------------------------------------------------
    public void removeStatesFromTable() {

        int nRows = getRowCount();
        if (nRows < 1) {
            return;
        }

        Integer state = new Integer(-1);
        for (int i = 0; i < nRows; i++) {
            // System.out.println("removeStatesFromTable: "+ i);
            if (isTimeDrivenProgram())
                setValueAt(state, i, 2);
            else {
                setValueAt(state, i, 1);
                setValueAt(state, i, 3);
            }

        }

        // ((AbstractTableModel)tableModel).fireTableDataChanged();
    }

    // ----------------------------------------------------------------
    public boolean checkIfProgramHasState(int removedState) {
        return removeStateFromTable(removedState, true);
    }

    // ----------------------------------------------------------------
    public void removeStateFromTable(int removedState) {
        removeStateFromTable(removedState, false);
    }

    // ----------------------------------------------------------------
    private boolean removeStateFromTable(int removedState, boolean check) {
        int nRows = getRowCount();
        if (nRows < 1)
            return false;

        Integer state = new Integer(-1);
        Integer curIntState;
        for (int i = 0; i < nRows; i++) {
            // System.out.println("removeStatesFromTable: "+ i);
            if (isTimeDrivenProgram()) {
                curIntState = (Integer) getValueAt(i, 2);
                if (curIntState.intValue() == removedState)
                    if (check)
                        return true;
                    else
                        setValueAt(state, i, 2);
            } else {
                curIntState = (Integer) getValueAt(i, 1);
                if (curIntState.intValue() == removedState)
                    if (check)
                        return true;
                    else
                        setValueAt(state, i, 1);

                curIntState = (Integer) getValueAt(i, 3);
                if (curIntState.intValue() == removedState)
                    if (check)
                        return true;
                    else
                        setValueAt(state, i, 3);
            }

        }

        return false;
    }

    // ----------------------------------------------------------------
    public void takeProgramFromTable() {
        int nItems = getRowCount();
        if (nItems < 1) {
            program = new int[0][3];
            return;
        }

        program = new int[nItems][3];
        int[] item;
        for (int i = 0; i < nItems; i++) {
            item = getProgramItem(i);
            program[i][0] = item[0];
            program[i][1] = item[1];
            program[i][2] = item[2];

            // System.out.println("getCompleteProgram "+program[i][0]+" "+
            // program[i][1]
            // +" "+ program[i][2]);
        }

        stopEditing();
    }

    // ----------------------------------------------------------------
    // public void appendRow()
    // { vecData.addElement( getNewRow( 0, -1, 0, -1 ) ); }
    // ----------------------------------------------------------------
    public void insertRowAt(int ind) {
        vecData.insertElementAt(getNewRow(ind, -1, 0, -1), ind);
    }

    // ----------------------------------------------------------------
    public void removeRowAt(int ind) {
        vecData.removeElementAt(ind);
    }

    // ----------------------------------------------------------------
    // Methods for TableModel
    // ----------------------------------------------------------------
    public int getRowCount() {
        return vecData.size();
    }

    // ----------------------------------------------------------------
    public Object getValueAt(int row, int col) {
        return ((Vector) vecData.elementAt(row)).elementAt(col);
    }

    // ----------------------------------------------------------------
    public boolean isCellEditable(int row, int col) {
        if (isRuleDrivenProgram())
            return ((col == 2) ? true : false);
        else
            return ((col == 1) ? true : false);
    }

    // ----------------------------------------------------------------
    public void setValueAt(Object aValue, int row, int col) {
        // System.out.println("ISP Setting value to: " + row+" "+col+"
        // "+aValue);
        // System.out.println("Setting value to: " + aValue);
        // if ( row >= getRowCount() )
        if (getRowCount() <= 0)
            return;
        ((Vector) vecData.elementAt(row)).removeElementAt(col);
        ((Vector) vecData.elementAt(row)).insertElementAt(aValue, col);
    }
    // ----------------------------------------------------------------
    // ================================================================

}

