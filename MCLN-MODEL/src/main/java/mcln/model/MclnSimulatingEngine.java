package mcln.model;


import mcln.palette.BasicColorPalette;
import mcln.palette.MclnState;
import mcln.simulator.SimulatedStateChangeListener;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created with IntelliJ IDEA.
 * User: XP Admin
 * Date: 12/12/13
 * Time: 8:20 PM
 * To change this template use File | Settings | File Templates.
 */
final class MclnSimulatingEngine {

//    private final Logger logger = Logger.getLogger(MclnSimulatingEngine.class.getSubject());

    static final int DONT_EXEC = 0;
    static final int DDLS_EXEC = 1;
    static final int DDRS_EXEC = 2;
    static final int DEDS_EXEC = 3;

    private int execMode = DEDS_EXEC;
    private MclnModel mclnModel;

    private StringBuilder tickResponseStringBuilder = new StringBuilder();

    private final LinkedBlockingQueue<MclnModelEvent> linkedBlockingEventQueue = new LinkedBlockingQueue();

    private final CopyOnWriteArrayList<SimulatedStateChangeListener> simulatedStateChangeListeners =
            new CopyOnWriteArrayList();

    /**
     * This two interfaces connect McLN Model to outer world
     */
    private final CopyOnWriteArrayList<ExternalActionRequestProcessor> externalActionRequestProcessors =
            new CopyOnWriteArrayList();

    ExternalEventListener externalEventListener = new ExternalEventListener() {

        /**
         * Currently called from controlled SEM Front Touch Sensor
         *
         * @param mclnModelEvent
         */
        @Override
        public void processInputEvent(MclnModelEvent mclnModelEvent) {
            System.out.println("EVENT: " + mclnModelEvent.toString());
            String sourceUID = mclnModelEvent.getSourceUID();
            MclnStatement mclnStatement = mclnModel.getMclnStatementByUID(sourceUID);

            String mclnStatementUID = mclnStatement.getUID();
            int newStateID = BasicColorPalette.RED_STATE_ID;
            MclnStatementState newMclnStatementState = mclnStatement.setNewCurrentMclnStateByStateAttribute(newStateID);
            fireSimulatedPropertyStateChanged(mclnStatement);

            MclnState newCurrentState = newMclnStatementState.getMclnState();
            MclnModelEvent externalMclnModelInputEvent = new MclnModelEvent(mclnStatementUID,
                    MclnModelEvent.EventType.PGM_INPUT, newCurrentState);
            System.out.println("E x t e r n a l McLN Model Input Event : putting external event into queue "
                    + externalMclnModelInputEvent.toString() + "\n");
            try {
                linkedBlockingEventQueue.put(externalMclnModelInputEvent);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mclnModel.fireModelStateChanged();
        }
    };

    ExternalActionResponseProcessor externalActionResponseProcessor = new ExternalActionResponseProcessor() {
        @Override
        public void processActionResponse(String effectorID, String response) {
            System.out.println("E x t e r n a l   Action   Response, node ID = " + effectorID + ",  response = " + response + "\n");

            MclnStatement mclnStatement = mclnModel.getMclnStatementByUID(effectorID);
            int newStateID = BasicColorPalette.YELLOW_STATE_ID;
            if (response.equals("ack")) {
                newStateID = BasicColorPalette.YELLOW_STATE_ID;
            } else if (response.equals("done")) {
                newStateID = BasicColorPalette.CYAN_STATE_ID;
            }

            MclnStatementState newMclnStatementState = mclnStatement.setNewCurrentMclnStateByStateAttribute(newStateID);
            mclnStatement.setCurrentStatementState(newMclnStatementState);
            MclnState newCurrentState = newMclnStatementState.getMclnState();
            MclnModelEvent externalMclnModelInputEvent = new MclnModelEvent(effectorID,
                    MclnModelEvent.EventType.PGM_INPUT, newCurrentState);
            System.out.println("E x t e r n a l McLN Model Input Event : putting external event into queue "
                    + externalMclnModelInputEvent.toString() + "\n");


            try {
                linkedBlockingEventQueue.put(externalMclnModelInputEvent);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//        fireSimulatedPropertyStateChanged(mclnStatement);
            mclnModel.fireModelStateChanged();
        }
    };

    /**
     * Method called when user selects Property State from popup menu
     *
     * @param uid
     * @param userProvidedMclnState
     */
    void processUserProvidedRuntimePropertyState(String uid, MclnState userProvidedMclnState) {
        MclnStatement mclnStatement = mclnModel.getMclnStatementByUID(uid);
        mclnStatement.setCurrentMclnState(userProvidedMclnState);
        MclnModelEvent userInputMclnModelEvent = new MclnModelEvent(mclnStatement.getUID(),
                MclnModelEvent.EventType.PGM_INPUT, mclnStatement.getCurrentMclnState());
        System.out.println("U s e r   I n p u t : putting user event into queue "
                + userInputMclnModelEvent.toString() + "\n");
        try {
            linkedBlockingEventQueue.put(userInputMclnModelEvent);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        fireSimulatedPropertyStateChanged(mclnStatement);
        mclnModel.fireModelStateChanged();
    }


    private final Set<MclnCondition> processedConditions = new HashSet<>();

    //   C o n s t r u c t i n g   i n s t a n c e

    MclnSimulatingEngine() {
        System.out.println("McLN Simulating Engine constructed");
    }

    ExternalEventListener getExternalEventListener() {
        return externalEventListener;
    }

    public ExternalActionResponseProcessor getExternalActionResponseProcessor() {
        return externalActionResponseProcessor;
    }

    void addMclnModelExternalActionProcessor(ExternalActionRequestProcessor externalActionRequestProcessor) {
        externalActionRequestProcessors.add(externalActionRequestProcessor);
    }

    /**
     * The listener is a component that creates list of MclnStatements changes during simulation cycle
     */
    void addStateChangeListener(SimulatedStateChangeListener simulatedStateChangeListener) {
        simulatedStateChangeListeners.add(simulatedStateChangeListener);
    }

    void clearSimulation() {
        linkedBlockingEventQueue.clear();
    }

    /**
     * @param mclnModel
     */
    void setMclnModel(MclnModel mclnModel) {
        this.mclnModel = mclnModel;
    }

    void initializeSimulation() {
        initialiseModelForSimulation();
    }

    /**
     * S t a r t   S i m u l a t i o n
     */
    void startSimulation() {
//        System.out.println("Start Execution");
        setModelStartedStoppedState(true);
        //        System.out.println("Start Execution, simulation running set to TRUE\n");
    }

    /**
     * S t o p   S i m u l a t i o n
     */
    void stopSimulation() {
        setModelStartedStoppedState(false);
    }

    /**
     * R e s e t   S i m u l a t i o n
     * i s   u s e d   b y   W E B   a n d   D e s k t o p   G U I
     */
    String resetSimulation() {
        setModelStartedStoppedState(false);
        tickResponseStringBuilder.delete(0, tickResponseStringBuilder.length());

        List<MclnStatement> mclnStatements = mclnModel.getMclnStatements();
        for (MclnStatement mclnStatement : mclnStatements) {
            mclnStatement.initializeSimulation();
            String statementUID = mclnStatement.getUID();
            MclnState currentState = mclnStatement.getCurrentMclnState();
            tickResponseStringBuilder.append(statementUID).append(":").append(currentState.toView()).append("#");
        }
        calculateAllConditionsState();

        String updatedStates = tickResponseStringBuilder.toString();
        tickResponseStringBuilder.delete(0, tickResponseStringBuilder.length());
        return updatedStates;
    }

    /**
     * E x e c u t e   O n e   S i m u l a t i o n   S t e p
     */
    final String executeOneSimulationStep() {
        return processSimulatingTick();
    }

    String simulationDebugTraceMessage = "\n" +
            "\n================================================================\n" +
            "E n t e r i n g   P r o c e s s    S i m u l a t i n g   T i c k" +
            "\n================================================================\n\n";

    /**
     * @return
     */
    private final String processSimulatingTick() {
//        System.out.println(simulationDebugTraceMessage);

        if (mclnModel == null) {
            //  System.out.println("     Mcln Execution Engine: Variable mclnModel is null");
            return "";
        }

        //  System.out.println("     Processing Simulating Tick ...simulation running = TRUE\n");

//        if (!modelInitialised) {
//            System.out.println("     initializing model");
//            initModelForSimulation();
//            modelInitialised = true;
//            System.out.println("     model initialized\n");
//            return "";
//        }

        //
        //   processing newly generated events
        //
        tickResponseStringBuilder.delete(0, tickResponseStringBuilder.length());
        boolean inputGenerated = simulateModelInput();
//        System.out.println("\n Process Simulating Tick: input generated " + inputGenerated);
        if (inputGenerated) {
//            System.out.println(" Process Simulating Tick: input placed into queue ");
        }
//        System.out.println("\n Process Simulating Tick: executing one queue unload cycle ");
        executeOneStep();
        String serverReturn = tickResponseStringBuilder.toString();
//        System.out.println("\n S e r v e r   R e t u r n:\n" + serverReturn);
        return tickResponseStringBuilder.toString();
    }

    /**
     * generating input events
     */
    private boolean simulateModelInput() {
//        System.out.println("     Running Statement programs\n");
        boolean inputGenerated = false;
        List<MclnStatement> mclnStatements = mclnModel.getMclnStatements();
        for (MclnStatement mclnStatementForProgramSimulation : mclnStatements) {
//            System.out.println("processing statement "+mclnStatementForProgramSimulation.getUID());
//            if(mclnStatementForProgramSimulation.getUID().equals("S-0000009") ||
//                    mclnStatementForProgramSimulation.getUID().equals("S-0000032")){
//                System.out.println();
//            }
            String advancedState = mclnStatementForProgramSimulation.doInputSimulatingProgramStep();

            if (advancedState == null) {
                continue;
            }

            // adding this call causes simulation problem in DP
            // the call was added on 8/14/2017, I do n ot remember for
            // I commented it out to fix simulation misbehavior
//            calculateStatementConditionStates(mclnStatementForProgramSimulation);

//                System.out.println("\n State advanced by Input Simulatinig program " + mclnStatementForProgramSimulation.getUID() +
//                        ", advancedState = " + advancedState + "\n");
            tickResponseStringBuilder.append(advancedState).append("#");

            MclnModelEvent mclnModelEventForNextTick = new MclnModelEvent(mclnStatementForProgramSimulation.getUID(),
                    MclnModelEvent.EventType.PGM_INPUT, MclnState.EMPTY_STATE);
            inputGenerated = true;
            try {
                linkedBlockingEventQueue.put(mclnModelEventForNextTick);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return inputGenerated;
    }

    /**
     *
     */
    private void executeOneStep() {

//        System.out.println("Engine.execute One Step queue " + linkedBlockingEventQueue.size() + "\n");

        if (linkedBlockingEventQueue.isEmpty()) {
            return;
        }

        int iCnt = 0;
        for (Iterator<MclnModelEvent> iterator = linkedBlockingEventQueue.iterator(); iterator.hasNext(); ) {
            MclnModelEvent mclnModelEvent = iterator.next();
//            System.out.println("Queue Entry " + iCnt + "  " + mclnModelEvent.toString());
        }
//        for (int i = 0; i < queueSize; i++) {
//            MclnModelEvent mclnModelEvent = linkedBlockingEventQueue.peek();
//            System.out.println("Queue Entry " + i + "  " + mclnModelEvent.toString());
//        }

        MclnModelEvent dividerEvent = MclnModelEvent.DIVIDER_EVENT;
        try {
            linkedBlockingEventQueue.put(dividerEvent);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // unloading event queue
        do {
            MclnModelEvent mclnModelEvent = linkedBlockingEventQueue.poll();
            if (mclnModelEvent == null) {
                break;
            }

            if (mclnModelEvent.isDivider()) {
                //  System.out.println("Engine.execute One Step Divider found - Stop cycle\n");
                break;
            }
            //  System.out.println("Engine.execute One Step queue processing event" + mclnModelEvent.toString() + "\n");
            processStateChangeEvent(mclnModelEvent);

        } while (true);
        // this call will go to MclnGraphView
        mclnModel.fireModelStateChanged(); // this will make Model View regenerated
        mclnModel.fireSimulationStepExecuted(); // this will make Execution State and Log panels to be called
    }

    /**
     * @param mclnModelEvent
     */
    private void processStateChangeEvent(MclnModelEvent mclnModelEvent) {

//        System.out.println("     Processing Simulating Tick ...processing event " + mclnModelEvent.toString() + "\n");
        String statementUID = mclnModelEvent.getSourceUID();
        MclnStatement mclnStatement = mclnModel.getMclnStatementByUID(statementUID);
        if (mclnModelEvent.isProgramInput()) {
            fireSimulatedPropertyStateChanged(mclnStatement);
        }
        int nEventsAddedToQueue = propagateStateChangeFromStatementToDependentStatements(mclnStatement);
    }

    /**
     *
     */
    private void initialiseModelForSimulation() {
        if (mclnModel == null || mclnModel.isEmpty()) {
            return;
        }

        List<MclnStatement> mclnStatements = mclnModel.getMclnStatements();
        for (MclnStatement mclnStatement : mclnStatements) {
            mclnStatement.initializeSimulation();
        }
        calculateAllConditionsState();
        boolean modelHasInputGeneratingStatements = modelHasInputGeneratingStatements();
        if (!modelHasInputGeneratingStatements) {
            findSimulationTriggeringStatement();
        }
    }

    private void findSimulationTriggeringStatement() {
        List<MclnCondition> mclnConditions = mclnModel.getMclnConditions();

        for (MclnCondition mclnCondition : mclnConditions) {
            MclnStatement mclnStatement = getActiveSituationStatement(mclnCondition);
            if (mclnStatement != null) {
                placeTheMclnModelEvent(mclnStatement);
                break;
            }
        }
    }

    private void placeTheMclnModelEvent(MclnStatement mclnStatement) {
        MclnModelEvent mclnModelEvent = new MclnModelEvent(mclnStatement.getUID(),
                MclnModelEvent.EventType.INFERENCE, mclnStatement.getCurrentMclnState());
//        System.out.println("I n f e r e n c e putting new event into queue " + mclnModelEvent.toString() + "\n");

//        int iCnt = 0;
//        for (Iterator<MclnModelEvent> iterator = linkedBlockingEventQueue.iterator(); iterator.hasNext(); ) {
//            MclnModelEvent queuedMclnModelEvent = iterator.next();
//            System.out.println("Queue Entry before one added " + iCnt++ + "  " + queuedMclnModelEvent.toString());
//        }
        try {
            linkedBlockingEventQueue.put(mclnModelEvent);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private MclnStatement getActiveSituationStatement(MclnCondition mclnCondition) {
        MclnStatement inpStatement = null;
        MclnState newState = MclnState.CORE_STATE_NOTHING;
        List<MclnArc<MclnStatement, MclnCondition>> allInpArcs = mclnCondition.getInboundArcs();
        for (MclnArc<MclnStatement, MclnCondition> conditionInpArc : allInpArcs) {
            inpStatement = conditionInpArc.getInpNode();
            MclnState statementState = inpStatement.getCurrentMclnState();
            MclnState arcState = conditionInpArc.getArcMclnState();
//            System.out.println("Statement state     " + statementState.toString());
//            System.out.println("arcState            " + arcState.toString());
            MclnState tmpState = arcState.applyDiscolorOperation(statementState);
//            System.out.println("tmp state " + tmpState.toString());
            newState = newState.applyConjunctionOperation(tmpState);
//            System.out.println("Condition new state     " + newState.toString());
            if (newState.isStateContradiction()) {
                break;
            }
        }
        return newState.isSituationRecognised() ? inpStatement : null;
    }

    // ================================================================================================================

    public boolean modelHasInputGeneratingStatements() {
        List<MclnStatement> mclnStatements = mclnModel.getMclnStatements();
        for (MclnStatement mclnStatement : mclnStatements) {
            if (mclnStatement.hasInputGeneratingProgram()) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param start
     */
    private void setModelStartedStoppedState(boolean start) {
        if (mclnModel == null) {
            return;
        }
        List<MclnStatement> mclnStatements = mclnModel.getMclnStatements();
        for (MclnStatement mclnStatement : mclnStatements) {
            if (start) {
                mclnStatement.setSimulationStarted();
            } else {
                mclnStatement.setSimulationStopped();
            }
        }
    }


    private void calculateAllConditionsState() {
        List<MclnCondition> mclnConditions = mclnModel.getMclnConditions();
//        System.out.println("Calc All Conditions State: n = " + mclnConditions.size());
        for (MclnCondition mclnCondition : mclnConditions) {
            calculateConditionState(mclnCondition);
        }
    }

    /**
     * forward chaining
     *
     * @param mclnStatement
     */


    private int propagateStateChangeFromStatementToDependentStatements(MclnStatement mclnStatement) {
        int added = 0;
        //  System.out.println("\n I n f e r e n c e  from statement " + mclnStatement.getUID() + "\n");
        processedConditions.clear();
        List<MclnArc<MclnStatement, MclnCondition>> statementOutboundArcs = mclnStatement.getOutboundArcs();
//        System.out.println("statement outbound arcs  n = " + statementOutboundArcs.size());
        if (statementOutboundArcs.size() == 0) {
            return added;
        }

        //
        // Calculating state of all outbound conditions
        //
        for (MclnArc<MclnStatement, MclnCondition> mclnArc : statementOutboundArcs) {
            MclnCondition outboundMclnCondition = mclnArc.getOutNode();
//            System.out.println("statement outbound Condition  " + outboundMclnCondition.getUID());
            calculateConditionState(outboundMclnCondition);
            processedConditions.add(outboundMclnCondition);
        }

        if (processedConditions.size() == 0) {
            return added;
        }

        //
        // Propagating new state of each condition to its outbound statements
        //
//        System.out.println("\nPropagating new state of each condition to its outbound statements\n");
        for (MclnCondition processedCondition : processedConditions) {
//            System.out.println("        Propagating from  condition  " + processedCondition.getUID());
            List<MclnArc<MclnCondition, MclnStatement>> conditionOutboundArcs = processedCondition.getOutboundArcs();
//            System.out.println("condition outbound arcs  n = " + conditionOutboundArcs.size() + "\n");
            if (conditionOutboundArcs.size() == 0) {
                continue;
            }

            //
            // Calculating state of all outbound statements
            //
            for (MclnArc<MclnCondition, MclnStatement> conditionOutArc : conditionOutboundArcs) {
                MclnStatement affectedMclnStatement = conditionOutArc.getOutNode();
                boolean stateUpdated = calculateEffectedStatementState(affectedMclnStatement);
                if (!stateUpdated) {
                    continue;
                }

//                calculateStatementConditionStates(affectedMclnStatement);

                MclnModelEvent mclnModelEvent = new MclnModelEvent(affectedMclnStatement.getUID(),
                        MclnModelEvent.EventType.INFERENCE, affectedMclnStatement.getCurrentMclnState());
//                System.out.println("I n f e r e n c e putting new event into queue " + mclnModelEvent.toString() + "\n");

                int iCnt = 0;
                for (Iterator<MclnModelEvent> iterator = linkedBlockingEventQueue.iterator(); iterator.hasNext(); ) {
                    MclnModelEvent queuedMclnModelEvent = iterator.next();
//                    System.out.println("Queue Entry before one added " + iCnt++ + "  " + queuedMclnModelEvent.toString());
                }
                try {
                    linkedBlockingEventQueue.put(mclnModelEvent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                System.out.println("\n");
                for (Iterator<MclnModelEvent> iterator = linkedBlockingEventQueue.iterator(); iterator.hasNext(); ) {
                    MclnModelEvent queuedMclnModelEvent = iterator.next();
//                    System.out.println("Queue Entry after one added " + iCnt++ + "  " + queuedMclnModelEvent.toString());
                }

                added++;
            }
        }

        return added;
    }

    /**
     * Calculates state of all outbound conditions of the statement
     *
     * @param mclnStatement
     */
    private void calculateStatementConditionStates(MclnStatement mclnStatement) {
        List<MclnArc<MclnStatement, MclnCondition>> statementOutboundArcs = mclnStatement.getOutboundArcs();
        if (statementOutboundArcs.size() == 0) {
            return;
        }
        for (MclnArc<MclnStatement, MclnCondition> mclnArc : statementOutboundArcs) {
            MclnCondition outboundMclnCondition = mclnArc.getOutNode();
//            System.out.println("statement outbound Condition  " + outboundMclnCondition.getUID());
            calculateConditionState(outboundMclnCondition);
        }
    }

    /**
     * @param mclnCondition
     */
    private void calculateConditionState(MclnCondition mclnCondition) {
        MclnState newState = MclnState.CORE_STATE_NOTHING;
//        if (mclnCondition.getUID().equals("C-0000002")) {
//            System.out.println();
//        }

        /*  !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            There was Null Pointer Exception on the line below. Can this Condition be null ?
            !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
         */
        List<MclnArc<MclnStatement, MclnCondition>> allInpArcs = mclnCondition.getInboundArcs();
        for (MclnArc<MclnStatement, MclnCondition> conditionInpArc : allInpArcs) {
            MclnStatement inpStatement = conditionInpArc.getInpNode();
            MclnState statementState = inpStatement.getCurrentMclnState();
            MclnState arcState = conditionInpArc.getArcMclnState();
            System.out.println("Statement state     " + statementState.toString());
            System.out.println("arcState            " + arcState.toString());
            MclnState recognitionState = arcState.applyDiscolorOperation(statementState);
            conditionInpArc.setCalculatedProducedState(recognitionState);
            System.out.println("recognitionState " + recognitionState.toString());
            newState = newState.applyConjunctionOperation(recognitionState);
//            System.out.println("Condition new state     " + newState.toString());
            if (newState.isStateContradiction()) {
                break;
            }
        }
        //  System.out.println("calculateConditionState: Condition new state  " + mclnCondition.getUID() + ",  " + newState.toString());
        mclnCondition.setCurrentMclnState(newState);
//        mclnCondition.fireConditionStateChanged(newState);
    }

    /**
     * @param effectedMclnStatement
     * @return
     */
    private boolean calculateEffectedStatementState(MclnStatement effectedMclnStatement) {
        List<MclnArc<MclnCondition, MclnStatement>> allInpArcs = effectedMclnStatement.getInboundArcs();
        //  System.out.println("**************************" +
//                "\nCalculating affected statement color " + effectedMclnStatement.toString() + ",  n inp arcs =  " + allInpArcs.size() +
//                "\n**************************");
        MclnState newState = MclnState.CORE_STATE_NOTHING;
        for (MclnArc<MclnCondition, MclnStatement> statementInpArc : allInpArcs) {
            MclnCondition statementInpCondition = statementInpArc.getInpNode();
            //  System.out.println("\nAffectedStatement input condition " + statementInpCondition.getUID() + ",  current state      " + statementInpCondition.toString());
            //  System.out.println("AffectedStatement input arc       " + statementInpArc.getUID() + "\n");
            if (!isConditionProcessedInCurrentLoop(statementInpCondition)) {
                if (!statementInpCondition.isNodeInBound(effectedMclnStatement)) {
//                    calculateConditionState(statementInpCondition);
                }

            }

            MclnState conditionState = statementInpCondition.getCurrentMclnState();
            MclnState arcState = statementInpArc.getArcMclnState();
//
//            System.out.println("Arc State            " + arcState.toString());
//            System.out.println("Condition state      " + conditionState.toString());

            MclnState arcProducedState = arcState.applyColorizeOperation(conditionState);
            statementInpArc.setCalculatedProducedState(arcProducedState);

//              System.out.println("Colorizing: tmp state is " + tmpState.toString() + " = " + arcState.toString() + " * " + conditionState.toString());
            String addition = newState.getColorName();
            newState = newState.applyDisjunctionOperation(arcProducedState);
//              System.out.println("new state after Disjunction is " + newState.toString() + " = " + addition + " + " + tmpState.toString());
            //  System.out.println();
            if (newState.isStateUnknown()) {
                effectedMclnStatement.setCalculatedSuggestedState(newState);
//                  System.out.println(effectedMclnStatement.getUID() + ": new state " + newState.toString() + " is Unknown, No update");
                return false;
            }
        }
//          System.out.println(effectedMclnStatement.getUID() + ": statement new state     " + newState.toString());
        MclnState currentState = effectedMclnStatement.getCurrentMclnState();
        if (newState == currentState) {
            effectedMclnStatement.setCalculatedSuggestedState(newState);
//              System.out.println("\n\n Statement NOT updated new state is same as current     " + newState.toString());
            return false;
        }
        if (MclnAlgebra.isCore(newState.getState())) {
            effectedMclnStatement.setCalculatedSuggestedState(newState);
//              System.out.println("\n\n Statement NOT updated new state is Core - ignored     " + newState.toString());
            return false;
        }

        String statementUID = effectedMclnStatement.getUID();
//          System.out.println("\n\n Statement updated " + effectedMclnStatement.getUID() + "  " + newState.toString() + "\n");
        effectedMclnStatement.setCalculatedSuggestedState(newState);
        effectedMclnStatement.setCurrentMclnState(newState);
        fireSimulatedPropertyStateChanged(effectedMclnStatement);
//        mclnStatement.fireSimulatedPropertyStateChanged(newState);

        // recording state change
        String statementStateAsString = effectedMclnStatement.getStatementStateAsString();
        statementStateAsString = (statementStateAsString.length() == 0) ? newState.toView() : statementStateAsString;
        tickResponseStringBuilder.append(statementUID).append(":").append(statementStateAsString).append("#");
//        System.out.println("\n\n +++++++++++++++++++++++++++++++++++++++++++++++++++\n" + tickResponseStringBuilder.toString());
        if (effectedMclnStatement.isActivator()) {
            callExternalActionRequestProcessor(effectedMclnStatement);
        }
        return true;
    }


    private boolean isConditionProcessedInCurrentLoop(MclnCondition mclnCondition) {
        if (processedConditions.size() == 0) {
            return false;
        }
        return processedConditions.contains(mclnCondition);
    }

    private void fireSimulatedPropertyStateChanged(MclnStatement mclnStatement) {
        for (SimulatedStateChangeListener simulatedStateChangeListener : simulatedStateChangeListeners) {
            simulatedStateChangeListener.simulatedPropertyStateChanged(mclnStatement);
        }
//        System.out.println("MclnStatement.fireSimulatedPropertyStateChanged: new current state " + currentState.toString());
    }

    private void callExternalActionRequestProcessor(MclnStatement mclnStatement) {
        for (ExternalActionRequestProcessor externalActionRequestProcessor : externalActionRequestProcessors) {
            String subject = mclnStatement.getSubject();
            String propertyName = mclnStatement.getPropertyName();
            String statementInterpretation = mclnStatement.getStateInterpretation();
            String statementAsText = mclnStatement.getStatementText();
            externalActionRequestProcessor.processActionRequest(mclnStatement.getUID(), subject, propertyName,
                    statementInterpretation);
        }
//        System.out.println("MclnStatement.fireSimulatedPropertyStateChanged: new current state " + currentState.toString());
    }


    //
    //  old simulation
    //


    synchronized void execModel() {

// System.out.println( "execModel");
/*
 if ( execMode == DDLS_EXEC )
 {
//  System.out.println( "DDLS_EXEC");
    execDDLS();
 }
 if ( execMode == DDRS_EXEC )
 {
//  System.out.println( "DDRS_EXEC");
    execDEDS();
 }
 */
        if (execMode == DEDS_EXEC) {
            // System.out.println( "DEDSExec");
            execDEDS();
        }
// mclnMedia.mclnStateHistPanel.add_State();
    }

    private void execDEDS() {
        int inpNum;
        int orgNum;
// if ( runningActivated )
        {
//  orgNum = eventQueue.getSize();
//  inpNum = calcDEDSInput();
        }


// mclnDisplayPanel.redrawAllNodeNames();
        //execDEDSOneStep( orgNum, inpNum );
        execDEDSOneStep();
//        mclnDisplayPanel.refreshInfoPanel();
//  execStep++;

// repaint();
// System.out.println(" exec Done "+execStep);
    }


    private synchronized void execDEDSOneStep() {
        int num;
        int oldNum, takenNum, addedNum, newNum;


//        if (eventQueue.isEmpty()) {
////  System.out.println("runDEDSOneStep:  queue is empty");
//            return;
//        }
//        putInQueue(null, RtsModelEvent.INF_DUMMY, 0);
//        oldNum = eventQueue.getSize();
//
//        addedNum = 0;
//        takenNum = 0;
//        boolean eop = false;
//        do {
//            num = eventQueue.getSize();
////  System.out.println("runDEDSOneStep:  queue has " + num );
//            RtsModelEvent modelEvent = extractQueueEvent();
//            if (modelEvent == null)
//                break;
//
//            takenNum++;
//            if (modelEvent.isDivider()) {
////   System.out.println("runDEDSOneStep:  divider" );
//                break;
//            }
//            num = eventQueue.getSize();
////  System.out.println("runDEDSOneStep:  queue has " + num );
//            addedNum += processDEDSEvent(modelEvent);
//
//        } while (!eop);
//
//        //repaint();
//        newNum = eventQueue.getSize();
    }

    //    private synchronized void execDEDSOneStep() {
//        int num;
//        int oldNum, takenNum, addedNum, newNum;
//
//        if (eventQueue.isEmpty()) {
////  System.out.println("runDEDSOneStep:  queue is empty");
//            return;
//        }
//
//        putInQueue(null, RtsModelEvent.INF_DUMMY, 0);
//        oldNum = eventQueue.getSize();
//// System.out.println(" ssssssssssssssssssssssss " + oldNum);
//// System.out.println("\n");
//// eventQueue.printQueue();
//// rtsMedia.waitKbdHit();
///*
// if ( (num = eventQueue.getSize()) != 0 )
// {
//  System.out.println("runDEDSOneStep:  queue has " + num );
//  RtsModelEvent modelEvent =
//     (RtsModelEvent)eventQueue.extractFirstElement();
//  num = eventQueue.getSize();
//  System.out.println("runDEDSOneStep:  queue has " + num );
//  processDEDSEvent( modelEvent );
//  repaint();
// }
//*/
//
//        addedNum = 0;
//        takenNum = 0;
//        boolean eop = false;
//        do {
//            num = eventQueue.getSize();
////  System.out.println("runDEDSOneStep:  queue has " + num );
//            RtsModelEvent modelEvent = extractQueueEvent();
//            if (modelEvent == null)
//                break;
//
//            takenNum++;
//            if (modelEvent.isDivider()) {
////   System.out.println("runDEDSOneStep:  divider" );
//                break;
//            }
//            num = eventQueue.getSize();
////  System.out.println("runDEDSOneStep:  queue has " + num );
//            addedNum += processDEDSEvent(modelEvent);
//
//        } while (!eop);
//
//        //repaint();
//        newNum = eventQueue.getSize();
//// eventQueue.printQueue();
////System.out.println("runDEDSOneStep: ==================  " +
////"orgNum "+orgNum+" + inpNum "+inpNum+" 1 = "
////+(orgNum + inpNum + 1)+ "  "
////+oldNum+"  - " + takenNum + " + "
//// + addedNum + "   "+newNum );
////rtsMedia.waitKbdHit();
///*
// while ( (num = eventQueue.getSize()) != 0 )
// {
//  System.out.println("runDEDSOneStep:  queue has " + num );
//  RtsModelEvent modelEvent =
//     (RtsModelEvent)eventQueue.extractFirstElement();
//  num = eventQueue.getSize();
//  System.out.println("runDEDSOneStep:  queue has " + num );
//  processDEDSEvent( modelEvent );
//  repaint();
// }
//*/
//// repaint();
//// ((RtsMVLView)mclnMedia.curView).menuPaint();
//    }
    private Set<MclnCondition> processedTrans = new HashSet<>();


//    private int calcOneTranState(RtsMVNObject curTran) {
//        RtsMCN_Arc curArc;
//        RtsMVNObject curNode;
//        int curNodeMCLState, curArcMCLState, inpMCLState, newMCLState,
//                threshold, dis, old;
//
//        /* loop throw all input Arcs */
//        int numOfTranArcs = curTran.inpArcList.size();
//        newMCLState = RtsLAlgebra.getNOTHING();
//// RtsLAlgebra.int32toBinaryStr( -1 );
//        if (curTran.debugPrint) {
////   System.out.println( "calcOneTranState " + curTran.name);
////   System.out.println( "newState = " + newState);
//        }
////  System.out.println( "calcOneTranState " + curTran.name+"  "+numOfTranArcs);
//        for (int i = 0; i < numOfTranArcs; i++) {
//            old = newMCLState;
//            curArc =
//                    (RtsMCN_Arc) curTran.inpArcList.elementAt(i);
//
//            curNode = curArc.inpNode;
//            // System.out.println( "calcOneTranState " + curNode.name+"  "+numOfTranArcs );
//            curNodeMCLState = RtsLAlgebra.rgbColorToMclValue(curNode.curState);
//            curArcMCLState = RtsLAlgebra.rgbColorToMclValue(curArc.curState);
//            threshold = curArc.threshold;
//            /* discoloring & adding 1*/
//            inpMCLState = RtsLAlgebra.mclDiscolorizing(curArcMCLState, curNodeMCLState);
//            if (curNode.getSubject().equals("C1")) {
////   System.out.println(RtsLAlgebra.int32toBinaryStr( curNodeMCLState ));
////stem.out.println(RtsLAlgebra.int32toBinaryStr( curArc.curState ));
////   System.out.println(RtsLAlgebra.int32toBinaryStr( curArcMCLState ));
////     System.out.println(RtsLAlgebra.int32toBinaryStr( inpMCLState ));
//            }
//            dis = inpMCLState;
////  inpMCLState = RtsLAlgebra.mcAdd1( threshold, inpMCLState );
//            newMCLState = RtsLAlgebra.mclAdd1(newMCLState, inpMCLState);
//
//            if (curTran.debugPrint) {
////   System.out.println( curNode.name + ": " + newState + " = " +
////   old + " + " + threshold + " + " + dis + "(" +
////   curArcState + "  " + curNodeState + ") " );
//            }
////    messageDlg( 'M', "calcOneTranState " + curTran.name + "  "+i,
//// "" + curArcState + "  " + curNodeState +
//// " " + inpState + " " + newState + " " );
//
//        }
//
//        curTran.setRunState(RtsLAlgebra.mclValueToRgbColor(newMCLState));
//        return newMCLState;
//    }


}
