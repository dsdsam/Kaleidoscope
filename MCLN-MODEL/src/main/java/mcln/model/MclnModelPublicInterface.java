package mcln.model;

import mcln.palette.MclnState;

/**
 * Created with IntelliJ IDEA.
 * User: XP Admin
 * Date: 4/13/14
 * Time: 9:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class MclnModelPublicInterface {

    private MclnSimulatingEngine mclnSimulatingEngine;

    public MclnModelPublicInterface() {
        mclnSimulatingEngine = new MclnSimulatingEngine();
    }

    public ExternalEventListener getMclnModelExternalEventListener() {
        return mclnSimulatingEngine.getExternalEventListener();
    }

    public ExternalActionResponseProcessor getExternalActionResponseProcessor() {
        return mclnSimulatingEngine.getExternalActionResponseProcessor();
    }

    public void addMclnModelExternalActionProcessor(ExternalActionRequestProcessor externalActionRequestProcessor) {
        mclnSimulatingEngine.addMclnModelExternalActionProcessor(externalActionRequestProcessor);
    }

    public void clearSimulation() {
        mclnSimulatingEngine.clearSimulation();
    }

    public void setMclnModel(MclnModel mclnModel) {
        mclnSimulatingEngine.setMclnModel(mclnModel);
    }

    public void initializeSimulation() {
        mclnSimulatingEngine.initializeSimulation();
    }

    public void startSimulation() {
        mclnSimulatingEngine.startSimulation();
    }

    public void stopSimulation() {
        mclnSimulatingEngine.stopSimulation();
    }

    public String resetSimulation() {
        return mclnSimulatingEngine.resetSimulation();
    }

    public String executeOneSimulationStep() {
        return mclnSimulatingEngine.executeOneSimulationStep();
    }

    public void processUserProvidedRuntimePropertyState(String uid, MclnState userProvidedMclnState) {
        mclnSimulatingEngine.processUserProvidedRuntimePropertyState(uid, userProvidedMclnState);
    }
}
