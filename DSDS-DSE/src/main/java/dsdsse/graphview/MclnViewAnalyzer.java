package dsdsse.graphview;

import java.util.List;

/**
 * Created by Admin on 3/5/2017.
 */
final class MclnViewAnalyzer {

    public static DevelopmentStatus isMclnStructureComplete(List<MclnPropertyView> propertyViewList,
                                                            List<MclnConditionView> conditionViewList,
                                                            List<MclnArcView> arcViewList) {

        //
        //   Structural Analysis
        //

        int totalPropertyNodes = propertyViewList.size();
        int connectedProperties = 0;
        for (MclnPropertyView mclnPropertyView : propertyViewList) {
            if (mclnPropertyView.isConnected()) {
                connectedProperties++;
            }
        }

        int totalConditionNodes = conditionViewList.size();
        int connectedConditions = 0;
        for (MclnConditionView mclnConditionView : conditionViewList) {
            if (mclnConditionView.isConnected()) {
                connectedConditions++;
            }
        }

        int totalArcs = arcViewList.size();
        int connectedArcs = 0;
        for (MclnArcView mclnArcView : arcViewList) {
            if (mclnArcView.isConnected()) {
                connectedArcs++;
            }
        }

        // Initialization Summary

        int initializedProperties = 0;
        for (MclnPropertyView mclnPropertyView : propertyViewList) {
            if (mclnPropertyView.isInitialized()) {
                initializedProperties++;
            }
        }

        int initializedArcs = 0;
        for (MclnArcView mclnArcView : arcViewList) {
            if (mclnArcView.isInitialized()) {
                initializedArcs++;
            }
        }

        return new DevelopmentStatus(totalPropertyNodes, connectedProperties,
                totalConditionNodes, connectedConditions,
                totalArcs, connectedArcs,
                initializedProperties, initializedArcs);
    }
}
