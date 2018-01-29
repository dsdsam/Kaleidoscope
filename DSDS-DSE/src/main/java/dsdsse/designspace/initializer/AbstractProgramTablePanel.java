package dsdsse.designspace.initializer;

import adf.ui.components.tables.OneColumnTableModel;
import mcln.palette.MclnState;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by Admin on 5/9/2016.
 */
abstract class AbstractProgramTablePanel<T> extends JPanel {

    protected Font PLAIN_FONT = new Font(getName(), Font.PLAIN, 11);

    private static final Color c1 = new Color(0x3399FF);
    private static final Color lb = new Color(0xaaddFF);
    private static final Color c2 = new Color(0xff7f50);
    private static final Color c3 = new Color(0xff8c00);
    private static final Color c4 = new Color(0xffa07a);
    private static final Color c5 = new Color(0xffa500);
    private static final Color c6 = new Color(0xf4a460);
    private static final Color c7 = new Color(0xffd700);

    private static final Color TABLE_HEADER_BACKGROUND = InitAssistantUIColorScheme.TABLE_HEADER_BACKGROUND;
    private static final Color TIME_TABLE_HEADER_BACKGROUND = new Color(0xBBBB66);
    private static final Color RULE_TABLE_HEADER_BACKGROUND = new Color(0xFED6A8);
    private static final Color RULE_TABLE_HEADER_BACKGROUND6 = new Color(0xFFDBAD);
    private static final Color RULE_TABLE_HEADER_BACKGROUND5 = new Color(0xFFDB89);
    private static final Color RULE_TABLE_HEADER_BACKGROUND4 = new Color(0xdec79f);
    private static final Color RULE_TABLE_HEADER_BACKGROUND0 = new Color(0xffa07a);
    private static final Color TIME_TABLE_HEADER_BACKGROUND2 = new Color(0xa0d0FF);
    private static final Color TIME_TABLE_HEADER_BACKGROUND3 = new Color(0x82B2F6);

    private static final Color RULE_TABLE_HEADER_BACKGROUND_G = new Color(0xffd0aa);
    private static final Color TABLE_HEADER_FOREGROUND = InitAssistantUIColorScheme.TABLE_HEADER_FOREGROUND;
    private static final Color TABLE_BACKGROUND = Color.WHITE;
    private static final Color TABLE_SELECTION_BACKGROUND = InitAssistantUIColorScheme.TABLE_SELECTION_BACKGROUND;

    static final Color PHASE_COLUMN_ONE_ENABLED_BACKGROUND = new Color(0xFFE9E9);
    static final Color PHASE_ROW_ENABLED_BACKGROUND = new Color(0xFFE9E9);
    static final Color PHASE_DISABLE_BACKGROUND = new Color(0xC8C8C8);

//    static final Color PHASE_COLUMN_ONE_ENABLED_BACKGROUND = new Color(0xffD8D8);
//    static final Color PHASE_ROW_ENABLED_BACKGROUND = new Color(0xffD8D8);
//    static final Color PHASE_DISABLE_BACKGROUND = new Color(0xC8C8C8);


    private int[] columnWidth;

    OneColumnTableModel oneColumnTableModel;
    adf.ui.components.tables.AdfBasicTable table = null;
    JScrollPane scrollPanel = null;
    JViewport viewPort = null;
    java.util.List data = new ArrayList();
    JTextField cellTextField;

    private Color bgColor = Color.white;
    private Color selectionForeground = Color.black;


    final InitAssistantDataModel initAssistantDataModel;

    public AbstractProgramTablePanel(InitAssistantDataModel initAssistantDataModel, int[] columnWidth,
                                     OneColumnTableModel oneColumnTableModel) {
        this(initAssistantDataModel, columnWidth, null, oneColumnTableModel);
    }

    public AbstractProgramTablePanel(InitAssistantDataModel initAssistantDataModel, int[] columnWidth,
                                     ActionListener al, OneColumnTableModel oneColumnTableModel) {

        this.initAssistantDataModel = initAssistantDataModel;
        this.columnWidth = columnWidth;
//        this.parentActionListener = al;
        setOpaque(false);
        setLayout(new BorderLayout());
        IaInputGeneratingProgram isp = null;
//            new InitInputGeneratorPage( this instanceogtimeDrivenProgram );
        this.oneColumnTableModel = oneColumnTableModel;
    }

    protected void initContexts(TableCellRenderer tableCellRenderer) {

        table = new adf.ui.components.tables.AdfBasicTable(oneColumnTableModel);
        int n = oneColumnTableModel.getColumnCount();
        for (int i = 0; i < n; i++) {
            TableColumn tableColumn = table.getColumn(oneColumnTableModel.getColumnName(i));
            tableColumn.setWidth(columnWidth[i]);
//            table.setDefaultRenderer(oneColumnTableModel.getColumnClass(i), tableCellRenderer );
        }

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        if (table.getRowCount() <= 0) {
            ;//  tableView.setRowSelectionInterval( -1, -1 );
        } else {
            table.setRowSelectionInterval(0, 0);
        }

        table.getTableHeader().setBorder(null);
        table.getTableHeader().setFont(InitAssistantUIColorScheme.TABLE_HEADER_FONT);

        if (this instanceof TimeDrivenProgramTablePanel) {
            table.getTableHeader().setBackground(TIME_TABLE_HEADER_BACKGROUND);
        } else {
            table.getTableHeader().setBackground(RULE_TABLE_HEADER_BACKGROUND);
        }

        table.getTableHeader().setForeground(TABLE_HEADER_FOREGROUND);
        table.setBackground(Color.WHITE);
        table.setForeground(InitAssistantUIColorScheme.TABLE_FOREGROUND);

        table.setSelectionForeground(selectionForeground);
        table.setSelectionBackground(TABLE_SELECTION_BACKGROUND);

        scrollPanel = new JScrollPane(table);
        scrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        viewPort = scrollPanel.getViewport();

        scrollPanel.setBackground(TABLE_HEADER_BACKGROUND);
        viewPort.setBackground(TABLE_BACKGROUND);

        add(scrollPanel, BorderLayout.CENTER);
    }

    boolean isPhaseEnabled() {
        return initAssistantDataModel.getSelectedProgramHasPhase();
    }

    abstract void appendEmptyRow();

    abstract void insertEmptyRow();

    abstract void removeRow();

    protected class StringCellRenderer extends JLabel implements TableCellRenderer {
        Border border = null;
        boolean isBordered = true;

        public StringCellRenderer(boolean isBordered) {
            super();
            this.isBordered = isBordered;
            setOpaque(true); // MUST do this for the background to show up.
        }

        public Component getTableCellRendererComponent(JTable table,
                                                       Object value, boolean isSelected, boolean hasFocus, int row,
                                                       int column) {
            setBorder(null);
//            System.out.println(" Render " + value.getClass().getName() + " row " + row + " col " + column + "  " + value);

            if (initAssistantDataModel.isSelectedProgramTimeDrivenProgram() && row == 0) {
                if (!isPhaseEnabled()) {
                    setBackground(PHASE_DISABLE_BACKGROUND);
                    setForeground(Color.black);
                } else if (isSelected) {
                    setBackground(table.getSelectionBackground());
                    setForeground(table.getSelectionForeground());
                } else {
                    setBackground(PHASE_ROW_ENABLED_BACKGROUND);
                    setForeground(Color.black);
                }
            } else if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else {
                setBackground(table.getBackground());
                setForeground(Color.black);
            }

            int intValue = (Integer) value;
            if (intValue < 0) {
                setText("");
                return this;
            }

            String ticksValue = value.toString();
            if (ticksValue.indexOf("[") > -1)

            {
                setBackground(Color.red);
                setForeground(Color.white);
                Font font = getFont();
                setFont(new Font(font.getName(), font.BOLD, 11));
            } else {
                Font font = getFont();
                setFont(new Font(font.getName(), font.BOLD, 10));
            }

            String strValue = new String((String) ticksValue);
            if (!(ticksValue.indexOf("R") > -1))
                while (strValue.length() < 3) {
                    strValue = new String("0" + strValue);
                }

            setText(strValue);

            //Font font = getFont();
            //setFont( new Font( font.getName(), font.PLAIN, font.getSize() ));
            setHorizontalAlignment(JLabel.CENTER);

            return this;
        }
    }

    protected class TicksInputDocument extends PlainDocument {

        private String oldStr = null;
        private AttributeSet a = null;
        private String mess = null;
        private int columnInd;

        //        if (curInpSimProg.isRuleDrivenProgram())
//            columnInd = 2;
//        else
//            columnInd = 1;
//
        public TicksInputDocument(int columnInd, String programName) {
            this.columnInd = columnInd;
            mess = programName;
        }

        private int isStrValid(String str) {
            int a = 0;
            boolean rnd = false;
            try { //System.out.println("isStrValid "+str +"   "+str.length());
                str.trim();
                if (str.startsWith("R=")) {
                    rnd = true;
//            System.out.println("isStrValid 1 r/  "+str +"   "+str.length()+"   "+str.indexOf("R"));
                    str = str.substring(str.indexOf("R=") + 2);
//            System.out.println("isStrValid 2 r/ "+str +"   "+str.length());
                    if (str.length() > 0) {
                        a = Integer.parseInt(str);
                        if (a > 999)
                            return -2;
                    }
//              System.out.println("isStrValid F r/ "+str +"   "+str.length());
                } else if (str.startsWith("R")) {
                    rnd = true;
//            System.out.println("isStrValid 1 R "+str +"   "+str.length()+"   "+str.indexOf("R"));
                    str = str.substring(str.indexOf("R") + 1);
//            System.out.println("isStrValid 2 R "+str +"   "+str.length());
                    if (str.length() > 0) {
                        a = Integer.parseInt(str);
                        if (a > 999)
                            return -2;
                    }
//            System.out.println("isStrValid F R "+str +"   "+str.length());
                } else {
                    a = Integer.parseInt(str);
                    if (a > 999)
                        return -2;
                    //   System.out.println("strTicksToInt A:"+str+"  "+a);
                }
                //  System.out.println("strTicksToInt x:"+str+"  "+rnd);
            } catch (Exception e) {
                return -1;
            }
            //if ( rnd )
            //  a = -a;
            return a;
        }

        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {

            String tmpStr;
            // System.out.println(" insertString "+str+"
            // "+tableView.getSelectedRow());
            if (str == null || oldStr != null || str.length() == 0)
                return;

            int intVal;
            // tmpStr = cellTextField.getText();

            String t = super.getText(0, super.getLength());
            // System.out.println(" insertString "+str+" "+t );
            /*
             * try {
             *
             * System.out.println(" EDITED TEXT "+tmpStr ); String t =
             * super.getText( 0, super.getLength() ); // cellMeaning =
             * cellMeaningValue.getText(); System.out.println(" OLD STRIBG "+
             * t+" "+str ); if ( !(str.startsWith("R") || str.startsWith(":")) )
             * intVal = Integer.parseInt( str ); } catch ( Exception e ) {
             * System.out.println(" ERR STRIBG "+ " "+str ); intVal =
             * strTicksToInt( str ); if (intVal == 0)
             * parentDialog.getToolkit().beep(); return; }
             */
            str.trim();
            if (str.length() == 0 || str.indexOf(" ") > -1)
                return;
            str = str.toUpperCase();
            super.insertString(offs, str, a);
            String newStr = super.getText(0, super.getLength());
            // cellMeaning = cellMeaningValue.getText();
            // System.out.println(" NEW STRING "+ newStr+" "+str );
            int res = isStrValid(newStr);

            if (res < 0) {// System.out.println(" NEW STRING IS INVALID" );
                // fireActionPerformd( this, -1, "Ticks changed" );
                super.remove(0, super.getLength());
                super.insertString(0, t, a);
                super.insertString(offs, "[ " + str + " ]", a);
                cellTextField.setBackground(Color.red);
                cellTextField.setForeground(Color.white);
                Font font = cellTextField.getFont();
                cellTextField.setFont(new Font(font.getName(), font.BOLD, 11));

                oldStr = new String(t);
                this.a = a;
                cellTextField.repaint();


//                if (res == -1)
//                    MCLNMedia.messageDlg(null, 'M', "Ticks Setup Error",
//                            "Entered symbol \"" + str + "\" is not a number.\n"
//                                    + "Please, read Help topic \"What is the "
//                                    + mess + "-Driven ISP\"\n"
//                                    + "for more information about ISP format.");
//                if (res == -2)
//                    MCLNMedia.messageDlg(null, 'M', "Ticks Setup Error",
//                            "Entered ticks value should be less then 999.\n"
//                                    + "Please, read Help topic \"What is the "
//                                    + mess + "-Driven ISP\"\n"
//                                    + "for more information about ISP format.");

                oldStr = null;
                super.remove(0, super.getLength());
                super.insertString(0, t, a);
                cellTextField.setBackground(TABLE_SELECTION_BACKGROUND);
                cellTextField.setForeground(Color.black);
                font = cellTextField.getFont();
                cellTextField.setFont(new Font(font.getName(), font.BOLD, 11));
                res = isStrValid(t);
                // fireActionPerformd( this, -1, "Ticks changed" );
                /*
                 * MCLNMedia.showQuickHelp( curInpSimProg.isSelectedProgramTimeDrivenProgram()?
                 * MCLNMedia.QH_TDISP : MCLNMedia.QH_RDISP, true, false );
                 */
            } else {
                cellTextField.setBackground(TABLE_SELECTION_BACKGROUND);
                cellTextField.setForeground(Color.black);
                Font font = cellTextField.getFont();
                cellTextField.setFont(new Font(font.getName(), font.BOLD, 11));
                // fireActionPerformd( this, -1, "Ticks changed" );
            }
            // if ( eld != null && tableView != null)

            cellTextField.requestFocus();
            cellTextField.repaint();

            // if ( editedRow >= 0 )
            // {

            /*
             * if (rowIndexForEditor > 0 ) { tableModel.setValueAt(
             * cellTextField.getText(), rowIndexForEditor, columnInd ); }
             */
            // }
            // */
            // fireActionPerformd( this, -1, "Ticks changed" );
        }

        // ------------------------------------
        public void remove(int offs, int len) throws BadLocationException {
            /*
             * if ( oldStr != null ) { super.remove( 0, super.getLength() );
             * super.insertString( 0, oldStr, a); oldStr = null;
             * cellTextField.setBackground( selActiveBgColor );
             * cellTextField.setForeground( Color.black ); // int ind =
             * curStr.indexOf( return; }
             */
            super.remove(offs, len);
            int res = isStrValid(cellTextField.getText());
            // fireActionPerformd( this, res, "Ticks changed" );
            cellTextField.requestFocus();
            cellTextField.repaint();


            /*
             if (rowIndexForEditor > 0 )
             tableModel.setValueAt( cellTextField.getText(),
             rowIndexForEditor,
             columnInd );
             */
            //      fireActionPerformd( this, -1, "Ticks changed" );
            String t = super.getText(0, super.getLength());
            //     cellMeaning = cellMeaning.substring( 0, offs ) +
            //                   cellMeaning.substring( offs+len, cellMeaning.length() );
            //     System.out.println(" REM "+ t+ "   "+offs+"   "+len+"  "+tableView.getSelectedRow() );

        }
        /*
         public void remove( int offs, int len )
         throws BadLocationException
         {
         super.remove( offs, len );
         String t = super.getText( 0, super.getLength() );
         //     cellMeaning = cellMeaning.substring( 0, offs ) +
         //                   cellMeaning.substring( offs+len, cellMeaning.length() );
         System.out.println(" REM "+ t+ "   "+offs+"   "+len );
         }
         */
    }

    /**
     * @param table
     * @param value
     * @param isSelected
     */
    static void processStateColumn(JLabel labelToRender, JTable table, Object value, boolean isSelected) {

        if (value == null) {
            // processing empty row
            if (isSelected) {
                labelToRender.setBackground(table.getSelectionBackground());
                labelToRender.setForeground(table.getSelectionForeground());
            } else {
                labelToRender.setBackground(table.getBackground());
                labelToRender.setForeground(Color.black);
            }
        } else {
            int stateValue = ((MclnState) value).getRGB();
            Color stateColor = new Color(stateValue);
            Color contourColor = Color.DARK_GRAY;

            labelToRender.setBackground(stateColor);

            Border border;
            if (isSelected) {
                border = new CompoundBorder(BorderFactory.createMatteBorder(2, 10, 2, 10,
                        table.getSelectionBackground()), new LineBorder(contourColor));
            } else {
                border = new CompoundBorder(BorderFactory.createMatteBorder(2, 10, 2, 10, table.getBackground()),
                        new LineBorder(contourColor));
            }
            labelToRender.setBorder(border);
        }

        labelToRender.setText("");
    }

}
