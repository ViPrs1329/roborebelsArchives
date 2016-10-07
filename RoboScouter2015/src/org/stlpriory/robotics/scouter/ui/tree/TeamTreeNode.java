package org.stlpriory.robotics.scouter.ui.tree;

public class TeamTreeNode extends javax.swing.tree.DefaultMutableTreeNode {
    private static final long serialVersionUID = 1L;
    private final int teamNumber;

    public TeamTreeNode(final Integer theTeamNumber, final String nodeName) {
        super(nodeName);
        this.teamNumber = theTeamNumber;
    }

    @Override
    public boolean getAllowsChildren() {
        return false;
    }

    public int getTeamNumber() {
        return this.teamNumber;
    }

}
