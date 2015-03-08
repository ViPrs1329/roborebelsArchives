package org.stlpriory.robotics.scouter.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle;

import org.stlpriory.robotics.scouter.io.IDataStore;
import org.stlpriory.robotics.scouter.ui.table.MatchResultTable;
import org.stlpriory.robotics.scouter.ui.table.MatchScheduleTable;
import org.stlpriory.robotics.scouter.ui.table.TeamsTable;

public class DetailsPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    public static final int CONTENT_HSIZE = RoboScouter.CONTENT_HSIZE;
    public static final int CONTENT_VSIZE = RoboScouter.CONTENT_VSIZE;

    private JScrollPane contentScrollPane;
    private JComponent detailsContent;
    private JButton saveButton;
    private JLabel contentLabel;
    
    private IDataStore store;
    private RoboScouter mainGui;
    
    @SuppressWarnings("deprecation")
    public DetailsPanel(final IDataStore theBackingStore, final RoboScouter theMainGui) {
        this.store = theBackingStore;
        this.mainGui = theMainGui;
        
        setBorder(BorderFactory.createTitledBorder("Details"));
        setPreferredSize(new Dimension(CONTENT_HSIZE, CONTENT_VSIZE));
        
        this.contentScrollPane = new JScrollPane();

        this.contentScrollPane.setViewportView(this.detailsContent);
        this.contentScrollPane.setPreferredSize(new Dimension(CONTENT_HSIZE-10, CONTENT_VSIZE - 50));

        this.contentLabel = new JLabel();
        Font labelFont = this.contentLabel.getFont();
        this.contentLabel.setFont(new Font(labelFont.getName(), Font.BOLD, 2*labelFont.getSize()));
        this.contentLabel.setText("My content label");

        this.saveButton = new JButton();
        this.saveButton.setLabel("Save");
        this.saveButton.addActionListener(e -> previewSaveActionPerformed(e));

        GroupLayout jPanel1Layout = new GroupLayout(this);
        setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(this.contentLabel)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(this.contentScrollPane, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, CONTENT_HSIZE, Short.MAX_VALUE)
                    .addGroup(GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(this.saveButton)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(this.contentLabel)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addComponent(this.contentScrollPane, GroupLayout.PREFERRED_SIZE, CONTENT_VSIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(this.saveButton)
                .addGap(119, 119, 119))
        );
        
    }
    
    public void setContentView(final JComponent theContents) {
        this.detailsContent = theContents;
        this.contentScrollPane.setViewportView(this.detailsContent);
    }
    
    public void setContentLabel(String text) {
        this.contentLabel.setText(text);
    }

    private void previewSaveActionPerformed(final ActionEvent event) {
        if (this.detailsContent instanceof MatchResultTable) {
            ((MatchResultTable)this.detailsContent).saveTableUpdates(this.store);
            this.mainGui.getNavTreePanel().refresh();
            JOptionPane.showMessageDialog(this.contentScrollPane, "Successfully saved new match result");
            
        } else if (this.detailsContent instanceof TeamsTable) {
            ((TeamsTable)this.detailsContent).saveTableUpdates(this.store);
            this.mainGui.getNavTreePanel().refresh();
            JOptionPane.showMessageDialog(this.contentScrollPane, "Successfully saved team data");
            
        } else if (this.detailsContent instanceof MatchScheduleTable) {
            ((MatchScheduleTable)this.detailsContent).saveTableUpdates(this.store);
            this.mainGui.getNavTreePanel().refresh();
            JOptionPane.showMessageDialog(this.contentScrollPane, "Successfully saved match schedule data");
        }
    }

}
