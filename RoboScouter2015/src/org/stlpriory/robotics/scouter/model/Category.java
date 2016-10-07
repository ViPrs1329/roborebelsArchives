package org.stlpriory.robotics.scouter.model;


/**
 * Represents a scoring category
 */
public class Category {
    private final String name;
    private final String displayName;
    private final Integer minValue;
    private final Integer maxValue;

    public Category(final String theName, 
                    final String theDisplayName) {
        this(theName, theDisplayName, null, null);
    }

    public Category(final String theName, 
                    final String theDisplayName,
                    final Integer theMinValue, 
                    final Integer theMaxValue) {
        
        if (theName == null || theName.length() == 0) {
            throw new IllegalArgumentException("The category name cannot be null or empty");
        }
        this.name = theName.trim();
        this.displayName = (theDisplayName != null && theDisplayName.length() > 0 ? theDisplayName.trim() : this.name);        
        this.minValue = (theMinValue == null ? Integer.MIN_VALUE : theMinValue);
        this.maxValue = (theMaxValue == null ? Integer.MAX_VALUE : theMaxValue);
    }

    public String getName() {
        return this.name;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public Integer getMinValue() {
        return this.minValue;
    }

    public Integer getMaxValue() {
        return this.maxValue;
    }

    @Override
    public String toString() {
        return this.displayName;
    }

    @Override
    public int hashCode() {
        return this.name.toLowerCase().hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Category)) {
            return false;
        }
        Category that = (Category) obj;
        return this.name.toLowerCase().equals(that.name.toLowerCase());
    }

}
