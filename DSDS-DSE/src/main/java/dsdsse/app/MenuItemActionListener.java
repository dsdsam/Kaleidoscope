package dsdsse.app;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: XP Admin
 * Date: May 11, 2013
 * Time: 8:38:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class MenuItemActionListener implements ActionListener{
       public void actionPerformed(ActionEvent e) {

        String cmd = e.getActionCommand();
//        MCLNMedia.appendToTrailFile(cmd);
//   System.out.println("MenuItemListener  "+cmd);
/*
        if ("MenuOpen".equals(cmd))
          onMenuOpen();
        if ("MenuClosed".equals(cmd))
          onMenuClosed();
*/
//        if ("New Proj".equals(cmd))
//          onNewProj();
//        if ("New Model".equals(cmd))
//          onNewModel();
//        if ("Register".equals(cmd))
//          onRegister();
//        if ("New Folder".equals(cmd))
//          onNewFolder();
//        if ("Open".equals(cmd))
//          onOpen();
//        if ("Save".equals(cmd))
//          onSave( false );
//        if ("Erase Proj".equals(cmd))
//          onEraseProj();
//        if ("Remove".equals(cmd))
//          onRemove();
//        if ("Save As".equals(cmd))
//          onSave( true );
//        if ("Print".equals(cmd))
//          onShowPrintPreviewContent();
//        if("Exit".equals(cmd))
//          onExit();
//
//   //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//   if ("Simple Blocks".equals(cmd))
//   {
//    String projName = "Simple Blocks";
//    removeProj();
//    setRetrievedProj( projName, projName, 1,
//                      -8., -5., 8., 5, 1., RtsCSys.DRAW_NOTHING );
//    createSimplestNets();
//    mclnMedia.setDemoPrj();
//    onSetRunMode();
//   }
//   if ("Three Rules".equals(cmd))
//   {
//    String projName = "Three_Rules";
//    removeProj();
//    setRetrievedProj( projName, projName, 1,
//                      -5., -5., 5., 5., 1., RtsCSys.DRAW_NOTHING );
//    createThreeRules( projName );
//    mclnMedia.setDemoPrj();
//    onSetRunMode();
//   }
//   if ("Trigger".equals(cmd))
//   {
//    String projName = "Trigger";
//    removeProj();
//    setRetrievedProj( projName, projName, 1,
//                      -8., -5., 8., 5, 1., RtsCSys.DRAW_NOTHING );
//    createTriggers( projName );
//    mclnMedia.setDemoPrj();
//    onSetRunMode();
//   }
//   if ("Processes".equals(cmd))
//   {
//    String projName = "Processes";
//    removeProj();
//    setRetrievedProj( projName, projName, 1,
//                      -10., -5., 10., 5., 1., RtsCSys.DRAW_NOTHING );
//    createSimpleProcesses( projName );
//    mclnMedia.setDemoPrj();
//    onSetRunMode();
//   }
//  /*
//   if ("Set Of Lights".equals(cmd))
//   {
//    String projName = "Set Of Lights";
//    removeProj();
//    setRetrievedProj( projName, projName, 1,
//                      -5., -5., 5., 5., 1., RtsCSys.DRAW_NOTHING );
//    mclnMedia.setDemoPrj();
//   }
//   */
//   if ("Dining Philosophers".equals(cmd))
//   {
//    String projName = "Dining Philosophers";
//    removeProj();
//    setRetrievedProj( projName, projName, 1,
//                      -2., -2.75, 2., 2.75, 0.5, RtsCSys.DRAW_NOTHING );
//    CreateDiningPhilosophers( projName );
//    mclnMedia.setDemoPrj();
//    onSetRunMode();
////    repaint();
//   }
//   /*
//   if ("Shared Resource with orbiter".equals(cmd))
//   {
//    String projName = "Shared Resource with orbiter";
//    removeProj();
//    setRetrievedProj( projName, projName, 1,
//                      -10., -5., 10., 5., 1., RtsCSys.DRAW_NOTHING );
//    CreateSharedResourceWithOrbiter( projName );
//    mclnMedia.setDemoPrj();
//   }
//   */
//   if ("Mutual Exclusion".equals(cmd))
//   {
//    String projName = "Mutual Exclusion";
//    removeProj();
//    setRetrievedProj( projName, projName, 1,
//                      -10., -5., 10., 5., 1., RtsCSys.DRAW_NOTHING );
//    CreateSharedResourceWithoutOrbiter( projName );
//    mclnMedia.setDemoPrj();
//    onSetRunMode();
//   }
//   //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//   if ("Repaint".equals(cmd))
//    onRepaint();
//   if ("Cells".equals(cmd))
//    onAddNodes();
//   if ("Fragments".equals(cmd))
//     onAddFragment();
//   if ("Vertical Junctions".equals(cmd))
//     onAddVTran();
//   if ("Horizontal Junctions".equals(cmd))
//     onAddHTran();
//   if ("Right Slanted Junctions".equals(cmd))
//     onAddRSTran();
//   if ("Left Slanted Junctions".equals(cmd))
//     onAddLSTran();
//   if ("Arcs".equals(cmd))
//     onAddArcs();
//   // -----------------------
//   if ("Launch Initialization Assistent".equals(cmd))
//     onSetInitState();
//   // ------------------------
//   if ("Discrete Dynamic Linear System".equals(cmd))
//     onDDLS();
//   if ("Discrete Dynamic Random System".equals(cmd))
//     onDDRS();
//   if ("Discrete Event Dynamic System".equals(cmd))
//     onDEDS();
//   // -------------------------
//   if ("Move Element".equals(cmd))
//     onMoveElement();
//   if ("Move Model".equals(cmd))
//     onMoveModel();
//
//   if ("Run Model".equals(cmd))
//     onStartRunning();
//   if ("Delete Element".equals(cmd))
//     onDeleteElement();
//
//   if ("How to Run DDSSE".equals(cmd))
//     onHelp();
//
//   if (cmd.equals("What is the Time-Driven ISP"))
//     MCLNMedia.showQuickHelp(MCLNMedia.QH_TDISP, false, true );
//
//   if (cmd.equals("What is the Rule-Driven ISP"))
//     MCLNMedia.showQuickHelp(MCLNMedia.QH_RDISP, false, true );
//
//   if ( cmd.equals("About") )
//   {
//     MCLNMedia.showAboutDlg();
//   /*
//    RtsMedia.messageDlg( parentFrame, 'M', "About DDSSE",
//    "The Discrete Dynamic System Simulating Environmemt\n"+
//    "\n                        version  1.01.\n\n"+
//    "                     by Vladimir Lakin\n\n\n"+
//    "mail:       vlakin1995@.aol.com\n"+
//    "www:     www@members.aol.com/vlakin1995\n"+
//    "");
//    */
//    return;
//   }
//
//   if ("Set Node".equals(cmd))
//     onSetNode();
//   if ("Set Junction".equals(cmd))
//     onSetTran();
//   if ("Set Arc".equals(cmd))
//     onSetArc();
//
//   if ( cmd.equals( "Start Running" ) )
//     onStartRunning();
//   if ( cmd.equals( "Stop Running" ) )
//     onStopRunning();
//   if ( cmd.equals( "Edit Model" ) )
//     onAddNodes();
//
//     // Not the menu action
//   if ( cmd.equals( "Init State" ) )
//     setEditMode( SET_STATE, DO_NOTHING );
  }
}
