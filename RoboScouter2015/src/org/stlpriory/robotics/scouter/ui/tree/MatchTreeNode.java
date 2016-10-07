package org.stlpriory.robotics.scouter.ui.tree;

public class MatchTreeNode extends javax.swing.tree.DefaultMutableTreeNode {

    private static final long serialVersionUID = 1L;
    private final int matchNumber;

    public MatchTreeNode(final Integer theMatchNumber, final String nodeName) {
        super(nodeName);
        this.matchNumber = theMatchNumber;
    }

    @Override
    public boolean getAllowsChildren() {
        return false;
    }

    public int getMatchNumber() {
        return this.matchNumber;
    }

}
