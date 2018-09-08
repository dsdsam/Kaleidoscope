package mcln.model;

import mcln.simulator.InputOutputStateChangeListener;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: XP Admin
 * Date: 8/29/13
 * Time: 10:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class MclnModel {

    public static final String MCLN_MODEL_XML_TAG = "Mcln-Model";
    public static final String MCLN_MODEL_NAME_TAG = "Mcln-Model-Name";
    public static final String MCLN_MODEL_RECTANGLE_TAG = "Mcln-Model-Rectangle";
    public static final String MCLN_MODEL_RECTANGLE_CORNER_TAG = "XY-Location";

    public static final String STATEMENT_UID_FORMAT = "S-%07d";

    public static final String CONDITION_UID_FORMAT = "C-%07d";

    public static final String ARC_UID_FORMAT = "A-%07d";

    private static int[] findIdRange(Set<String> ids) {
        int[] minAndMaxIds = {0, 0};
        boolean firstID = true;
        for (String strID : ids) {
            String strIdNumber = strID.substring(2);
            try {
                int id = Integer.parseInt(strIdNumber);
                if (firstID) {
                    minAndMaxIds[0] = id;
                    minAndMaxIds[1] = id;
                    firstID = false;
                } else {
                    minAndMaxIds[0] = Integer.min(minAndMaxIds[0], id);
                    minAndMaxIds[1] = Integer.max(minAndMaxIds[1], id);
                }
            } catch (Exception e) {
                return null;
            }
        }

        return minAndMaxIds;
    }

    //
    //   created to be a part of GUI
    //
    public static MclnModel createInstance(String modelName, String modelId,
                                           double cSysX, double cSysY, double cSysWidth, double cSysHeight) {
        MclnDoubleRectangle mclnDoubleRectangle = new MclnDoubleRectangle(cSysX, cSysY, cSysWidth, cSysHeight);
        MclnModel mclnModel = new MclnModel(null, modelName, modelId, mclnDoubleRectangle);
        return mclnModel;
    }

    public static MclnModel createInstance(String modelName, String modelId, MclnDoubleRectangle modelSpaceRectangle) {
        MclnModel mclnModel = new MclnModel(null, modelName, modelId, modelSpaceRectangle);
        return mclnModel;
    }

    //
    //     M c l n   M o d e l   I n s t a n c e
    //

    private final CopyOnWriteArrayList<MclnModelSimulationListener> mclnModelSimulationListeners =
            new CopyOnWriteArrayList();

    private final CopyOnWriteArrayList<InputOutputStateChangeListener> inputOutputStateChangeListeners =
            new CopyOnWriteArrayList();

    private final List<MclnStatement> mclnStatements = new ArrayList();
    private final List<MclnCondition> mclnConditions = new ArrayList();
    private List<MclnArc> mclnArcs = new ArrayList();

    private final Map<String, MclnStatement> nodeIdToMclnStatementMap = new HashMap();
    private final Map<String, MclnCondition> nodeIdToMclnConditionMap = new HashMap();
    private final Map<String, MclnArc> arcIdToMclnArcMap = new HashMap();

    private int statementMinId;
    private int statementMaxId;

    private int conditionMinId;
    private int conditionMaxId;

    private int arcMinId;
    private int arcMaxId;

    private int statementCounter;
    private int conditionCounter;
    private int arcCounter;

    private final String sourceXml;

    private String modelName = "";

    private String fullName = null;

    private String modelId;

    protected double cSysX;
    protected double cSysY;
    protected double cSysWidth;
    protected double cSysHeight;

    protected MclnDoubleRectangle modelSpaceRectangle;

    // Simulation
    private boolean simulationEnabled;
    private boolean simulationRunning;
    private boolean simulationPaused;

    /**
     * created to be a part of server
     *
     * @param sourceXml
     * @param modelName
     * @param modelId
     * @param modelSpaceRectangle
     */
    private MclnModel(String sourceXml, String modelName, String modelId, MclnDoubleRectangle modelSpaceRectangle) {
        this.sourceXml = sourceXml;
        this.modelName = modelName;
        this.modelId = modelId;

        this.modelSpaceRectangle = modelSpaceRectangle;
        this.cSysX = modelSpaceRectangle.getX();
        this.cSysY = modelSpaceRectangle.getY();
        this.cSysWidth = modelSpaceRectangle.getWidth();
        this.cSysHeight = modelSpaceRectangle.getHeight();
    }

    // =============================
    // Simulation runtime attributes
    // =============================

    public boolean isSimulationEnabled() {
        return simulationEnabled;
    }

    public void setSimulationEnabled(boolean simulationEnabled) {
        this.simulationEnabled = simulationEnabled;
    }

    public boolean isSimulationRunning() {
        return simulationRunning;
    }

    public void setSimulationRunning(boolean simulationRunning) {
        this.simulationRunning = simulationRunning;
    }

    public boolean isSimulationPaused() {
        return simulationPaused;
    }

    public void setSimulationPaused(boolean simulationPaused) {
        this.simulationPaused = simulationPaused;
    }

    // =============================

    private int mclnModelsCounter;

    private synchronized final int getMclnModelUID() {
        return ++mclnModelsCounter;
    }

    synchronized final String getStatementUID() {
        return String.format(MclnModel.STATEMENT_UID_FORMAT, ++statementCounter);
    }

    synchronized final String getConditionUID() {
        return String.format(MclnModel.CONDITION_UID_FORMAT, ++conditionCounter);
    }

    synchronized final String getArcUID() {
        return String.format(MclnModel.ARC_UID_FORMAT, ++arcCounter);
    }

    void resetModelName(String modelName) {
        this.modelName = modelName;
    }

    public boolean isEmpty() {
        return mclnStatements.isEmpty() && mclnConditions.isEmpty() && mclnArcs.isEmpty();
    }

    public String getSourceXml() {
        return sourceXml;
    }

    public MclnDoubleRectangle getModelSpaceRectangle() {
        return new MclnDoubleRectangle(cSysX, cSysY, cSysWidth, cSysHeight);
    }

    public void setModelSpaceRectangle(MclnDoubleRectangle modelSpaceRectangle) {
        this.modelSpaceRectangle = modelSpaceRectangle;
        this.cSysX = modelSpaceRectangle.getX();
        this.cSysY = modelSpaceRectangle.getY();
        this.cSysWidth = modelSpaceRectangle.getWidth();
        this.cSysHeight = modelSpaceRectangle.getHeight();
    }

    public void clearMclnModel() {
        mclnStatements.clear();
        nodeIdToMclnStatementMap.clear();
        mclnConditions.clear();
        nodeIdToMclnConditionMap.clear();
        mclnArcs.clear();
        arcIdToMclnArcMap.clear();

        statementMinId = 0;
        statementMaxId = 0;
        conditionMinId = 0;
        conditionMaxId = 0;
        arcMinId = 0;
        arcMaxId = 0;

        statementCounter = 0;
        conditionCounter = 0;
        arcCounter = 0;
    }

    public void addMclnModelSimulationListener(MclnModelSimulationListener mclnModelSimulationListener) {
        mclnModelSimulationListeners.add(mclnModelSimulationListener);
    }

    public void removeMclnModelSimulationListener(MclnModelSimulationListener mclnModelSimulationListener) {
        mclnModelSimulationListeners.remove(mclnModelSimulationListener);
    }

    public void addInputOutputStateChangeListener(InputOutputStateChangeListener inputOutputStateChangeListener) {
        inputOutputStateChangeListeners.add(inputOutputStateChangeListener);
    }

    //
    //   model structure
    //

    public String getModelId() {
        return modelId;
    }

    public String getModelName() {
        return modelName;
    }

    public final int getNumberOfStatements() {
        return mclnStatements.size();
    }

    public final int getNumberOfConditions() {
        return mclnConditions.size();
    }

    public final int getNumberOfArcs() {
        return mclnArcs.size();
    }

    //
    //
    //

    public void addMclnStatement(MclnStatement mclnStatement) {
        mclnStatements.add(mclnStatement);
        nodeIdToMclnStatementMap.put(mclnStatement.getUID(), mclnStatement);
        updatePropertyMinAndMaxIDs(true);
    }

    public void removeMclnStatement(MclnStatement mclnStatement) {
        mclnStatement.disconnectFromAllArc();
        mclnStatements.remove(mclnStatement);
        nodeIdToMclnStatementMap.remove(mclnStatement.getUID());
        updatePropertyMinAndMaxIDs(false);
    }

    public MclnStatement getMclnStatementByUID(String UID) {
        return nodeIdToMclnStatementMap.get(UID);
    }

    public List<MclnStatement> getMclnStatements() {
        return mclnStatements;
    }

    //
    //    C o n d i t i o n s
    //

    public void addMclnCondition(MclnCondition mclnCondition) {
        mclnConditions.add(mclnCondition);
        nodeIdToMclnConditionMap.put(mclnCondition.getUID(), mclnCondition);
        updateConditionMinAndMaxIDs(true);
    }

    public void removeMclnCondition(MclnCondition mclnCondition) {
        mclnCondition.disconnectFromAllArc();
        mclnConditions.remove(mclnCondition);
        nodeIdToMclnConditionMap.remove(mclnCondition.getUID());
        updateConditionMinAndMaxIDs(false);
    }

    public MclnCondition getMclnConditionByUID(String UID) {
        return nodeIdToMclnConditionMap.get(UID);
    }

    public List<MclnCondition> getMclnConditions() {
        return mclnConditions;
    }

    //
    //   A r c s
    //

    public void addMclnArc(MclnArc mclnArc) {
        mclnArcs.add(mclnArc);
        arcIdToMclnArcMap.put(mclnArc.getUID(), mclnArc);
        updateArcMinAndMaxIDs(true);
    }

    public void removeMclnArc(MclnArc mclnArc) {
        mclnArc.disconnectFromInputAndOutputNodes();
        mclnArcs.remove(mclnArc);
        arcIdToMclnArcMap.remove(mclnArc.getUID());
        updateArcMinAndMaxIDs(false);
    }

    public MclnArc getMclnArcByUID(String UID) {
        return arcIdToMclnArcMap.get(UID);
    }

    public List<MclnArc> getMclnArcs() {
        return mclnArcs;
    }

    //
    // Updating model attributes
    //

    private void updatePropertyMinAndMaxIDs(boolean afterAdded) {
        Set<String> statementIds = nodeIdToMclnStatementMap.keySet();
        int[] statementMinAndMaxIds = findIdRange(statementIds);

        statementMinId = statementMinAndMaxIds[0];
        statementMaxId = statementMinAndMaxIds[1];

        if (afterAdded) {
            // updating growing UID after Add operation
            statementCounter = statementMaxId;
        }
    }

    private void updateConditionMinAndMaxIDs(boolean afterAdded) {
        Set<String> conditionIds = nodeIdToMclnConditionMap.keySet();
        int[] conditionMinAndMaxIds = findIdRange(conditionIds);

        conditionMinId = conditionMinAndMaxIds[0];
        conditionMaxId = conditionMinAndMaxIds[1];

        if (afterAdded) {
            // updating growing UID after Add operation
            conditionCounter = conditionMaxId;
        }
    }

    private void updateArcMinAndMaxIDs(boolean afterAdded) {
        Set<String> arcIds = arcIdToMclnArcMap.keySet();
        int[] arcMinAndMaxIds = findIdRange(arcIds);

        arcMinId = arcMinAndMaxIds[0];
        arcMaxId = arcMinAndMaxIds[1];

        if (afterAdded) {
            // updating growing UID after Add operation
            arcCounter = arcMaxId;
        }
    }

    public String getStatementMinId() {
        return String.format(STATEMENT_UID_FORMAT, statementMinId);
    }

    public String getStatementMaxId() {
        return String.format(STATEMENT_UID_FORMAT, statementMaxId);
    }

    public String getConditionMinId() {
        return String.format(CONDITION_UID_FORMAT, conditionMinId);
    }

    public String getConditionMaxId() {
        return String.format(CONDITION_UID_FORMAT, conditionMaxId);
    }

    public String getArcMinId() {
        return String.format(ARC_UID_FORMAT, arcMinId);
    }

    public String getArcMaxId() {
        return String.format(ARC_UID_FORMAT, arcMaxId);
    }

    //
    //   Model behavior
    //

    public static String getMclnModelXmlTag() {
        return MCLN_MODEL_XML_TAG;
    }

    public void updateState() {

    }

    private void fireStateChane() {

    }

    // ================================================================================================================

    //
    //   Methods that call MclnModelSimulationListener
    //

    //  Called by:
    //  1) simulation Controller when simulation started
    //  2) simulation Controller when simulation reset
    //  3) simulating engine when model state changed

    public void fireModelStateChanged() {
        for (MclnModelSimulationListener mclnModelSimulationListener : mclnModelSimulationListeners) {
            mclnModelSimulationListener.mclnModelStateChanged();
        }
    }

    public void fireSimulationStepExecuted() {
        for (MclnModelSimulationListener mclnModelSimulationListener : mclnModelSimulationListeners) {
            mclnModelSimulationListener.simulationStepExecuted();
        }
    }

    public void fireModelStateReset() {
        for (MclnModelSimulationListener mclnModelSimulationListener : mclnModelSimulationListeners) {
            mclnModelSimulationListener.mclnModelStateReset();
        }
    }

    public void firePropertyNewSuggestedStateInferred(MclnStatement mclnStatement) {
        for (MclnModelSimulationListener mclnModelSimulationListener : mclnModelSimulationListeners) {
            mclnModelSimulationListener.propertyNewSuggestedStateInferred(mclnStatement);
        }
    }

    // ================================================================================================================

    //
    // Methods that call InputOutputStateChangeListener
    //

    public void fireInputPropertyStateChangedOnInputEvent(MclnStatement mclnStatement) {
        for (InputOutputStateChangeListener inputOutputStateChangeListener : inputOutputStateChangeListeners) {
            inputOutputStateChangeListener.inputPropertyStateChanged(mclnStatement);
        }
    }

    public void fireOutputPropertyStateChangedOnInputEvent(MclnStatement mclnStatement) {
        for (InputOutputStateChangeListener inputOutputStateChangeListener : inputOutputStateChangeListeners) {
            inputOutputStateChangeListener.outputPropertyStateChanged(mclnStatement);
        }
    }

    // ================================================================================================================

    //
    //   model to XML
    //

    public String toXml() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<").append(MCLN_MODEL_XML_TAG).append(" name=\"" + modelName + "\">");

        rectangleToXml(stringBuilder);

        for (MclnStatement mclnStatement : mclnStatements) {
            String statementAsXml = mclnStatement.toXml();
            stringBuilder.append(statementAsXml);
        }

        for (MclnCondition mclnCondition : mclnConditions) {
            String conditionAsXml = mclnCondition.toXml();
            stringBuilder.append(conditionAsXml);
        }

        for (MclnArc mclnArc : mclnArcs) {
            String arcAsXml = mclnArc.toXml();
            stringBuilder.append(arcAsXml);
        }

        stringBuilder.append("</").append(MCLN_MODEL_XML_TAG).append(">");
        return stringBuilder.toString();
    }

    private StringBuilder rectangleToXml(StringBuilder stringBuilder) {
        stringBuilder.append("<").append(MCLN_MODEL_RECTANGLE_TAG).append(">");
        toXYXmlLocation(stringBuilder, modelSpaceRectangle.getX(), modelSpaceRectangle.getY(),
                MCLN_MODEL_RECTANGLE_CORNER_TAG);
        toXYXmlLocation(stringBuilder, modelSpaceRectangle.getWidth(), modelSpaceRectangle.getHeight(),
                MCLN_MODEL_RECTANGLE_CORNER_TAG);
        stringBuilder.append("</").append(MCLN_MODEL_RECTANGLE_TAG).append(">");
        return stringBuilder;
    }

    private StringBuilder toXYXmlLocation(StringBuilder stringBuilder, double x, double y, String TAG) {
        stringBuilder.append("<").append(TAG).append(">").
                append(doubleToFormattedString(x)).
                append(" : ").
                append(doubleToFormattedString(y)).
                append("</").append(TAG).append(">");
        return stringBuilder;
    }

    private String doubleToFormattedString(double number) {
        return String.format(Locale.getDefault(), "%012.6f", number);
    }
}
