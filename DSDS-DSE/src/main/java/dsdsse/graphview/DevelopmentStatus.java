package dsdsse.graphview;

/**
 * Created by Admin on 3/5/2017.
 */
public class DevelopmentStatus {

    // structural summary
    private final int totalPropertyNodes;
    private final int connectedProperties;
    private final int totalConditionNodes;
    private final int connectedConditions;
    private final int totalArcs;
    private final int connectedArcs;

    // Initialization Summary
    private final int initializedProperties;
    private final int initializedArcs;

    DevelopmentStatus(int totalPropertyNodes, int connectedProperties,
                      int totalConditionNodes, int connectedConditions,
                      int totalArcs, int connectedArcs,
                      int initializedProperties, int initializedArcs) {
        this.totalPropertyNodes = totalPropertyNodes;
        this.connectedProperties = connectedProperties;
        this.totalConditionNodes = totalConditionNodes;
        this.connectedConditions = connectedConditions;
        this.totalArcs = totalArcs;
        this.connectedArcs = connectedArcs;

        // Initialization Summary
        this.initializedProperties = initializedProperties;
        this.initializedArcs = initializedArcs;
    }

    public int getTotalPropertyNodes() {
        return totalPropertyNodes;
    }

    public int getConnectedProperties() {
        return connectedProperties;
    }

    public int getTotalConditionNodes() {
        return totalConditionNodes;
    }

    public int getConnectedConditions() {
        return connectedConditions;
    }

    public int getTotalArcs() {
        return totalArcs;
    }

    public int getConnectedArcs() {
        return connectedArcs;
    }

    public int getInitializedProperties() {
        return initializedProperties;
    }

    public int getInitializedArcs() {
        return initializedArcs;
    }

    public boolean isMclnViewDevelopmentComplete() {
        return isMclnViewStructureComplete() && isMclnViewInitializationComplete();
    }

    public boolean isMclnViewStructureComplete() {
        return connectedProperties == totalPropertyNodes && connectedConditions == totalConditionNodes &&
                connectedArcs == totalArcs;
    }

    public boolean isMclnViewInitializationComplete() {
        return initializedProperties == totalPropertyNodes && initializedArcs == totalArcs;
    }
}
