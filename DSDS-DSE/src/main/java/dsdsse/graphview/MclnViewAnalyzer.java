package dsdsse.graphview;

import mclnview.graphview.MclnArcView;
import mclnview.graphview.MclnConditionView;
import mclnview.graphview.MclnPropertyView;

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
        for (MclnPropertyView mcLnPropertyView : propertyViewList) {
            if (mcLnPropertyView.isConnected()) {
                connectedProperties++;
            }
        }

        int totalConditionNodes = conditionViewList.size();
        int connectedConditions = 0;
        for (MclnConditionView mcLnConditionView : conditionViewList) {
            if (mcLnConditionView.isConnected()) {
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
        for (MclnPropertyView mcLnPropertyView : propertyViewList) {
            if (mcLnPropertyView.isInitialized()) {
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
