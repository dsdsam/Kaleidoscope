package dsdsse.app;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: XP Admin
 * Date: Feb 3, 2013
 * Time: 9:50:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class AppStateModel {

    private static final AppStateModel APP_STATE_MODEL = new AppStateModel();

    public static AppStateModel getInstance() {
        return APP_STATE_MODEL;
    }


    /**
     * A p p l i c a t i o n   m o d e s
     */
    public static enum Mode {
        NONE("None"),
        DEVELOPMENT("Development"),
        DEMO("Demo"),
        SIMULATION("Simulation");

        private String text;

        Mode(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }

        public String toString() {
            return text;
        }
    }

    /**
     * O p e r a t i o n s
     */
    public static enum Operation {
        NONE("", "None"),

        //   demo operations
        MODELS_BASIC_BLOCK(AppController.MENU_BASIC_BLOCK, "Basic Block"),
        MODELS_LOGICAL_BLOCKS(AppController.MENU_LOGICAL_BLOCKS, "Logical Blocks"),
        MODELS_TWO_RULES(AppController.MENU_TWO_RULES, "Two Rules"),
        MODELS_THREE_RULES(AppController.MENU_THREE_RULES, "Three Rules"),
        MODELS_TRIGGER(AppController.MENU_TRIGGER, "Trigger"),
        MODELS_SINGLE_PROPERTY(AppController.MENU_SINGLE_PROPERTY, AppController.MENU_SINGLE_PROPERTY),
        MODELS_TERNARY_COUNTER(AppController.MENU_TERNARY_COUNTER, AppController.MENU_TERNARY_COUNTER),
        MODELS_DIN_PHIL(AppController.MENU_DIN_PHIL, "Dining Philosophers"),
        MODELS_MUT_EX(AppController.MENU_MUT_EXCL, "Mutual Exclusion"),

        //   creation operations

        CREATE_PROPERTIES(AppController.MENU_ITEM_CREATE_PROPERTIES, "Creating Property Nodes"),
        CREATE_CONDITIONS(AppController.MENU_ITEM_CREATE_CONDITIONS, "Creating Condition Nodes"),

        CREATING_POLYLINE_ARCS(AppController.MENU_ITEM_CREATE_POLYLINE_ARCS, "Creating Polyline Arcs"),
        CREATING_SPLINE_ARCS(AppController.MENU_ITEM_CREATE_SPLINE_ARCS, "Creating Spline Arcs"),

        CREATE_FRAGMENTS(AppController.MENU_ITEM_CREATE_FRAGMENTS, "Creating Fragments"),

        MOVE_ELEMENTS(AppController.MENU_ITEM_MOVE_ELEMENTS, "Moving elements"),
        MOVE_FRAGMENT(AppController.MENU_ITEM_MOVE_FRAGMENT, "Moving fragments"),
        MOVE_MODEL(AppController.MENU_ITEM_MOVE_MODEL, "Moving Entire Model"),

        DELETE_ELEMENT(AppController.MENU_ITEM_DELETE_ELEMENT, "Deleting Elements"),

        INITIALIZATION(AppController.MENU_ITEM_LAUNCH_IA, "Initialization"),

        //    simulation operations

        SIMULATION_ENABLED(AppController.MENU_ITEM_SIMULATION_MODE, "Enabled"),

        SIMULATION_STARTED(AppController.MENU_ITEM_SIMULATION_MODE, "Started"),
        SIMULATION_STOPPED(AppController.MENU_ITEM_SIMULATION_MODE, "Stopped"),
        SIMULATION_RESUMED(AppController.MENU_ITEM_SIMULATION_MODE, "Resumed"),
        SIMULATION_ONE_STEP(AppController.MENU_ITEM_SIMULATION_MODE, "One Step"),
        SIMULATION_RESET(AppController.MENU_ITEM_SIMULATION_MODE, "Reset");

        //   O p e r a t i o n   I n s t a n c e

        private final String menuItem;
        private final String text;

        Operation(String menuItem, String text) {
            this.menuItem = menuItem;
            this.text = text;
        }

        public String getMenuItem() {
            return menuItem;
        }

        public boolean isNone() {
            return this == NONE;
        }

        public boolean isCreatingProperties() {
            return this == CREATE_PROPERTIES;
        }

        public boolean isCreatingConditions() {
            return this == CREATE_CONDITIONS;
        }

        public boolean isCreatingArcs() {
            return isCreatingSplineArcs() || isCreatingPolylineArcs();
        }

        public boolean isCreatingSplineArcs() {
            return this == CREATING_SPLINE_ARCS;
        }

        public boolean isCreatingPolylineArcs() {
            return this == CREATING_POLYLINE_ARCS;
        }

        public boolean isCreatingFragments() {
            return this == CREATE_FRAGMENTS;
        }

        public boolean isMovingElements() {
            return this == MOVE_ELEMENTS;
        }

        public boolean isMovingFragment() {
            return this == MOVE_FRAGMENT;
        }

        public boolean isMovingEntireModel() {
            return this == MOVE_MODEL;
        }

        public boolean isDeletingElements() {
            return this == DELETE_ELEMENT;
        }

        public boolean isCreationOperation() {
            return isCreatingProperties() || isCreatingConditions() ||
                    isCreatingArcs() || isCreatingFragments() ||
                    isMovingElements() || isMovingFragment() || isMovingEntireModel() || isDeletingElements();
        }

        public boolean isInitialization() {
            return this == INITIALIZATION;
        }

        public boolean isSettingNewEditingOperationBlocked() {
            boolean switchingToAnotherEditingOperationBlocked = isCreatingArcs() || isCreatingFragments() ||
                    isMovingFragment() || isMovingEntireModel() || isDeletingElements();
            return isCreatingArcs() || isCreatingFragments() || isMovingElements() || isMovingFragment() ||
                    isMovingEntireModel() || isDeletingElements();
        }

        public String getText() {
            return text;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    /**
     * O p e r a t i o n   s t e p s
     */
    public static enum OperationStep {
        NONE("None"),
        CANCELED("Canceled"),

        PLACE_NODE("Click on empty space to create new Property node. Ctrl+Click a Node to drag it."+
                " Click empty space to quite drugging."),
        PLACE_CONDITION("Click on empty space to create new Condition node. Ctrl+Click a Node to drag it."+
        " Click empty space to quite drugging."),

        // Arc steps
        PICK_UP_ARC_INPUT_NODE("New Arc:  Click on a Property or Condition node to pick up arc input node."),
        PICK_UP_ARC_FIRST_KNOT_OR_OUTPUT_PROPERTY(
                "Click on empty space to pick up arc knot location or on a Property to make it Arc output node. Right mouse button to undo."),
        PICK_UP_ARC_FIRST_KNOT_OR_OUTPUT_CONDITION(
                "Click on empty space to pick up arc knot location or on a Condition to make it Arc output node. Right mouse button to undo."),
        PICK_UP_ARC_ONLY_KNOT(
                "Click on empty space to pick up curved arc middle knot or output node again to create straight arc. Right mouse button to undo."),
        PICK_UP_THREE_KNOT_ARC_ARROW_TIP_LOCATION(
                "Move and click mouse to pick up three knot arc arrow tip location.  Right mouse button to undo."),
        PICK_UP_ARC_NEXT_KNOT_OR_OUTPUT_NODE(
                "Click to pick up arc next knot location or output node to finish.  Right mouse button to undo."),
        PICK_UP_MULTI_KNOT_ARC_ARROW_TIP_LOCATION("Move and click mouse to pick up arc arrow tip location. Right mouse button to undo."),

        PICK_UP_POLYLINE_ARC_ARROW_TIP_LOCATION("Move and click mouse to pick up arc arrow tip location. Right mouse button to undo."),

        // creating fragment
        PICK_UP_FIRST_PROPERTY("New Fragment:  Click on existing Property node to pick up fragment's input Property."),
        PICK_UP_SECOND_PROPERTY("Click on another existing Property node to pick up fragment's output Property."),
        PICK_UP_CONDITION_LOCATION("Click on any empty area to place there Condition and create its connecting Arcs."),

        // deletion
        PICK_UP_ELEMENT_TO_BE_DELETED("Click to pick up element to be deleted."),
        REMOVE_SELECTED_ELEMENT("Click on selected element to delete, RMB to unselect it, or another element to select."),

        // moving elements
        SELECT_ELEMENT_TO_BE_MOVED("Click on a Node or Arc Arrow to start dragging the Node or the knots."),
        ELEMENT_PROPERTY_SELECTED_START_DRAGGING("The Property node is selected. Start dragging it."),
        ELEMENT_CONDITION_SELECTED_START_DRAGGING("The Condition node is selected. Start dragging it."),
        ELEMENT_ARC_KNOT_SELECTION_AND_DRAGGING("The Arc is selected. Click on a knot to be moved and start dragging it."),
        ELEMENT_ARC_DRAGGING_KNOT("The Arc knot selected. Drag it to new position."),

        // move the model fragment
        START_SELECTING_NODES_TO_BE_MOVED("Click on the first fragment node to be moved."),
        SELECT_NODES_TO_BE_MOVED_AND_LEADING_NODE(
                "Click on the other node(s) to be moved. Click one of selected node again to make it leading."),
        //        FRAGMENT_IS_READY_TO_BE_DRAGGED("Press the mouse on the leading node and start dragging."),
        MODEL_SEGMENT_IS_BEING_DRAGGED("Drop fragment being drugged at the desired location."),
        MODEL_SEGMENT_DRAGGING_IS_PAUSED(
                "Fragment dragging is paused. / Continue drugging. / Click LMB to accept location. / Click RMB to undo move."),

        // move entire model
        CLICK_ON_THE_MODEL_OR_PRES_AND_START_DRAGGING(
                "Press the mouse button on the model and start dragging."),
        DRAG_MODEL_AN_RELEASE_AT_NEW_PLACE("Drag the model and release at new location."),

        // initialization
        INIT_ASSISTANT_IS_IDLING("Initialization is idling"),
        INIT_ASSISTANT_CLOSED("Closed"),
        INITIALIZING_PROPERTY_NODE("Initializing Property node"),
        INITIALIZING_RECOGNIZING_ARC("Initializing Recognizing Arc"),
        INITIALIZING_GENERATING_ARC("Initializing Generating Arc");

        //   O p e r a t i o n   S t e p   I n s t a n c e

        private String text;

        OperationStep(String text) {
            this.text = text;
        }

        public boolean isPickupArcFirstNode() {
            return this == PICK_UP_ARC_INPUT_NODE;
        }

        public boolean isPickupFragmentFirstProperty() {
            return this == PICK_UP_FIRST_PROPERTY;
        }

        public boolean isSelectElementToBeMoved() {
            return this == SELECT_ELEMENT_TO_BE_MOVED;
        }

        public boolean isPickupFirstFragmentNodeToBeMoved() {
            return this == START_SELECTING_NODES_TO_BE_MOVED;
        }

        public boolean isPickupNodesToBeMoved() {
            return this == SELECT_NODES_TO_BE_MOVED_AND_LEADING_NODE;
        }

        public boolean isPickupModelToBeMoved() {
            return this == CLICK_ON_THE_MODEL_OR_PRES_AND_START_DRAGGING;
        }

        public boolean isPeakupForDeletion() {
            return this == PICK_UP_ELEMENT_TO_BE_DELETED;
        }

        public boolean isInitAssistantIdling() {
            return this == INIT_ASSISTANT_IS_IDLING;
        }

        public boolean canBeInterrupted() {
            return this == INIT_ASSISTANT_IS_IDLING || this == INIT_ASSISTANT_CLOSED;
        }

        public boolean isInitializingProperty() {
            return this == INITIALIZING_PROPERTY_NODE;
        }

        public boolean isInitializingArc() {
            return this == INITIALIZING_RECOGNIZING_ARC || this == INITIALIZING_GENERATING_ARC;
        }

        public boolean isCanceled() {
            return this == CANCELED;
        }

        public String getText() {
            return text;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    //
    //  I n s t a n c e
    //

    private static AppStatusPanel appStatusPanel;
    private static Mode prevMode = Mode.NONE;
    private static Mode mode = Mode.NONE;
    private static Operation prevOperation = Operation.NONE;
    private static Operation currentOperation = Operation.NONE;
    private static OperationStep prevOperationStep = OperationStep.NONE;
    private static OperationStep currentOperationStep = OperationStep.NONE;

    private static Operation postponedOperation;
    private static OperationStep postponedOperationStep;

    private static int currentTicks;

    private static List<AppStateModelListener> appStateModelListeners = new CopyOnWriteArrayList();

    private static boolean printToolIsActive;

    /**
     *
     */
    private AppStateModel() {

    }

    public static void setAppStateModelListener(AppStateModelListener appStateModelListener) {
        appStateModelListeners.add(appStateModelListener);

    }


    public static void setAppStatusPanel(AppStatusPanel appStatusPanel) {
        AppStateModel.appStatusPanel = appStatusPanel;
    }

    public static void updateModelName(String modelName) {
//        String modelName = MclnProject.getCurrentMclnProject().getCurrentMclnModel().getModelName();
//        String modelName = DesignSpaceModel.getInstance().getMclnProject().getCurrentMclnModel().getModelName();
        appStatusPanel.updateModelName(modelName);
    }

//    public static String getModelName() {
//        String modelName = DesignSpaceModel.getInstance().getMclnProject().getCurrentMclnModel().getModelName();
//        return modelName;
//    }

    public static Mode getMode() {
        return mode;
    }

    public static void setDevelopmentMode() {
        setMode(AppStateModel.Mode.DEVELOPMENT);
    }

    public static void setSimulationMode() {
        setMode(AppStateModel.Mode.SIMULATION);
    }

    private static void setCurrentOperationAvailability() {
        boolean operationCanBeInterrupted = canOperationBeInterrupted();
//        if (operationCanBeInterrupted) {
//            AppController.getInstance().makeInitAssistantAvailable();
//            AppController.getInstance().makeDeleteOperationAvailable();
//        } else {
//            AppController.getInstance().makeInitAssistantUnavailable();
//            AppController.getInstance().makeDeleteOperationUnavailable();
//        }
        AppController.getInstance().enableDisableCurrentCommand(operationCanBeInterrupted);
    }

//    /**
//     *
//     */
//    private void setInitializationAvailability() {
//        boolean operationCanBeInterrupted = canOperationBeInterrupted();
//        if (operationCanBeInterrupted) {
//            AppController.getInstance().makeInitAssistantAvailable();
//        } else {
//            AppController.getInstance().makeInitAssistantUnavailable();
//        }
//    }
//
//    private void setDeleteOperationAvailability() {
//        boolean operationCanBeInterrupted = canOperationBeInterrupted();
//        if (operationCanBeInterrupted) {
//            AppController.getInstance().makeDeleteOperationAvailable();
//        } else {
//            AppController.getInstance().makeDeleteOperationUnavailable();
//        }
//    }


    private static void setMode(Mode mode) {
        AppStateModel.mode = mode;
        appStatusPanel.updateMode();
//        setInitializationAvailability();
//        setDeleteOperationAvailability();
        fireStateChanged();
    }

    public static Operation getCurrentOperation() {
        return currentOperation;
    }

    /**
     * Method is used to set Simulation operation.
     * Simulation operations do not have steps.
     *
     * @param currentOperation
     */
    public static void setCurrentSimulationOperation(Operation currentOperation) {
        AppStateModel.currentOperation = currentOperation;
//        setInitializationAvailability();
//        setDeleteOperationAvailability();
        fireStateChanged();
    }

    public static boolean isDevelopmentMode() {
        return mode == Mode.DEVELOPMENT;
    }

    public static boolean isSimulationMode() {
        return mode == Mode.SIMULATION;
    }

    public static boolean isOperationCreateNodes() {
        return currentOperation == Operation.CREATE_PROPERTIES;
    }

    public static OperationStep getCurrentOperationStep() {
        return currentOperationStep;
    }

    /**
     * @param currentOperationStep
     */
    public static void setCurrentOperationStep(OperationStep currentOperationStep) {
        AppStateModel.currentOperationStep = currentOperationStep;
        setCurrentOperationAvailability();
        appStatusPanel.updateStatus();
    }

    public static boolean isStepPlaceNode() {
        return currentOperationStep == OperationStep.PLACE_NODE;
    }

    public static boolean isCurrentOperation(Operation operation) {
        return AppStateModel.currentOperation == operation;
    }

    public static void setNewEditingOperation(Operation newOperation, OperationStep newOperationStep) {

        /*
          Case: User opens Init Assistant.
//          This case can be initiated only when current operation

          Init Assistant is not open
          Some editing currentOperation is set, but not yet started, hence Init Assistant button is enabled.
          User clicks Init Assistant button.
          Transition:
          Current currentOperation and its step are postponed.
          Current currentOperation set to INITIALIZATION
          Current currentOperation step set to NONE
          Postponed currentOperation should be unselected
         */
        if (postponedOperation == null && currentOperation != Operation.NONE &&
                newOperation == Operation.INITIALIZATION &&
                (newOperationStep != OperationStep.INIT_ASSISTANT_IS_IDLING &&
                        newOperationStep != OperationStep.INIT_ASSISTANT_CLOSED)) {

            postponedOperation = currentOperation;
            postponedOperationStep = currentOperationStep;
            currentOperation = newOperation;
            currentOperationStep = newOperationStep;

            AppController.getInstance().setOperationSelected(postponedOperation.toString(), Boolean.FALSE);

            setCurrentOperationAvailability();
//        setInitializationAvailability();
//        setDeleteOperationAvailability();
            fireStateChanged();
            return;
        }

        /*
            Case: User Canceled Initialization currentOperation.
            There is postponed currentOperation in overSetOperation.
            Current currentOperation is INITIALIZATION
            Current currentOperation step is not NONE (Init Assistant was initializing graph entity)
            Transition:
            User either clicked "Cancel" button or finished Initialization
            postponed currentOperation and currentOperation step are restored
            Postponed currentOperation should be reselected
         */
        if (postponedOperation != null && currentOperation == Operation.INITIALIZATION && currentOperationStep != OperationStep.NONE &&
                newOperation == Operation.INITIALIZATION && newOperationStep == OperationStep.INIT_ASSISTANT_IS_IDLING) {

            currentOperation = postponedOperation;
            currentOperationStep = postponedOperationStep;
            postponedOperation = null;
            postponedOperationStep = null;

            AppController.getInstance().setOperationSelected(currentOperation.toString(), Boolean.TRUE);

            setCurrentOperationAvailability();
//        setInitializationAvailability();
//        setDeleteOperationAvailability();
            fireStateChanged();
            return;
        }

        /*
            Case: User or other components Closed Initialization currentOperation.
            There is postponed currentOperation in overSetOperation.
            Current currentOperation is INITIALIZATION
            Current currentOperation step is not NONE (Init Assistant was initializing graph entity)
            Transition:
            User either clicked "Cancel" button or finished Initialization
            postponed currentOperation and currentOperation step are restored
            Postponed currentOperation should be reselected
         */
        if (newOperation == Operation.INITIALIZATION && newOperationStep == OperationStep.INIT_ASSISTANT_CLOSED) {
            if (postponedOperation != null) {
                currentOperation = postponedOperation;
                currentOperationStep = postponedOperationStep;
                postponedOperation = null;
                postponedOperationStep = null;
            } else if (currentOperation == Operation.NONE || currentOperation == Operation.INITIALIZATION) {
                currentOperation = Operation.NONE;
                currentOperationStep = OperationStep.NONE;
            } else {
                /**
                 * if current editing operation is not at blocking step the do not change state
                 */
            }

            AppController.getInstance().setOperationSelected(currentOperation.toString(), Boolean.TRUE);
            setCurrentOperationAvailability();
            fireStateChanged();
            return;
        }

        prevOperation = AppStateModel.currentOperation;
        AppStateModel.currentOperation = newOperation;
        prevOperationStep = AppStateModel.currentOperationStep;
        AppStateModel.currentOperationStep = newOperationStep;
        setCurrentOperationAvailability();
//        setInitializationAvailability();
//        setDeleteOperationAvailability();
        fireStateChanged();
    }

    public static void updateDemoMode(Operation model) {
        prevOperation = AppStateModel.currentOperation;
        AppStateModel.currentOperation = Operation.NONE;
        prevOperationStep = AppStateModel.currentOperationStep;
        AppStateModel.currentOperationStep = OperationStep.NONE;
        fireStateChanged();
    }

    public static void updateSimulationOperation(Operation operation) {
        AppStateModel.currentOperation = operation;
        fireStateChanged();
    }

    public static void updateSimulationTicks(int currentTicks) {
        AppStateModel.currentTicks = currentTicks;
        fireStateChanged();
    }

    public static String getSimulationTicks() {
        return Integer.toString(currentTicks);
    }

    private static void fireStateChanged() {
        appStatusPanel.updateStatus();
        switch (mode) {
            case DEVELOPMENT:
            case SIMULATION:
                for (AppStateModelListener appStateModelListener : appStateModelListeners) {
                    appStateModelListener.stateChanged();
                }
                break;
        }
    }

    public static final void printState() {
//        System.out.println("AppStateModel: Current state: currentOperation = " + currentOperation.toString() + ", step = " + currentOperationStep);
    }

    public static boolean isPrintToolActive() {
        return printToolIsActive;
    }

    public static void setPrintToolIsActive(boolean printToolIsActive) {
        AppStateModel.printToolIsActive = printToolIsActive;
        AppController.getInstance().enableDisableCurrentCommand(!printToolIsActive);
    }

    public static boolean isSettingNewEditingOperationBlocked() {
        return currentOperation.isSettingNewEditingOperationBlocked();
    }

    /**
     * Usage:
     * 1) by Editing Popup Menu
     *
     * @return
     */
    public static boolean isCreationOperationOn() {
        boolean creationOperationIsOn = isDevelopmentMode() && (currentOperation.isCreatingProperties() ||
                currentOperation.isCreatingConditions() ||
                currentOperation.isCreatingArcs() ||
                currentOperation.isCreatingFragments() ||
                currentOperation.isMovingElements() ||
                currentOperation.isMovingFragment() ||
                currentOperation.isMovingEntireModel() ||
                currentOperation.isDeletingElements() ||
                (currentOperation.isInitialization() && !currentOperationStep.isInitAssistantIdling())
        );
        return creationOperationIsOn;
    }

    /**
     * The method is used to block:
     * 1) Ini tAssistant
     * 2) Editor Popup Menu
     *
     * @return
     */
    public static boolean canOperationBeInterrupted() {
        boolean g = currentOperation.isNone();
        boolean initializationPossible = isDevelopmentMode() && (
//                currentOperation.isCreationEnabled() ||
                currentOperation.isNone() ||
//                        !currentOperation.isDeletingElements() || it does not work well
                        currentOperation.isCreatingProperties() || currentOperation.isCreatingConditions() ||
                        (currentOperation.isCreatingArcs() && currentOperationStep.isPickupArcFirstNode()) ||
                        (currentOperation.isCreatingFragments() && currentOperationStep.isPickupFragmentFirstProperty()) ||
                        (currentOperation.isMovingElements() && currentOperationStep.isSelectElementToBeMoved()) ||
                        (currentOperation.isMovingFragment() && currentOperationStep.isPickupFirstFragmentNodeToBeMoved()) ||
                        (currentOperation.isMovingEntireModel() && currentOperationStep.isPickupModelToBeMoved()) ||
                        (currentOperation.isDeletingElements() && currentOperationStep.isPeakupForDeletion())
                        || (currentOperation.isInitialization() && currentOperationStep.canBeInterrupted()
                )
        );
        return initializationPossible;
    }


    /**
     * This method is currently used by Editor Popup only
     *
     * @return
     */
    public static boolean canInitAssistantBeInitialized() {
//        System.out.println("AppStateModel: Current operation: " + currentOperation);
//        System.out.println("AppStateModel: Current step:      " + currentOperationStep);
        boolean initializationPossible = isDevelopmentMode() && (
//                currentOperation.isCreationEnabled() ||
                currentOperation.isNone() ||
//                        !currentOperation.isDeletingElements() || iot does not work well
                        currentOperation.isCreatingProperties() || currentOperation.isCreatingConditions() ||
                        (currentOperation.isCreatingArcs() && currentOperationStep.isPickupArcFirstNode()) ||
                        (currentOperation.isCreatingFragments() && currentOperationStep.isPickupFragmentFirstProperty()) ||
                        (currentOperation.isMovingElements() && currentOperationStep.isSelectElementToBeMoved()) ||
                        (currentOperation.isMovingFragment() && currentOperationStep.isPickupFirstFragmentNodeToBeMoved()) ||
                        (currentOperation.isMovingEntireModel() && currentOperationStep.isPickupModelToBeMoved()) ||
                        (currentOperation.isDeletingElements() && currentOperationStep.isPeakupForDeletion())
                        ||
                        (currentOperation.isInitialization()
//                                && currentOperationStep.isInitAssistantIdling()
                        )
        );
        return initializationPossible;
    }
}
