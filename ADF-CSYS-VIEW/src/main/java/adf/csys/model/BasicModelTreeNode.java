package adf.csys.model;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.NoSuchElementException;

import javax.swing.tree.TreeNode;

/**
 * Implements the requirements for an object that can be used as a tree node in
 * a JTree.
 * <p/>
 * <p/>
 * <p/>
 * For further information and examples of using tree nodes, see <a
 * href="http://java.sun.com/docs/books/tutorial/uiswing/components/tree.html">How
 * to Use Tree Nodes</a> in <em>The Java Tutorial.</em>
 *
 * @author xpadmin
 */
public class BasicModelTreeNode implements ModelTreeNode {

    /**
     * An enumeration that is always empty. This is used when an enumeration
     * of a leaf node's children is requested.
     */
    /*
         * static public final Enumeration<TreeNode> EMPTY_ENUMERATION = new
         * Enumeration<TreeNode>() { public boolean hasMoreElements() { return
         * false; } public TreeNode nextElement() { throw new
         * NoSuchElementException("No more elements"); } };
         */

    private List<ModelTreeNode> subnodeList = new ArrayList();

    private BasicModelTreeNode parentNode;

    private ModelTreeNode rootNode;

    private String name = "";

    private String fullName = null;

    private String nodeId;

    /**
     * true if the node is able to have children
     */
    protected boolean allowsChildren = true;

    public BasicModelTreeNode(String name, String nodeId) {
        this.name = name;
        this.nodeId = nodeId;
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @return Returns the fully cvalified name.
     */
    public String getFullName() {
        if (fullName == null) {
            String parentFullName = null;
            if (parentNode == null) {
                return name;
            }
            parentFullName = parentNode.getFullName();
            fullName = parentFullName + "." + name;
        }
//        System.out.println("fullName " + fullName);
        return fullName;
    }

    public String getNodeId() {
        return nodeId;
    }

    /**
     * @return
     */
    public BasicModelTreeNode getTopNode() {
        if (parentNode == null) {
            return this;
        }
        return parentNode.getTopNode();
    }

    public ModelTreeNode findNode(String fullName) {
        BasicModelTreeNode topNode = getTopNode();
        ModelTreeNode modelTreeNode = topNode.getNode(fullName);

        return modelTreeNode;

    }

    private String getFirstName(String fullName) {
        String name = null;
        int dotInxed = fullName.indexOf(".");
        if (dotInxed < 0) {
            name = fullName;
        } else {
            name = fullName.substring(0, dotInxed);
        }
        return name;
    }

    private String getRestOfName(String fullName) {
        String name = null;
        int dotInxed = fullName.indexOf(".");
        if (dotInxed < 0) {
            name = EMPTY_STRING;
        } else {
            name = fullName.substring(dotInxed + 1);
        }
        return name;
    }

    public ModelTreeNode getNode(String fullName) {
        if (fullName.equalsIgnoreCase(EMPTY_STRING)) {
            return null;
        }
        String firstName = getFirstName(fullName);
        if (firstName.equalsIgnoreCase(EMPTY_STRING)) {
            return null;
        }
        if (!firstName.equalsIgnoreCase(nodeId)) {
            return null;
        }
        String restOfFullName = getRestOfName(fullName);
        if (restOfFullName.equalsIgnoreCase(EMPTY_STRING)) {
            return this;
        }
        ModelTreeNode foundModelTreeNode = null;
        for (ModelTreeNode modelTreeNode : subnodeList) {
            foundModelTreeNode = modelTreeNode.getNode(restOfFullName);
            if (foundModelTreeNode != null) {
                break;
            }
        }
        return foundModelTreeNode;
    }

    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    public void addModelTreeNode(ModelTreeNode modelTreeNode) {
        subnodeList.add(modelTreeNode);
        modelTreeNode.setParentNode(this);
    }

    /**
     * @return Returns the parentNode.
     */
    public ModelTreeNode getParentNode() {
        return parentNode;
    }

    /**
     * @param parentNode The parentNode to set.
     */
    public void setParentNode(ModelTreeNode parentNode) {
        this.parentNode = (BasicModelTreeNode) parentNode;
    }

    public ModelTreeNode getTreeRoot() {
        if (rootNode != null) {
            return rootNode;
        }
        if (parentNode != null) {
            rootNode = parentNode.getTreeRoot();
        } else {
            rootNode = this;
        }
        return rootNode;
    }

    /**
     * @return Returns the subnodeList.
     */
    public List getSubnodeList() {
        return subnodeList;
    }

    /**
     * @param subnodeList The subnodeList to set.
     */
    public void setSubnodeList(List subnodeList) {
        this.subnodeList = subnodeList;
    }

    public void printTree(String offset) {
        TraverciveExecuter traverciveExecuter = new TraverciveExecuter() {
            public Object exec(ModelTreeNode modelTreeNode, Object obj) {
//                System.out.println("^^^" + ((String) obj) + modelTreeNode.getName());
                return super.exec(modelTreeNode, ((String) obj) + "  ");
            }
        };
        traverciveExecuter.exec(this, offset);
    }

    public class TraverciveExecuter {

        public Object exec(ModelTreeNode modelTreeNode, Object obj) {
            return iterateThroughSubNodes(modelTreeNode, obj);
        }

        protected Object iterateThroughSubNodes(ModelTreeNode modelTreeNode,
                                                Object obj) {
            List subnodeList = modelTreeNode.getSubnodeList();
            int n = subnodeList.size();
            for (int i = 0; i < n; i++) {
                ModelTreeNode currentModelTreeNode = (ModelTreeNode) subnodeList
                        .get(i);
                this.exec(currentModelTreeNode, obj);
            }
            return null;
        }
    }

    /**
     * Returns true if <code>aNode</code> is a child of this node. If
     * <code>aNode</code> is null, this method returns false.
     *
     * @return true if <code>aNode</code> is a child of this node; false
     *         if <code>aNode</code> is null
     */
    public boolean isNodeChild(TreeNode aNode) {
        boolean retval;

        if (aNode == null) {
            retval = false;
        } else {
            if (getChildCount() == 0) {
                retval = false;
            } else {
                retval = (aNode.getParent() == this);
            }
        }

        return retval;
    }

    /**
     * Determines whether or not this node is allowed to have children. If
     * <code>allows</code> is false, all of this node's children are
     * removed.
     * <p/>
     * Note: By default, a node allows children.
     *
     * @param allows true if this node is allowed to have children
     */
    public void setAllowsChildren(boolean allows) {
        if (allows != allowsChildren) {
            allowsChildren = allows;
            if (!allowsChildren) {
                subnodeList.clear();
            }
        }
    }

    // ******************************************************************
    // The implementation of TreeNode interface
    // ******************************************************************

    /**
     * Returns the child <code>TreeNode</code> at index
     * <code>childIndex</code>.
     */
    public TreeNode getChildAt(int childIndex) {
        return (TreeNode) subnodeList.get(childIndex);
    }

    /**
     * Returns the number of children <code>TreeNode</code>s the receiver
     * contains.
     */
    public int getChildCount() {
        return subnodeList.size();
    }

    /**
     * Returns the parent <code>TreeNode</code> of the receiver.
     */
    public TreeNode getParent() {
        return parentNode;
    }

    /**
     * Returns the index of <code>node</code> in the receivers children.
     * If the receiver does not contain <code>node</code>, -1 will be
     * returned.
     */
    public int getIndex(TreeNode node) {
        if (node == null) {
            throw new IllegalArgumentException("argument is null");
        }

        if (!isNodeChild(node)) {
            return -1;
        }
        return subnodeList.indexOf(node); // linear search

    }

    /**
     * Returns true if the receiver allows children.
     */
    public boolean getAllowsChildren() {
        return allowsChildren;
    }

    /**
     * Returns true if the receiver is a leaf.
     */
    public boolean isLeaf() {
        return subnodeList.size() == 0;
    }

    /**
     * Returns the children of the receiver as an <code>Enumeration</code>.
     */
    public Enumeration children() {
        if (subnodeList == null) {
            return null;// EMPTY_ENUMERATION;
        } else {
            return new Enumeration() {
                int count = 0;

                public boolean hasMoreElements() {
                    return count < subnodeList.size();
                }

                public Object nextElement() {

                    if (count < subnodeList.size()) {
                        return subnodeList.get(count++);
                    }

                    throw new NoSuchElementException(
                            "ModelTreeNode Enumeration");
                }
            };
        }

    }
}
