/*
 * Created on Aug 15, 2005
 *
 */
package adf.csys.model;

import java.util.List;

import javax.swing.tree.TreeNode;

/**
 * @author xpadmin
 *
 */
public interface ModelTreeNode extends TreeNode, ModelConstants{
    
    public void addModelTreeNode( ModelTreeNode modelTreeNode );

    public ModelTreeNode getNode(String fullName );

    /**
     * @return Returns the parentNode.
     */
    public ModelTreeNode getParentNode();

    /**
     * @param parentNode The parentNode to set.
     */
    public void setParentNode(ModelTreeNode parentNode);
    
    public ModelTreeNode getTreeRoot();
    
    public String getName();

    /**
     * @param name The name to set.
     */
    public void setName(String name);
    
    /**
     * @return Returns the subnodeList.
     */
    public List getSubnodeList();

    /**
     * @param subnodeList The subnodeList to set.
     */
    public void setSubnodeList(List subnodeList);

}
