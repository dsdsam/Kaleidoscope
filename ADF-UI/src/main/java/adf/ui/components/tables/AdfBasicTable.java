package adf.ui.components.tables;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableModel;

public class AdfBasicTable extends JTable{
    
    private TableModel tableModel;
    private JScrollPane scrollPanel;
    private JViewport viewPort;
    private List data = new ArrayList();
    
    private int       firstVisibleIndex;
    private int       lastVisibleIndex  = 6;
    private int       nVisibleRows;
    
    public AdfBasicTable(){
        this( null );
    }
    
    public AdfBasicTable( TableModel tableModel ){
        super( tableModel );
    }
    
    public void selectRow( int newRowInd ){
        clearSelection();
        if ( newRowInd >= 0 ){
             setRowSelectionInterval( newRowInd, newRowInd );
        }
    }
    
    public void setViewPortToIndex( int ind ){
     Rectangle rect = viewPort.getViewRect();
//     boolean in = tableView.getScrollableTracksViewportHeight();
//    System.out.println(" rowHeight "+ rowHeight+"   "+rect.y+"  "+(rowHeight * ind));
     rect.setLocation( rect.x, rowHeight * ind );
//      rect.setLocation( rect.x, rowHeight * ind );
//     rect.height = rowHeight * 3;
     setVisibleRowIndexes( ind );
     scrollRectToVisible( rect );
//     viewPort. setScrollPosition( 0, 150 );
//     System.out.println(" rowHeight "+ rowHeight+"   "+in+"  "+(rowHeight * ind));
    }
    
    protected void setVisibleRowIndexes( int firstVisibleIndex ){
     Rectangle viewPortRect = viewPortRect = viewPort.getViewRect();
//     System.out.println(" set vis before "+ vecData.size()+"   "
//     +firstVisibleIndex+"  "+lastVisibleIndex+"  "+nVisibleRows );
     this.firstVisibleIndex = firstVisibleIndex;
     if ( (data.size() - firstVisibleIndex) > nVisibleRows )
     {
       lastVisibleIndex = firstVisibleIndex + nVisibleRows - 1;
//        System.out.println(" set 2"+ vecData.size()+"   "
     //+firstVisibleIndex+"  "+lastVisibleIndex+"  "+nVisibleRows );
     }
     else
       lastVisibleIndex = data.size() - 1;
     //   System.out.println(" set vis after "+ vecData.size()+"   "
     //+firstVisibleIndex+"  "+lastVisibleIndex+"  "+nVisibleRows );
    }
    
    public void viewPortScrolledUP( int offset ){
        // offset = (offset / rowHeight) * rowHeight;
        // viewPortRect = viewPort.getViewRect();
        
        // System.out.println(" offset "+ offset);
        
        
         Rectangle rect = viewPort.getViewRect();
         int res = offset - (offset / rowHeight) * rowHeight;
        // int adj = (res < 8 ) ? -res: rowHeight - res;
         int adj = -res;
        // adj = rowHeight - adj;
         offset = rect.y + adj;
         //System.out.println(" adj "+ adj );
         rect.setLocation( rect.x, rect.y + adj );
         scrollRectToVisible( rect );
        
         int rowOffset = (offset) / rowHeight;
        // int rowOffset = (offset+(rowHeight/2)) / rowHeight;
        
         setVisibleRowIndexes( rowOffset );
        // firstVisibleIndex = ;
        // lastVisibleIndex = firstVisibleIndex + nVisibleRows-1;
        
        // System.out.println(" rowOffset "+ rowOffset);
        // System.out.println(" nVisibleRows "+ nVisibleRows );
        // System.out.println(" firstVisibleIndex "+ firstVisibleIndex );
        // System.out.println(" lastVisibleIndex "+ lastVisibleIndex );
         //System.out.println(" viewPortRect.height "+ viewPortRect.height );
        
         ListSelectionModel rowSM = getSelectionModel();
         int index0 =  rowSM.getAnchorSelectionIndex();
        // System.out.println(" index0 "+ index0);
         if ( firstVisibleIndex <= index0 && index0 <= lastVisibleIndex )
          return;
         //   System.out.println(" set "+ (firstVisibleIndex) );
         //tableView.setRowSelectionInterval( firstVisibleIndex,
           //                                 firstVisibleIndex );
         //tableView.repaint();
//         parentDialog.repaint();
    }
}
