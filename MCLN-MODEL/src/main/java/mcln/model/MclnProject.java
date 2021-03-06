package mcln.model;

import mcln.palette.CreationStatePalette;
import mcln.palette.MclnState;
import vw.valgebra.VAlgebra;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: XP Admin
 * Date: 1/30/14
 * Time: 7:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class MclnProject {

    /*
       There are six operations that start and stop model modification:
       1) Creating Default Project
       2) New Project
       3) Clear Project
       4) Create Demo Project
       5) Save Project
       6) Retrieve Project

       After each of this operations DSE creates backup data that represent the state of the model.
       Them the model cam be modified.
       After modification and before any of operation DSE compares id model was modified between the operations.
       If the model was modified use has three options:
           a) Cancel operation and proceed modifications.
           b) Discard the change and fulfil the operation.
           c) Save Project and then fulfil the operation.

        The modifications saving support is slightly different for each of the operations,
        but generally includes following steps:
           a) Creating Project backup,
           b) Setting entities UID initial states.
           c) Checking the change.
           d) If operation is not cancelled, preparing to next modification cycle by
              Creating Project backup and establishing entities UID initial state again.

        Here are steps for particular operations.

        Creating Default Project
        ========================

        This operations begins modification cycle, therefore only two steps exists:
        Creating Project backup and setting entities UID initial states.

        The backup procedure stores:
        1) List of model Statements,
        2) List of model Conditions,
        3) List of model Arcs,
        4) Model location.

        Setting entities UID initial states.
        ====================================

        If Operation created empty model then nothing should be done as all initial UIDs are 0

        If model is Demo model or retrieved from file initial UID values should be equal to
        Max UID for each entity type.

        If model was saved UIDs should not change

        During modification min and max UIDs should be update after each add or remove operation.
        Growing UID should be updated only after add operation;

     */

    public static final String DEFAULT_PROJECT_NAME = "Default Mcln Project";

    private static final SimpleDateFormat dataFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss", Locale.US);

    private static final String MCLN_PROJECT_XML_TAG = "Mcln-Project";

    private static MclnProject currentMclnProject;

    private synchronized final String getStatementUID() {
        return getCurrentMclnModel().getStatementUID();
    }

    private synchronized final String getConditionUID() {
        return getCurrentMclnModel().getConditionUID();
    }

    private synchronized final String getArcUID() {
        return getCurrentMclnModel().getArcUID();
    }


    //
    //   F a c t o r y   m e t h o d s
    //

    public static final MclnProject createInitialMclnProject(String projectName,
                                                             MclnDoubleRectangle projectSpaceRectangle,
                                                             MclnModel mclnModel) {
        currentMclnProject = new MclnProject(projectSpaceRectangle, false, false, projectName, mclnModel);
        currentMclnProject.backupProject();
        return currentMclnProject;
    }

    /**
     * Called when
     * a) When DSDSSE GUI is created
     * b) When DSDSSE GUI user creates new projects
     *
     * @param projectSpaceRectangle
     * @param mclnModel
     * @return
     */
    public static final MclnProject createNewEmptyMclnProject(String projectName,
                                                              MclnDoubleRectangle projectSpaceRectangle,
                                                              MclnModel mclnModel) {
        currentMclnProject = new MclnProject(projectSpaceRectangle, false, false, projectName, mclnModel);
        currentMclnProject.backupProject();
        return currentMclnProject;
    }

    /**
     * Called when
     * a) When DSDSSE GUI is created
     * b) When DSDSSE GUI user creates new projects
     *
     * @param projectName
     * @param mclnModel
     * @return NewEmptyMclnProject
     */
    public static final MclnProject createPresentationMclnProject(String projectName,
                                                                  MclnDoubleRectangle projectSpaceRectangle,
                                                                  MclnModel mclnModel) {
        currentMclnProject = new MclnProject(projectSpaceRectangle, true, false, projectName, mclnModel);
        return currentMclnProject;
    }

    /**
     * @param projectName
     * @param mclnModel
     * @return
     */
    public static final MclnProject createDemoMclnProject(String projectName, MclnDoubleRectangle projectSpaceRectangle,
                                                          MclnModel mclnModel) {
        boolean currentProjectIsPresentationOne = MclnProject.getInstance().isPresentationProject();
        currentMclnProject = new MclnProject(projectSpaceRectangle, currentProjectIsPresentationOne,
                true, projectName, mclnModel);
        return currentMclnProject;
    }

    /**
     * Called to create project upon retrieval, when parsing XML
     * Models are added as part of retrieval when parsed
     *
     * @param projectName
     * @return
     */
    public static final MclnProject createRetrievedMclnProject(String projectName, MclnDoubleRectangle projectSpaceRectangle, MclnModel mclnModel) {
        currentMclnProject = new MclnProject(projectSpaceRectangle, false, false, projectName, mclnModel);
        currentMclnProject.backupProject();
        return currentMclnProject;
    }

    /**
     * Method recreates provided as argument project and sets it as the current project.
     * The argument project already backed up. While recreating project we only recreate
     * the project structure. This will preserve entity's initialized flag that might be
     * set before the project was temporarily stashed.
     *
     * @param stashedMclnProject
     * @return
     */
    public static final MclnProject recreateStashedMclnProject(MclnProject stashedMclnProject) {
        MclnDoubleRectangle projectSpaceRectangle = stashedMclnProject.projectSpaceRectangle;
        currentMclnProject = new MclnProject(projectSpaceRectangle, false,
                false, stashedMclnProject.getProjectName(), stashedMclnProject.getCurrentMclnModel());
        currentMclnProject.backupProject();
        return currentMclnProject;
    }

    public static MclnProject getInstance() {
        return currentMclnProject;
    }

    //
    //  C r e a t i n g   S t a t e m e n t
    //

    /**
     * Called when Mcln Statement is created by Editor
     *
     * @param cSysLocation MclnModelFactory
     * @return
     */
    public synchronized final MclnStatement createMclnStatement(String subject, double[] cSysLocation) {
        MclnStatement mclnStatement = MclnModelFactory.createMclnStatement(getStatementUID(), subject, cSysLocation,
                CreationStatePalette.CREATION_STATE);
        return mclnStatement;
    }

    /**
     * Called when Mcln Statement is created programmatically for Demo
     *
     * @param subject
     * @param cSysLocation
     * @param initialState
     * @return
     */
    public synchronized final MclnStatement createMclnStatement(String subject, double[] cSysLocation,
                                                                MclnState initialState) {
        MclnStatement mclnStatement = MclnModelFactory.createMclnStatement(getStatementUID(), subject, cSysLocation,
                initialState);
        return mclnStatement;
    }

    /**
     * Called when Mcln Statement is created programmatically for Demo
     *
     * @param subject
     * @param availableMclnStatementStates
     * @param cSysLocation
     * @param initialState
     * @return
     */
    public synchronized final MclnStatement createMclnStatement(String subject,
                                                                AvailableMclnStatementStates availableMclnStatementStates,
                                                                double[] cSysLocation,
                                                                MclnState initialState) {
        MclnStatement mclnStatement = MclnModelFactory.createMclnStatement(getStatementUID(), subject,
                availableMclnStatementStates, cSysLocation, initialState);
        return mclnStatement;
    }

    /**
     * Called when Mcln Statement is created programmatically with Time Driven Input Simulating Program
     *
     * @param subject
     * @param cSysLocation
     * @param initialState
     * @param inputSimulatingProgramData
     * @return
     */
    public synchronized final MclnStatement createMclnStatementWithTimeDrivenProgram(
            String subject, AvailableMclnStatementStates availableMclnStatementStates,
            double[] cSysLocation, MclnState initialState, Object[][] inputSimulatingProgramData) {

        MclnStatement mclnStatement = MclnModelFactory.createMclnStatementWithTimeDrivenProgram(getStatementUID(),
                subject, availableMclnStatementStates, cSysLocation, initialState, inputSimulatingProgramData);
        return mclnStatement;
    }

    /**
     * Called when Mcln Statement is created programmatically with State Driven Input Simulating Program
     *
     * @param subject
     * @param availableMclnStatementStates
     * @param cSysLocation
     * @param initialState
     * @param inputSimulatingProgramData
     * @return
     */
    public synchronized final MclnStatement createMclnStatementWithStateDrivenProgram(
            String subject, AvailableMclnStatementStates availableMclnStatementStates,
            double[] cSysLocation, MclnState initialState, Object[][] inputSimulatingProgramData) {

        MclnStatement mclnStatement = MclnModelFactory.createMclnStatementWithStateDrivenProgram(getStatementUID(),
                subject, availableMclnStatementStates, cSysLocation, initialState, inputSimulatingProgramData);
        return mclnStatement;
    }

    /**
     * @param mclnStatement
     * @param theProgramHasPhase
     * @param timeDrivenProgram
     * @param programStepDataList
     */
    public static final void updateInputGeneratingProgram(MclnStatement mclnStatement,
                                                          boolean theProgramHasPhase,
                                                          boolean timeDrivenProgram,
                                                          List<ProgramStepData> programStepDataList) {
        List<ProgramStep> programSteps = MclnModelFactory.mclnProgramDataToProgramStates(theProgramHasPhase,
                programStepDataList);
        mclnStatement.updateInputGeneratingProgram(programSteps, timeDrivenProgram);
    }

    public static final void removeInputGeneratingProgram(MclnStatement mclnStatement) {
        mclnStatement.removeInputGeneratingProgram();
    }

    //
    //  C o n d i t i o n s
    //

    /**
     * Called when Mcln Condition is being created by Editor
     *
     * @param cSysLocation
     * @return
     */
    public final MclnCondition createMclnCondition(String name, double[] cSysLocation) {
        MclnCondition mclnCondition = new MclnCondition(getConditionUID(), cSysLocation);
        mclnCondition.setName(name);
        return mclnCondition;
    }

    public final MclnCondition createMclnCondition(double[] cSysLocation,
                                                   MclnStatement inpStatement, MclnStatement outStatement) {
        MclnCondition mclnCondition = new MclnCondition(getConditionUID(), cSysLocation);
        return mclnCondition;
    }

    //
    //   A r c s
    //

    /**
     * Called when new Mcln Arc is being created by Editor
     *
     * @return MclnArc instance
     */
    public final MclnArc createIncompleteMclnSplineArc(ArrowTipLocationPolicy arrowTipLocationPolicy, MclnNode inpNode) {
        List<double[]> cSysKnots = new ArrayList();
        MclnArc<MclnNode, MclnNode> mclnArc = new MclnSplineArc(arrowTipLocationPolicy, getArcUID(), cSysKnots,
                CreationStatePalette.CREATION_STATE, inpNode);
        return mclnArc;
    }

    /**
     * Called when new Mcln Arc is being created by Editor
     *
     * @return MclnPolylineArc instance
     */
    public final MclnPolylineArc createIncompleteMclnPolylineArc(ArrowTipLocationPolicy arrowTipLocationPolicy, MclnNode inpNode) {
        List<double[]> cSysKnots = new ArrayList();
        MclnPolylineArc<MclnNode, MclnNode> mclnArc = new MclnPolylineArc(arrowTipLocationPolicy, getArcUID(), cSysKnots,
                CreationStatePalette.CREATION_STATE, inpNode);
        return mclnArc;
    }

    /**
     * Is used
     * 1) to create Fragment Arc for Editor
     * 2) to create Arc for Demo projects
     *
     * @param arcMclnState
     * @param inpNode
     * @param outNode
     * @return
     */
    public final MclnArc createMclnStraightArc(ArrowTipLocationPolicy arrowTipLocationPolicy, MclnState arcMclnState,
                                               MclnNode inpNode, MclnNode outNode) {
        List<double[]> cSysKnots = new ArrayList();
        cSysKnots.add(inpNode.getCSysLocation());
        cSysKnots.add(outNode.getCSysLocation());
        MclnArc<MclnNode, MclnNode> mclnArc = new MclnSplineArc(arrowTipLocationPolicy, getArcUID(), cSysKnots,
                arcMclnState, inpNode, outNode);
        return mclnArc;
    }


    // used by demo with Mcln State

    /**
     * Is used
     * 1) To create Demo projects to create Arc with Mcln State
     *
     * @param cSysKnots
     * @param arcMclnState
     * @param inpNode
     * @param outNode
     * @param <InpNodeType>
     * @param <OutNodeType>
     * @return
     */
    public synchronized final <InpNodeType, OutNodeType> MclnArc createMclnArc(
            ArrowTipLocationPolicy arrowTipLocationPolicy, List<double[]> cSysKnots, MclnState arcMclnState,
            InpNodeType inpNode, OutNodeType outNode) {
        MclnArc<InpNodeType, OutNodeType> mclnArc = new MclnSplineArc(arrowTipLocationPolicy, getArcUID(), cSysKnots,
                arcMclnState, inpNode, outNode);
        return mclnArc;
    }

    //
    //   F r a g m e n t s
    //

    /**
     * create Statement-Condition-Statement fragment for Demo
     *
     * @param arrowTipLocationPolicy
     * @param inpStatement
     * @param outStatement
     * @param cSysLocation
     * @param inpArcState
     * @param outArcState
     * @return
     */
    public MclnCondition createSCSFragment(ArrowTipLocationPolicy arrowTipLocationPolicy, MclnStatement inpStatement,
                                           MclnStatement outStatement, double[] cSysLocation, MclnState inpArcState,
                                           MclnState outArcState) {

        MclnCondition mclnCondition = createMclnCondition(cSysLocation, inpStatement, outStatement);


        //   Create Condition input arc

        List<double[]> knotCSysLocations01 = new ArrayList();
        knotCSysLocations01.add(inpStatement.getCSysLocation());
        knotCSysLocations01.add(mclnCondition.getCSysLocation());
        MclnArc inpArc =
                createMclnArc(arrowTipLocationPolicy, knotCSysLocations01, inpArcState, inpStatement, mclnCondition);
        // arc from statement to condition

        List<double[]> knotCSysLocations02 = new ArrayList();
        knotCSysLocations02.add(mclnCondition.getCSysLocation());
        knotCSysLocations02.add(outStatement.getCSysLocation());
        MclnArc outArc =
                createMclnArc(arrowTipLocationPolicy, knotCSysLocations02, outArcState, mclnCondition, outStatement);
        // arc from condition to statement

        /* Creating the fragment

         inpArcList[i]   --> InputNode   outNodeList[i]
                        /                /
               -> Arc --                /
              /                        /
         inpArcList[i]   --> Tran <-- /  outNodeList[i]
                        /                /
               -> Arc --                /
              /                        /
        inpArcList[i]   OutputNode <--/  outNodeList[i]

        */
        return mclnCondition;
    }


    //
    //   I n s t a n c e
    //

    //   Backup data

    private MclnProject backupMclnProject;
    private MclnModel backupMclnModel;

    private ProjectAttributes backupProjectAttributes = new ProjectAttributes("", 0, 0, 0, 0);
    private List<MclnStatement> backupMclnStatements = new ArrayList();
    private List<MclnCondition> backupMclnConditions = new ArrayList();
    private List<MclnArc> backupMclnArcs = new ArrayList();

    private double[] backupModelLocation = VAlgebra.initVec3(0, 0, 0);

    //   Instance data

    private ProjectAttributes projectAttributes;
    private MclnDoubleRectangle projectSpaceRectangle;
    private final boolean demoProject;
    private final boolean presentationProject;
    private Map<String, MclnModel> models = new ConcurrentHashMap();

    private MclnModel currentMclnModel;

    private String projectName = "";

    private String lastAbsoluteModelStorageDirectory;
    private String lastSavedOrRetrievedProjectFileName;

    /**
     * @param projectName
     * @param mclnModel
     */
    private MclnProject(MclnDoubleRectangle projectSpaceRectangle, boolean presentationProject, boolean demoProject,
                        String projectName, MclnModel mclnModel) {
        projectAttributes = new ProjectAttributes(projectName, projectSpaceRectangle);
        this.projectSpaceRectangle = projectSpaceRectangle;
        this.presentationProject = presentationProject;
        this.demoProject = demoProject;
        this.projectName = projectName;
        addMclnModel(mclnModel);
    }

    public final MclnDoubleRectangle getProjectSpaceRectangleCopy() {
        return new MclnDoubleRectangle(projectSpaceRectangle);
    }

    public String getProjectName() {
        return projectName;
    }

    public final void resetProjectAttributes(ProjectAttributes projectAttributes) {
        this.projectName = projectAttributes.getProjectName();
        projectSpaceRectangle = projectAttributes.getMclnDoubleRectangle();
        this.projectAttributes = projectAttributes;
    }

    public boolean wasProjectSavedOrRetrieved() {
        return lastAbsoluteModelStorageDirectory != null;
    }

    public String getLastAbsoluteModelStorageDirectory() {
        return lastAbsoluteModelStorageDirectory;
    }

    public void setLastAbsoluteModelStorageDirectory(String lastAbsoluteModelStorageDirectory) {
        this.lastAbsoluteModelStorageDirectory = lastAbsoluteModelStorageDirectory;
    }

    public String getLastSavedOrRetrievedProjectFileNameAsItIs() {
        return lastSavedOrRetrievedProjectFileName;
    }

    public String getLastSavedOrRetrievedProjectFileName() {
        if (lastSavedOrRetrievedProjectFileName != null) {
            return lastSavedOrRetrievedProjectFileName;
        } else if (!projectName.isEmpty()) {
            return projectName;
        }
        return MclnProject.DEFAULT_PROJECT_NAME;
    }

    public void setLastSavedOrRetrievedProjectFileName(String lastSavedOrRetrievedProjectFileName) {
        this.lastSavedOrRetrievedProjectFileName = lastSavedOrRetrievedProjectFileName;
    }

    public boolean isProjectEmpty() {
        return currentMclnModel.isEmpty();
    }

    public boolean isPresentationProject() {
        return presentationProject;
    }

    public boolean isDemoProject() {
        return demoProject;
    }

    /**
     * user can change initially given model name
     *
     * @param modelName
     */
    public void resetModelName(String modelName) {
        models.remove(currentMclnModel.getModelName());
        currentMclnModel.resetModelName(modelName);
        models.put(currentMclnModel.getModelName(), currentMclnModel);
    }

    public MclnModel getCurrentMclnModel() {
        return currentMclnModel;
    }


    private void addMclnModel(MclnModel mclnModel) {
        if (mclnModel == null) {
            return;
        }
        models.put(mclnModel.getModelName(), mclnModel);
        if (currentMclnModel == null) {
            this.currentMclnModel = mclnModel;
        }
    }

    @Override
    public String toString() {
        return "MCLN project: " + projectName + ", current MCLN model: " + currentMclnModel;
    }

    //
    //   Checking if Project is modified
    //

    private void backupProjectAndCleanInitializationUpdatedFlag() {
        backupProject();
        clearInitializationUpdatedFlag();
    }

    public void resetTheProjectBackup() {
        backupProjectAndCleanInitializationUpdatedFlag();
    }

    public void backupProject() {
        backupMclnProject = MclnProject.getInstance();
        backupMclnModel = backupMclnProject.getCurrentMclnModel();

        backupProjectAttributes = backupMclnProject.projectAttributes.getCopy();
        backupMclnStatements = (List<MclnStatement>) ((ArrayList) backupMclnModel.getMclnStatements()).clone();
        backupMclnConditions = (List<MclnCondition>) ((ArrayList) backupMclnModel.getMclnConditions()).clone();
        backupMclnArcs = (List<MclnArc>) ((ArrayList) backupMclnModel.getMclnArcs()).clone();

        backupModelLocation = getModelLocation(backupMclnModel);
    }

    public double[] getModelLocation(MclnModel mclnModel) {

        double[] currentModelLocation = VAlgebra.initVec3(0, 0, 0);

        List<MclnStatement> currentMclnStatements = mclnModel.getMclnStatements();
        for (MclnStatement mclnStatement : currentMclnStatements) {
            double[] mclnStatementLocation = mclnStatement.getCSysLocation();
            currentModelLocation = VAlgebra.addVec3(currentModelLocation, mclnStatementLocation);

        }
        List<MclnCondition> currentMclnConditions = mclnModel.getMclnConditions();
        for (MclnCondition mclnCondition : currentMclnConditions) {
            double[] mclnConditionLocation = mclnCondition.getCSysLocation();
            currentModelLocation = VAlgebra.addVec3(currentModelLocation, mclnConditionLocation);
        }
        return currentModelLocation;
    }

    private void clearInitializationUpdatedFlag() {
        backupMclnProject = MclnProject.getInstance();
        backupMclnModel = backupMclnProject.getCurrentMclnModel();

         /*
           We clear the Runtime Initialization Updated Flag in case when the model
           was saved (but not destroyed).  So, all the initialization changes were
           saved.  The flag will be set  again when next initialization is applied.
         */
        List<MclnStatement> currentMclnStatements = backupMclnModel.getMclnStatements();
        List<MclnArc> currentMclnArcs = backupMclnModel.getMclnArcs();

        for (MclnStatement mclnStatement : currentMclnStatements) {
            mclnStatement.resetRuntimeInitializationUpdatedFlag();
        }

        for (MclnArc mclnArc : currentMclnArcs) {
            mclnArc.resetRuntimeInitializationUpdatedFlag();
        }
    }

    /**
     * projectChanged[0] -> boolean[1] = projectAttributesChanged && modelChanged[0]
     * projectChanged[1] -> boolean[1] = projectAttributesChanged
     * projectChanged[2] -> boolean[]  = modelChanged, where
     * <p>
     * modelChanged[0] - true when something changed = modelChanged[1] | modelChanged[2] |modelChanged[3]
     * modelChanged[1] - true when initialized
     * modelChanged[2] - true when structure changed
     * modelChanged[3] - true when model moved
     *
     * @return
     */
    public boolean[][] wasProjectModified() {
        boolean[][] projectChanged = new boolean[3][];
        boolean projectAttributesChanged = wereProjectAttributesChanged();
        boolean[] modelChanged = wasModelModified();
        projectChanged[0] = new boolean[]{projectAttributesChanged || modelChanged[0]};
        if (!projectChanged[0][0]) {
            return projectChanged;
        }
        projectChanged[1] = new boolean[]{projectAttributesChanged};
        projectChanged[2] = modelChanged;
        return projectChanged;
    }

    /**
     * @return
     */
    public boolean[] wasModelModified() {
        boolean[] modelChanged = new boolean[4];

        boolean entityInitializationChanged = isEntityInitializationChanged();
        modelChanged[1] = entityInitializationChanged;

        boolean modelStructureChanged = isModelStructureChanged();
        boolean modelUIDChanged = wasUIDChanged();
        modelChanged[2] = modelStructureChanged | modelUIDChanged;

        boolean modelOrFragmentWasMoved = wasModelOrFragmentMoved();
        modelChanged[3] = modelOrFragmentWasMoved;

        modelChanged[0] = modelChanged[1] || modelChanged[2] || modelChanged[3];

        return modelChanged;
    }

    /**
     * @return
     */
    private boolean wereProjectAttributesChanged() {
        MclnProject currentMclnProject = MclnProject.getInstance();
        boolean attributesWereChanged = !currentMclnProject.projectAttributes.equals(currentMclnProject.backupProjectAttributes);
        return attributesWereChanged;
    }

    /**
     * @return
     */
    private boolean isModelStructureChanged() {
        boolean modelChanged = false;

        MclnProject currentMclnProject = MclnProject.getInstance();
        MclnModel currentMclnModel = currentMclnProject.getCurrentMclnModel();

        List<MclnStatement> currentMclnStatements = currentMclnModel.getMclnStatements();
        List<MclnCondition> currentMclnConditions = currentMclnModel.getMclnConditions();
        List<MclnArc> currentMclnArcs = currentMclnModel.getMclnArcs();

        boolean statementSizeChanged = backupMclnStatements.size() != currentMclnStatements.size();
        boolean conditionsSizeChanged = backupMclnConditions.size() != currentMclnConditions.size();
        boolean arcsSizeChanged = backupMclnArcs.size() != currentMclnArcs.size();

        modelChanged |= statementSizeChanged || conditionsSizeChanged || arcsSizeChanged;

        return modelChanged;
    }

    private boolean wasUIDChanged() {
        boolean modelChanged = false;

        MclnProject currentMclnProject = MclnProject.getInstance();
        MclnModel currentMclnModel = currentMclnProject.getCurrentMclnModel();

        List<MclnStatement> currentMclnStatements = currentMclnModel.getMclnStatements();
        List<MclnCondition> currentMclnConditions = currentMclnModel.getMclnConditions();
        List<MclnArc> currentMclnArcs = currentMclnModel.getMclnArcs();

        Set<String> backupStatementUIDsSet = new HashSet();
        for (MclnStatement mclnStatement : backupMclnStatements) {
            backupStatementUIDsSet.add(mclnStatement.getUID());
        }

        Set<String> backupConditionUIDsSet = new HashSet();
        for (MclnCondition mclnCondition : backupMclnConditions) {
            backupConditionUIDsSet.add(mclnCondition.getUID());
        }

        Set<String> backupArcUIDsSet = new HashSet();
        for (MclnArc mclnArc : backupMclnArcs) {
            backupArcUIDsSet.add(mclnArc.getUID());
        }

        // checking ...

        for (MclnStatement mclnStatement : currentMclnStatements) {
            String currentUID = mclnStatement.getUID();
            boolean contains = backupStatementUIDsSet.contains(currentUID);
            modelChanged |= !contains;
        }
        if (modelChanged) {
            return true;
        }

        for (MclnCondition mclnCondition : currentMclnConditions) {
            modelChanged |= !backupConditionUIDsSet.contains(mclnCondition.getUID());
        }
        if (modelChanged) {
            return true;
        }

        for (MclnArc mclnArc : currentMclnArcs) {
            modelChanged |= !backupArcUIDsSet.contains(mclnArc.getUID());
        }

        return modelChanged;
    }

    private boolean wasModelOrFragmentMoved() {
        MclnModel mclnModel = MclnProject.getInstance().getCurrentMclnModel();
        double[] modelLocation = getModelLocation(mclnModel);
        double[] difference = VAlgebra.subVec3(backupModelLocation, modelLocation);
        return !VAlgebra.isZeroVec3(difference);
    }

    private boolean isEntityInitializationChanged() {
        MclnProject currentMclnProject = MclnProject.getInstance();
        MclnModel currentMclnModel = currentMclnProject.getCurrentMclnModel();

        List<MclnStatement> currentMclnStatements = currentMclnModel.getMclnStatements();
        List<MclnArc> currentMclnArcs = currentMclnModel.getMclnArcs();

        boolean statementInitializationChanged = false;
        for (MclnStatement mclnStatement : currentMclnStatements) {
            statementInitializationChanged |= mclnStatement.isRuntimeInitializationUpdatedFlag();
        }

        boolean arcInitializationChanged = false;
        for (MclnArc mclnArc : currentMclnArcs) {
            arcInitializationChanged |= mclnArc.isRuntimeInitializationUpdatedFlag();
        }

        return statementInitializationChanged || arcInitializationChanged;
    }

    //
    //   Converting Project to XML
    //

    public String toXml() {
        String timeStamp = dataFormat.format(new Date());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<").append(MCLN_PROJECT_XML_TAG).append(" project-name=\"").append(projectName).
                append("\" saved-on=\"").append(timeStamp).append("\">");
        if (currentMclnModel != null) {
            stringBuilder.append(currentMclnModel.toXml());
        }
        stringBuilder.append("</").append(MCLN_PROJECT_XML_TAG).append(">");
        return stringBuilder.toString();
    }
}
