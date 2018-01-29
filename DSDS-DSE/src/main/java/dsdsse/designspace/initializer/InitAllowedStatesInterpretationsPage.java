package dsdsse.designspace.initializer;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;

/**
 * Created by Admin on 4/20/2016.
 */
public class InitAllowedStatesInterpretationsPage extends JPanel {

    private static final Dimension LABEL_SIZE = new Dimension(300, 16);

    private final InitAssistantDataModel initAssistantDataModel;

    private final JLabel componentNameLabel = new JLabel("Modeled system component name:", JLabel.LEFT);
    private final JLabel propertyNameLabel = new JLabel("The component's property name:", JLabel.LEFT);
//    private final JLabel paletteSelectorLabel = new JLabel("Available palettes:", JLabel.LEFT);

    private String componentName;
    private String propertyName;

    private final JTextField componentNameInputField = new JTextField( );
    private final JTextField propertyNameInputField  = new JTextField( );;

    InitAllowedStatesInterpretationsPage(InitAssistantDataModel initAssistantDataModel) {
        super(new BorderLayout());
        this.initAssistantDataModel = initAssistantDataModel;
        setOpaque(true);
        setBackground(InitAssistantUIColorScheme.CONTAINER_PANEL_BACKGROUND);
        Border border = BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15), BorderFactory.createEtchedBorder()
        );
        setBorder(border);
        initContents();
    }

    private JPanel initContents() {

        componentName = initAssistantDataModel.getComponentName();
        propertyName = initAssistantDataModel.getPropertyName();

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setOpaque(true);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(InitAssistantUIColorScheme.INPUT_PANEL_BACKGROUND);

        componentNameLabel.setPreferredSize(LABEL_SIZE);
        setCompFontSize(componentNameLabel, Font.BOLD, 11);
        componentNameLabel.setForeground(Color.BLUE);

        propertyNameLabel.setPreferredSize(LABEL_SIZE);
        setCompFontSize(propertyNameLabel, Font.BOLD, 11);
        propertyNameLabel.setForeground(Color.BLUE);

//        paletteSelectorLabel.setPreferredSize(LABEL_SIZE);
//        setCompFontSize(paletteSelectorLabel, Font.BOLD, 11);
//        paletteSelectorLabel.setForeground(Color.BLUE);

        componentNameInputField.setDocument(new ComponentNameInputDocument());
        componentNameInputField.setText(componentName);
        setCompFontSize(componentNameInputField, Font.BOLD, 13);

        propertyNameInputField.setDocument(new PropertyNameInputDocument());
        propertyNameInputField.setText(propertyName);
        setCompFontSize(propertyNameInputField, Font.BOLD, 13);

//        JComboBox paletteSelectionComboBox = new ComboBox3D(PALETTES);
//        paletteSelectionComboBox.setPreferredSize(new Dimension(300, 23));



        mainPanel.add(componentNameLabel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(6, 0, 0, 0), 0, 0));

        mainPanel.add(componentNameInputField, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

        mainPanel.add(Box.createVerticalStrut(9), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

        mainPanel.add(propertyNameLabel, new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

        mainPanel.add(propertyNameInputField, new GridBagConstraints(0, 4, 1, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

        mainPanel.add(Box.createVerticalStrut(9), new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

//        mainPanel.add(paletteSelectorLabel, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0,
//                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
//
//        mainPanel.add(paletteSelectionComboBox, new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0,
//                GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

        mainPanel.add(Box.createGlue(), new GridBagConstraints(0, 8, 1, 1, 0.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));

//        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

// String title = "  The Cell Properties  ";
// Border etched = BorderFactory.createEtchedBorder();
// TitledBorder titleBorder = BorderFactory.createTitledBorder(
//                            etched, title );
// titleBorder.setTitleJustification( TitledBorder.CENTER );
// titleBorder.setTitleColor( borderTitleColor );
// Font font = titleBorder.getTitleFont();
// titleBorder.setTitleFont( new Font( font.getName(), font.BOLD, 11 ));
//        mainPanel.add(nameLabel);
//        mainPanel.add(objectNameInputField);


/*
 cellMeaningValue.addActionListener( new ActionListener()
 {
  public void executeMenuOperation( ActionEvent ae )
  {
   cellMeaning = cellMeaningValue.getText();
   System.out.println("JTextField  "+cellMeaning );
  }
 } );
*/

//        mainPanel.add(initCellIdnStatePanel());

//        JCheckBox inpCellCheckBox = new JCheckBox(
//                " The Cell has Input Simulating Program ", false);
//        inpCellCheckBox.setSelected(propertyHasProgram);
//        setCompFontSize(inpCellCheckBox, Font.BOLD, 11);
//        inpCellCheckBox.setOpaque(false);
////        inpCellCheckBox.addItemListener(new ItemListener() {
////                                            public void itemStateChanged(ItemEvent e) {
////                                                if (e.getStateChange() == ItemEvent.SELECTED) {
////                                                    nextButton.setText(nextStr);
////                                                    propertyHasProgram = true;
////                                                    notePanel.setNote(2, step2of3Str);
////                                                    notePanel.repaint();
////                                                } else {
////                                                    nextButton.setText(applyStr);
////                                                    propertyHasProgram = false;
////                                                    notePanel.setNote(2, step2of3Str);
////                                                    notePanel.repaint();
////                                                }
////                                            }
////                                        }
////        );
//        mainPanel.add(Box.createRigidArea(new Dimension(0, 3)));
//        mainPanel.add(inpCellCheckBox);

        this.add(mainPanel, BorderLayout.CENTER);
        return mainPanel;
    }

    private class ComponentNameInputDocument extends PlainDocument {
        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
            super.insertString(offs, str, a);
            componentName = componentNameInputField.getText();
//            System.out.println(" NEW STRIBG " + cellMeaning + "   " + str);
            initAssistantDataModel.setComponentName(componentName);
        }

        public void remove(int offs, int len) throws BadLocationException {
            super.remove(offs, len);
            componentName = componentName.substring(0, offs) +
                    componentName.substring(offs + len, componentName.length());
//            System.out.println(" REM " + cellMeaning + "   " + offs + "   " + len);
            initAssistantDataModel.setComponentName(componentName);
        }
    }

    private class PropertyNameInputDocument extends PlainDocument {
        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
            super.insertString(offs, str, a);
            propertyName = propertyNameInputField.getText();
//            System.out.println(" NEW STRIBG " + cellMeaning + "   " + str);
            initAssistantDataModel.setPropertyName(propertyName);
        }

        public void remove(int offs, int len) throws BadLocationException {
            super.remove(offs, len);
            propertyName = propertyName.substring(0, offs) +
                    propertyName.substring(offs + len, propertyName.length());
//            System.out.println(" REM " + cellMeaning + "   " + offs + "   " + len);
            initAssistantDataModel.setPropertyName(propertyName);
        }
    }

    private final void setCompFontSize(Component comp, int style, int fontSize) {
        Font font = comp.getFont();
        comp.setFont(new Font(font.getName(), style, fontSize));
    }


//    processCell( boolean initDoneFlag, String cellId,
//                 String cellMeaning, int state,
//                 int origInitialStateInd,
//                 int[] allowedStates,
//                 String[] allowedStatesMining,
//                 InitInputGeneratorPage isp )
//    {
//// System.out.println("processCell "+showIntroduction);
//// if (showIntroduction)
////  return;
//        makeTryStateButton( false );
//// System.out.println("processCell 2");
//        orgAllowedStates = allowedStates;
//        orgAllowedStatesMining = allowedStatesMining;
//
//// this.initDoneFlag = initDoneFlag;
//        this.cellId = cellId;
//        this.cellMeaning = new String( cellMeaning );
//        cellOrigMeaning = new String( cellMeaning );
//        origState = state;
//        this.origInitialStateInd = origInitialStateInd;
//        selectedInitialStateInd = origInitialStateInd;
//// System.out.println("processCell initDoneFlag "+ this.initDoneFlag );
//// System.out.println("processCell selectedInitialStateInd "+this.selectedInitialStateInd);
//        cellPropPanel = null ;
//        cellProgramPanel = null;
//
//// initialStateIsSet = initDoneFlag;
//        initialStateIsSet = (origInitialStateInd != -1);
//// if ( initDoneFlag )
//        selectedInitialState = state;
//// else
//// //  selectedInitialState = -1;
//
//// orgProgram = program;
//// orgStateDrivenProgram = stateDrivenProgram;
//// System.out.println("processCell 2 ");
//        if ( isp == null )
//        {
//            programComplete = propertyHasProgram = false;
//            timeDrivenISP = InitInputGeneratorPage.instanceOf( null, false );
//            ruleDrivenISP = InitInputGeneratorPage.instanceOf( null, true );
//            // this.stateDrivenProgram = false;
//            curInpSimProg = timeDrivenISP;
//            phaseNeeded = false;
////  System.out.println("orgProgram == null ");
//        }
//        else
//        {
//            programComplete = propertyHasProgram = true;
//
// /*
//   for( int i = 0; i < orgProgram.length; i++ )
//   {
//    newProgram[i][0] = orgProgram[i][0];
//    newProgram[i][1] = orgProgram[i][1];
//    newProgram[i][2] = orgProgram[i][2];
//   }
//  */
////   this.stateDrivenProgram = stateDrivenProgram;
//            curInpSimProg = isp;
//            if ( isp.isRuleDrivenProgram() ){
//                timeDrivenISP = InitInputGeneratorPage.instanceOf( null, false );
//                ruleDrivenISP = curInpSimProg;
//            }else{
//                ruleDrivenISP = InitInputGeneratorPage.instanceOf( null, true );
//                timeDrivenISP = curInpSimProg;
//                phaseNeeded = curInpSimProg.isPhase();
//            }
//
//        }
//        timeDrivenISP.prepareForEditing_();
//        ruleDrivenISP.prepareForEditing_();
//        setupMode = CELL_SETUP;
///*
// if ( MCLNMedia.setupAdviserNewPrijFlag == true )
//   return;
//*/
////System.out.println("CELL "+showIntroduction);
//        if ( showIntroduction )
//        {
////  System.out.println("Created Cell dialog with Intro");
//            cellSetupState = -2;
//            initSetupLayout();
//            notePanel.setIntrodNote( introductionHeaderStr, introductionTextStr );
//            showIntroduction = false;
////  System.out.println("Created Cell dialog with Intro"+cellSetupState);
//            show();
//        } else
//        {
//            setInitAllowedStatesState();
//// System.out.println("Created Cell dialog without Intro"+cellSetupState);
// /*
//   System.out.println("Created Cell dialog without Intro");
//  cellSetupState = 0;  // for to ignor table state changes
//  initSetupLayout();
//  cellSetupState = 1;
//  notePanel.setNote( cellSetupState, step1of3Str );
//  makeTryStateButton( false );
//  show();
//  stateTable.init();
//  */
//        }
//        visible = true;
//// System.out.println("processCell 2 "+selectedInitialState);
//// repaint();
//// initCellPropertySetupLayout();
//    }
}
