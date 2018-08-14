package dsdsse.designspace.initializer;

import dsdsse.graphview.DesignerArcView;
import mcln.model.*;
import mcln.palette.MclnState;
import mcln.palette.MclnStatesNewPalette;
import mclnview.graphview.MclnArcView;
import mclnview.graphview.MclnPropertyView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 6/22/2016.
 */
class ArcDataHolder extends DataHolder {

    static ArcDataHolder createArcDataHolder(MclnStatesNewPalette mclnStatesPalette, MclnStatement mclnProperty,
                                             DesignerArcView mclnArcView) {
        return new ArcDataHolder(mclnStatesPalette, mclnArcView);
    }

    private MclnStatesNewPalette mclnStatesPalette;
    private final List<MclnState> mclnStatesPaletteAsList = new ArrayList();
    private String componentName = "";
    private String propertyName = "";
    private final DesignerArcView mclnArcView;
    private final MclnArc mclnArc;

    // Property Attributes
    private final List<MclnStatementState> propertyAllowedStatesList = new ArrayList();

    private MclnState originalArcMclnState;
    private MclnState selectedArcMclnState;
    private Polygon arcArrowPoints;
    private boolean recognizingArc;

    /**
     * @param mclnStatesPalette
     * @param mclnArcView
     */
    ArcDataHolder(MclnStatesNewPalette mclnStatesPalette, DesignerArcView mclnArcView) {
        assert mclnArcView != null : "mclnArcView must not be null";

        this.mclnStatesPalette = mclnStatesPalette;
        this.mclnArcView = mclnArcView;
        this.mclnArc = mclnArcView.getTheElementModel();

        MclnStatement mclnProperty;
        if (mclnArcView.getInpNode() instanceof MclnPropertyView) {
            recognizingArc = true;
            MclnPropertyView inputPropertyView = mclnArcView.getInpNode().toPropertyView();
            mclnProperty = inputPropertyView.getTheElementModel();
        } else {
            recognizingArc = false;
            MclnPropertyView outputPropertyView = mclnArcView.getOutNode().toPropertyView();
            mclnProperty = outputPropertyView.getTheElementModel();
        }

        List<MclnStatementState> allowedStatesList = mclnProperty.getPropertyStatesAsList();
        propertyAllowedStatesList.addAll(allowedStatesList);

        originalArcMclnState = mclnArc.getArcMclnState();
        selectedArcMclnState = originalArcMclnState;

        componentName = mclnProperty.getSubject();
        propertyName = mclnProperty.getPropertyName();
        arcArrowPoints = mclnArcView.buildTriangleArrow(30, 12, new double[]{0, 0, 0});
    }

    //
    //   default getters used to populate Property pages
    //

    boolean isPairsOfOppositeStatesPalette() {
        return mclnStatesPalette.isPairsOfOppositeStatesPalette();
    }

    @Override
    public List<MclnState> geMclnStatesPaletteAsList() {
        return mclnStatesPaletteAsList;
    }

    boolean[] getPaletteSelectedStates() {
        return new boolean[0];
    }

    public MclnStatementState getMclnStatementStateByIndex(int index) {
//        if (index >= currentAllowedStatesList.size()) {
//            showInfoMessage("Requested Mcln Statement State index exceeded list size ");
//            return null;
//        }
        return propertyAllowedStatesList.get(index);
    }

    //
    //   Arc attribute getters and setters
    //

    boolean isRecognizingArc() {
        return recognizingArc;
    }

    @Override
    public Polygon getArcArrowPoints() {
        return arcArrowPoints;
    }

    @Override
    MclnArc getMclnArc() {
        return mclnArc;
    }

    @Override
    public String getComponentName() {
        return componentName;
    }

    @Override
    public String getPropertyName() {
        return propertyName;
    }

    @Override
    final List<MclnStatementState> getAllowedStatesList() {
        return propertyAllowedStatesList;
    }

    @Override
    final void setArcSelectedMclnState(MclnState selectedArcMclnState) {
        this.selectedArcMclnState = selectedArcMclnState;
        System.out.println(" arcSelectedMclnState " + selectedArcMclnState.toString());
    }

    @Override
    final MclnState getOriginalArcMclnState() {
        return originalArcMclnState;
    }

    @Override
    final MclnState getArcSelectedMclnState() {
        return selectedArcMclnState;
    }

    //
    //   M o d i f i c a t i o n   c h e c k
    //

    @Override
    boolean isEntityModified() {
        boolean assignedStateChanged = isAssignedStateChanged();
        return assignedStateChanged;
    }

    private boolean isAssignedStateChanged() {
        return (originalArcMclnState == null && selectedArcMclnState != null) ||
                (selectedArcMclnState != originalArcMclnState);
    }

    boolean isSelectedAllowedStatesListValid(List<MclnState> paletteSelectedAllowedStatesList) {
        return paletteSelectedAllowedStatesList != null && paletteSelectedAllowedStatesList.size() != 0;
    }

    // Finalization

//    private boolean isPropertyAllowedStatesListModified() {
//        return true;
//    }
//
//    boolean isInitializationComplete() {
//        boolean initialized = false;
//
//        return initialized;
//    }
//
//
//    private boolean isStateChanged() {
//        return false;
////        (originalInitialMclnStatementState == null && selectedInitialMclnStatementState != null) ||
////                (selectedInitialMclnStatementState != originalInitialMclnStatementState);
//    }
}
