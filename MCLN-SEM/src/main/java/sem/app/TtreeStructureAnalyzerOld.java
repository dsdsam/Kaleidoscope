/**
 * 
 */
package sem.app;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 *
 */
public class TtreeStructureAnalyzerOld {
    
    
    public static List traverseTree(String ind, List defectList, Node rightTreeNode,
	    Node wrongTreeNode){
	
	rightTreeNode.printName(ind);
	List<Node> children = rightTreeNode.getChildren();
	Node wrongTreeChild = null;
	for(Node child : children ){
	    traverseTree(ind+"    " ,defectList, child, wrongTreeChild);
	}
	return defectList;
    }
    
    private static class Node {
        String nodeName = "";
        String link = "";
 
        List<Node> children = new ArrayList<Node>();
 
        public Node(String nodeName, String link) {
            this.nodeName = nodeName;
            this.link = link;
        }
 
        void printName(String ind) {
            System.out.println(ind+"Name: " + nodeName+",   Link: "+link);
        }
        
        public List<Node> getChildren(){
            return children;
        } 
 
        
        public void add(Node node) {
            children.add(node);
        }
    }
 
    public static void main(String[] args) {
	List defectList = new ArrayList();
	Node n_a_00 = new Node("A", "B");
	Node n_a_01 = new Node("A-01", "B-01");
	Node n_a_02 = new Node("A-02", "B-01");
	Node n_a_03 = new Node("A-03", "B-01");
        n_a_00.add(n_a_01);
        n_a_00.add(n_a_02);
        n_a_00.add(n_a_03);
        
        Node n_a_011 = new Node("A-011", "B-011");
        Node n_a_012 = new Node("A-012", "B-012");
        Node n_a_013 = new Node("A-013", "B-013");
        n_a_01.add(n_a_011);
        n_a_01.add(n_a_012);
        n_a_01.add(n_a_013);
        
        Node n_a_021 = new Node("A-021", "B-021");
        Node n_a_022 = new Node("A-022", "B-022");
        Node n_a_023 = new Node("A-023", "B-023");
        n_a_02.add(n_a_021);
        n_a_02.add(n_a_022);
        n_a_02.add(n_a_023);
        
        Node n_a_031 = new Node("A-031", "B-031");
        Node n_a_032 = new Node("A-032", "B-032");
        Node n_a_033 = new Node("A-033", "B-033");
        n_a_03.add(n_a_031);
        n_a_03.add(n_a_032);
        n_a_03.add(n_a_033);
        
        Node n_a_0311 = new Node("A-0311", "B-0311");
        Node n_a_0312 = new Node("A-0312", "B-0312");
        Node n_a_0313 = new Node("A-0313", "B-0313");
        n_a_031.add(n_a_0311);
        n_a_031.add(n_a_0312);
        n_a_031.add(n_a_0313);
        TtreeStructureAnalyzerOld.traverseTree("", defectList, n_a_00, null);
    }
}
