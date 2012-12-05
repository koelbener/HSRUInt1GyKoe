package domain;

import java.util.HashMap;
import java.util.Map;

import application.core.Texts;

public enum Condition {
    NEW(1, "General.condition.new", "cond_new.gif"), //
    GOOD(2, "General.condition.good", "cond_good.gif"), //
    DAMAGED(3, "General.condition.damaged", "cond_damaged.gif"), //
    WASTE(4, "General.condition.waste", "cond_waste.gif"), //
    LOST(5, "General.condition.lost", "cond_lost.gif");

    private String key;
    private final String icon;
    private final int order;
    private static final Map<Integer, Condition> ENTRIES = new HashMap<Integer, Condition>();

    Condition(int order, String key, String icon) {
        this.order = order;
        this.key = key;
        this.icon = icon;
    }

    static {
        for (Condition c : Condition.values()) {
            ENTRIES.put(c.order, c);
        }
    }

    @Override
    public String toString() {
        return Texts.get(getKey());
    }

    public String getKey() {
        return key;
    }

    public String getIcon() {
        return icon;
    }

    public boolean isWorseThan(Condition other) {
        return order > other.order;
    }

    public Condition getNextWorse() {
        int newOrder = order;
        if (newOrder < 5) {
            newOrder += 1;
        }
        return ENTRIES.get(newOrder);
    }

}