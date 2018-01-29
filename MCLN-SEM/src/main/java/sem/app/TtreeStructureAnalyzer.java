/**
 * 
 */
package sem.app;

/*
 * 
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * 
 */
public class TtreeStructureAnalyzer {
    static public Map<String, Node> linksMap = new HashMap<String, Node>();

    static public Map<String, Link> linkList = new HashMap<String, Link>();

    public static void traverseTree(String ind, List<DefectedLink> defectList,
	    Node rightTreeNode) {

	rightTreeNode.printName(ind);
	List<Node> children = rightTreeNode.getChildren();
	Node wrongTreeChild = null;
	for (Node child : children) {
	    Link link = linkList.get(child.getName());
	    if (link != null) {
		wrongTreeChild = linksMap.get(link.getNodeNameLeft());
		if (wrongTreeChild == null) {
		    Link linkParent = linkList.get(rightTreeNode.getName());
		    if (linkParent != null) {
			Node wrongTreeParent = linksMap.get(linkParent
				.getNodeNameLeft());
			DefectedLink defectedLink = new DefectedLink(
				wrongTreeParent.getName(), link
					.getNodeNameLeft(), "defected");
			defectList.add(defectedLink);
		    }
		}
	    }
	    traverseTree(ind + "    ", defectList, child);
	}
    }

    public static void traverseTreeNodesToMap(String ind, Node wrongTreeNode) {
	wrongTreeNode.printName(ind);
	linksMap.put(wrongTreeNode.getLink(), wrongTreeNode);
	System.out.println(ind + " Put in map, key:" + wrongTreeNode.getLink());

	List<Node> children = wrongTreeNode.getChildren();
	for (Node child : children) {
	    traverseTreeNodesToMap(ind + "    ", child);
	}

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
	    System.out.println(ind + "Name: " + nodeName + ",   Link: " + link);
	}

	public List<Node> getChildren() {
	    return children;
	}

	public String getLink() {
	    return link;
	}

	public String getName() {
	    return nodeName;
	}

	public void add(Node node) {
	    children.add(node);
	}
    }

    private static class Link {
	String nodeNameRight = "";

	String nodeNameLeft = "";

	List<Node> children = new ArrayList<Node>();

	public Link(String nodeNameRight, String nodeNameLeft) {
	    this.nodeNameRight = nodeNameRight;
	    this.nodeNameLeft = nodeNameLeft;
	}

	public String getNodeNameRight() {
	    return nodeNameRight;
	}

	public String getNodeNameLeft() {
	    return nodeNameLeft;
	}

	void printName(String ind) {
	    System.out.println(ind + " Link nodeNameRight: " + nodeNameRight
		    + ",   nodeNameLeft: " + nodeNameLeft);
	}

    }

    private static class DefectedLink {
	String nodeParentName = "";

	String nodeChildName = "";

	String status = "";

	List<Node> children = new ArrayList<Node>();

	public DefectedLink(String nodeParentName, String nodeChildName,
		String status) {
	    this.nodeParentName = nodeParentName;
	    this.nodeChildName = nodeChildName;
	    this.status = status;
	}

	public String getNodeParentName() {
	    return nodeParentName;
	}

	public String getNodeChildName() {
	    return nodeChildName;
	}

	void print() {
	    System.out.println("Defect Link nodeParentName: " + nodeParentName
		    + ",   nodeChildName: " + nodeChildName + ", Status="
		    + status);
	}

    }

    public static void main(String[] args) {
	List<DefectedLink> defectList = new ArrayList<DefectedLink>();
	Node n_a_00 = new Node("A", "B");
	Node n_a_01 = new Node("A-01", "B-01");
	Node n_a_02 = new Node("A-02", "B-02");
	Node n_a_03 = new Node("A-03", "B-03");
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

	// create defected LeftTree
	Node wn_a_00 = new Node("AA", "BB");
	Node wn_a_01 = new Node("AA-01", "BB-01");
	Node wn_a_02 = new Node("AA-02", "BB-02");
	Node wn_a_03 = new Node("AA-03", "BB-03");
	wn_a_00.add(wn_a_01);
	wn_a_00.add(wn_a_02);
	wn_a_00.add(wn_a_03);

	Node wn_a_011 = new Node("AA-011", "BB-011");
	Node wn_a_012 = new Node("AA-012", "BB-012");
	Node wn_a_013 = new Node("AA-013", "BB-013");
	wn_a_01.add(wn_a_011);
	wn_a_01.add(wn_a_012);
	wn_a_01.add(wn_a_013);

	Node wn_a_021 = new Node("AA-021", "BB-021");
	Node wn_a_022 = new Node("AA-022", "BB-022");
	Node wn_a_023 = new Node("AA-023", "BB-023");
	wn_a_02.add(wn_a_021);
	wn_a_02.add(wn_a_022);
	wn_a_02.add(wn_a_023);

	Node wn_a_031 = new Node("AA-031", "BB-031");
	Node wn_a_032 = new Node("AA-032", "BB-032");
	// Node wn_a_033 = new Node("AA-033", "B-033");
	wn_a_03.add(wn_a_031);
	wn_a_03.add(wn_a_032);
	// wn_a_03.add(wn_a_033);

	Node wn_a_0311 = new Node("AA-0311", "BB-0311");
	Node wn_a_0312 = new Node("AA-0312", "BB-0312");
	Node wn_a_0313 = new Node("AA-0313", "BB-0313");
	wn_a_031.add(wn_a_0311);
	wn_a_031.add(wn_a_0312);
	wn_a_031.add(wn_a_0313);

	TtreeStructureAnalyzer.traverseTreeNodesToMap("", wn_a_00);

	linkList.put("A", new Link("B", "BB"));
	linkList.put("A-01", new Link("B-01", "BB-01"));
	linkList.put("A-02", new Link("B-02", "BB-02"));
	linkList.put("A-03", new Link("B-03", "BB-03"));
	linkList.put("A-011", new Link("B-011", "BB-011"));
	linkList.put("A-012", new Link("B-012", "BB-012"));
	linkList.put("A-013", new Link("B-013", "BB-013"));
	linkList.put("A-021", new Link("B-021", "BB-021"));
	linkList.put("A-022", new Link("B-022", "BB-022"));
	linkList.put("A-023", new Link("B-023", "BB-023"));
	linkList.put("A-031", new Link("B-031", "BB-031"));
	linkList.put("A-032", new Link("B-032", "BB-032"));
	linkList.put("A-033", new Link("B-033", "BB-033"));
	linkList.put("A-0311", new Link("B-0311", "BB-0311"));
	linkList.put("A-0312", new Link("B-0312", "BB-0312"));
	linkList.put("A-0313", new Link("B-0313", "BB-0313"));

	TtreeStructureAnalyzer.traverseTree("", defectList, n_a_00);

	System.out.println("Found DefectedLinks=" + defectList.size());
	for (DefectedLink defectedLink : defectList) {
	    defectedLink.print();
	}
    }
}
