package dsdsse.app;

/**
 * Created by IntelliJ IDEA.
 * User: XP Admin
 * Date: Jan 29, 2013
 * Time: 10:31:24 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DSDSSEConstants {
    
    // WhatToDo
    int DO_NOTHING = 0;
    int CREATE_NODES = 1;
    int CREATE_FRAGMENT = 2;
    int CREATE_ARCS = 3;
    int SET_STATE = 4;
    int RUN_MODEL = 5;
    int CREATE_V_TRAN = 6;
    int CREATE_H_TRAN = 7;
    int CREATE_RS_TRAN = 8;
    int CREATE_LS_TRAN = 9;
    int MOVE_ELEMENT = 10;
    int MOVE_MODEL = 11;
    int DELETE_ELEMENT = 12;
    // Operation steps
    int TAKE_NODE_PLACE = 1;
    int TAKE_FIRST_NODE = 2;
    int TAKE_SECOND_NODE = 3;
    int TAKE_TRAN_PLACE = 4;
    int TAKE_ARC_THIRD_POINT = 5;
    int TAKE_ARC_KNOT_OR_NODE = 6;
    int TAKE_ARC_KNOB = 7;

    int TAKE_ELEMENT = 8;
    int TAKE_ELEMENT_NEW_PLACE = 9;
    int TAKE_MODEL = 10;
    int TAKE_MODEL_NEW_PLACE = 11;
    int REMOVE_ELEMENT = 12;
// Grid operations
    int SET_GRID_ON = 13;
    int SET_GRID_OFF = 14;
    int SET_GRID_VISIBLE = 15;
    int SET_GRID_INVISIBLE = 16;

// Exec Mode
// static final int SEQUENTIAL_EXECUTION  =  1;
// static final int PARALLEL_EXECUTION    =  2;
    int DONT_EXEC = 0;
    int DDLS_EXEC = 1;
    int DDRS_EXEC = 2;
    int DEDS_EXEC = 3;

    // State rendering operations
    int STATE_RENDERING_IDEL = 0;
    int STATE_RENDERING_START = 1;
    int STATE_RENDERING_STOP = 2;
    int STATE_RENDERING_RESET = 3;
    int STATE_RENDERING_SUSPEND = 4;
    int STATE_RENDERING_RESUME = 5;

    // Run time property keys, values
    String NEVER_SHOW_TDISP_HELP = "NEVER_SHOW_TDISP_HELP";
    String NEVER_SHOW_RDISP_HELP = "NEVER_SHOW_RDISP_HELP";
    String TRUE = "TRUE";
    String FALSE = "FALSE";

    // Quick Helps
    String QH_TDISP = "QH_TDISP";
    String QH_RDISP = "QH_RDISP";
}
