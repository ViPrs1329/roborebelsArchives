package org.stlpriory.robotics.scouter.ui.rater;

import java.awt.GridLayout;
import java.awt.Label;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.stlpriory.robotics.scouter.model.Category;

public class TeamCategoryRater extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private final static int HORIZONTAL_GAP = 0;
    private final static int VERTICAL_GAP   = 5;
    private final static int NUMBER_STARS   = 5;
    
    /** Map of Category to 5 star rating widget */
    private final Map<Category,StarRater> starRaters;
    
    /**
     * The constructor.
     */
    public TeamCategoryRater(final List<Category> theScoringCateogries) {
        setLayout(new GridLayout(2,0,HORIZONTAL_GAP,VERTICAL_GAP));
        setVisible(true);

        this.starRaters = new HashMap<>();
        for (Category c : theScoringCateogries) {
            StarRater starRater = new StarRater(NUMBER_STARS, 0f, 0);
            this.starRaters.put(c,starRater);
            
            add(starRater); 
            add(new Label(c.getDisplayName()));
        }
    }
    
    public int getSelection(final Category category) {
        StarRater rater = this.starRaters.get(category);
        return (rater != null ? rater.getSelection() : 0);
    }
    
    public void setSelection(final Category category, final int selection) {
        StarRater rater = this.starRaters.get(category);
        if (rater != null) {
            rater.setSelection(selection);            
        }
    }
    
    public float getRating(final Category category) {
        StarRater rater = this.starRaters.get(category);
        return (rater != null ? rater.getRating() : 0.0f);
    }
    
    public void setRating(final Category category, final float rating) {
        StarRater rater = this.starRaters.get(category);
        if (rater != null) {
            rater.setRating(rating);
        }
    }
    
    /**
     * Called to enable/disable.
     *
     * @param enabled True for enabled.
     */
    @Override
    public void setEnabled(final boolean enabled) {
        super.setEnabled(enabled);
        if (!enabled) {
            repaint();
        }
    }
    
    public static void main(String[] args) {
        final JFrame frame = new JFrame("Nested Layout Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        List<Category> cats = Arrays.asList(new Category("Cat1","Category One"), 
                                            new Category("Cat2","Category Two"),
                                            new Category("Cat3","Category Three"));
        final TeamCategoryRater gui = new TeamCategoryRater(cats);
        frame.add(gui);

        frame.setVisible(true);
        frame.setContentPane(gui);
        frame.setTitle("Test for TeamCategoryRater");
        frame.pack();
    }

}
