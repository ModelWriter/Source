package synalp.parsing.dlSemantics;

import java.util.ArrayList;

/**
 * 
 * This class will be used to create a node from an input Jeni format literal.
 * Two nodes are considered to be equal if their identifier is the same.
 * @author bikash
 */

public class DLNode {

	private String identifier; // This is the sole thing that checks for equality of nodes.
	private String label;
	private ArrayList<DLNode> children;
	private DLNode parent;
	
	public DLNode() {
		this.children = new ArrayList<DLNode>();
		this.parent = null;
	}
	
	public DLNode(String identifier, String label) {
		this();
		this.identifier = identifier;
		this.label = label;
	}
	
	
	public DLNode(String identifier) {
		this();
		this.identifier = identifier;
	}


	public DLNode(String identifier, String label, ArrayList<DLNode> children) {
		this();
		this.identifier = identifier;
		this.label = label;
		this.children = children;
	}
	
	/*
	public void addChildren(ArrayList<DLNode> children) {
		for (DLNode child:children) {
			addChild(child);
		}
	}*/
	
	public void addChild(DLNode child) {
		children.add(child);
	}
	
	public void setParent(DLNode parent) {
		this.parent = parent;
	}
	
	public boolean hasParent() {
		return (parent==null?false:true);
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	public ArrayList<DLNode> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<DLNode> children) {
		this.children = children;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DLNode other = (DLNode) obj;
		if (identifier == null) {
			if (other.identifier != null)
				return false;
		} else if (!identifier.equals(other.identifier))
			return false;
		return true;
	}

	
	@Override
	public String toString() {
		return "DLNode [identifier=" + identifier + ", label=" + label + ", children=" + children + "]";
	}
}
