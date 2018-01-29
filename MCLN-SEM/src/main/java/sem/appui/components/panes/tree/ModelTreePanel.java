package sem.appui.components.panes.tree;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import sem.mission.explorer.model.SpaceExplorerModel;
import adf.csys.model.ModelTreeNode;
import adf.ui.components.panels.ImagePanel;

public class ModelTreePanel extends ImagePanel{

    private JScrollPane scrollPane;
    private JTree seModelTree;
    private SpaceExplorerModel spaceExplorerModel;

    private static ModelTreePanel modelTreePanel;

    public static ModelTreePanel getInstanse(){
        return modelTreePanel;
    }

    public ModelTreePanel( ImageIcon imageIcon ){
        super( imageIcon );
        init();
        modelTreePanel = this;
    }

    private void init(){
        setBackground( Color.BLACK );
        setLayout( new BorderLayout() );
        scrollPane = new JScrollPane();
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(2,0,0,0));
//
//        tree.setCellRenderer( renderer );
//        tree.addTreeSelectionListener(new TreePanelListener());

        add( scrollPane, BorderLayout.CENTER );
    }

    public void buildModelRepresentation( SpaceExplorerModel spaceExplorerModel ){
        this.spaceExplorerModel = spaceExplorerModel;
        seModelTree = initTree( spaceExplorerModel );
        scrollPane.getViewport().add(seModelTree);
        expandeTree();
    }

    /**
     *
     * @param spaceExplorerModel
     * @return
     */
    private JTree initTree( SpaceExplorerModel spaceExplorerModel ){
        if (spaceExplorerModel == null){
           throw new IllegalArgumentException("SpaceExplorerModel is null");
        }else{
           System.out.println("Model "+spaceExplorerModel.getName());
        }

        this.spaceExplorerModel = spaceExplorerModel;
    /*
    DefaultMutableTreeNode western =
           new DefaultMutableTreeNode("Western Conference");
    DefaultMutableTreeNode pacific =
           new DefaultMutableTreeNode("Pacific Division Teams");
     western.add(pacific);
     theSys.add(western);
     */
        JTree tree = new JTree( spaceExplorerModel );
        tree.setBorder( BorderFactory.createEmptyBorder(3,5,5,5) );
        tree.setCellRenderer( new ModelTreeRenderer() );
    //     tree.setForeground( Color.red );
//        tree.setBackground( bgColor );
        tree.setOpaque(false);
        tree.putClientProperty( "JTree.lineStyle", "Angled" );
        tree.setShowsRootHandles(true);
        tree.setRootVisible(false);
 //       tree.expandPath( new TreePath(new Object[]{spaceExplorerModel, spaceExplorerModel.getChildAt(0) }));
        return tree;
    }

    public void addTreeSelectionListener( TreeSelectionListener treeSelectionListener ){
        if( seModelTree == null ){
            throw new RuntimeException("addTreeSelectionListener: Tree is null" );
        }
        seModelTree.addTreeSelectionListener( treeSelectionListener );
    }

    /**
     *
     * @param list
     */
    private void expandeNode( List list ){
        seModelTree.expandPath( new TreePath(list.toArray() ));
        List nodeList = ((ModelTreeNode)list.get(list.size()-1)).getSubnodeList();

        int n = nodeList.size();
        for( int i = 0; i < n; i++ ){
            list.add(nodeList.get(i));
            expandeNode( list );
            list.remove(list.size()-1);
        }
    }

    /**
     *
     *
     */
    public void expandeTree(){
        if (spaceExplorerModel == null){
            return;
        }
        List list = new ArrayList();
        list.add(spaceExplorerModel);
        expandeNode( list );
    }

}
