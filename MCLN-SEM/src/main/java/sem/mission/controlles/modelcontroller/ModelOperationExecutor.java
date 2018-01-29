package sem.mission.controlles.modelcontroller;

import sem.mission.controlles.modelcontroller.interfaces.ModelSensor;
import sem.mission.explorer.model.SpaceExplorerModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: VL
 * Date: Nov 27, 2011
 * Time: 6:38:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class ModelOperationExecutor {

    static ModelRotationController modelRotationController;
    static ModelMotionController modelMotionController;
    static ModelTransmissionController modelTransmissionController;

    private final List<ModelSensor> sensors;
    private final List<BasicOperationExecutionController> basicOperationExecutionControllers = new ArrayList();

    private final ModelController modelController;
    long timeBefore;
    long timeAfter;


    final javax.swing.Timer animationTickTimer = new javax.swing.Timer(40, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (!modelController.engineStarted || modelController.pause) {
                return;
            }
            timeBefore = System.currentTimeMillis();
            execStep();
            simulateSensors();
            timeAfter = System.currentTimeMillis();
        }
    });

    /**
     * @param modelController
     * @param spaceExplorerModel
     */
    ModelOperationExecutor(ModelController modelController, SpaceExplorerModel spaceExplorerModel) {
        this.modelController = modelController;

        modelMotionController = new ModelMotionController(spaceExplorerModel);
        basicOperationExecutionControllers.add(modelMotionController);

        modelRotationController = new ModelRotationController(spaceExplorerModel);
        basicOperationExecutionControllers.add(modelRotationController);

        modelTransmissionController = new ModelTransmissionController(spaceExplorerModel);
        basicOperationExecutionControllers.add(modelTransmissionController);

        sensors = spaceExplorerModel.getAllSensors();

        animationTickTimer.start();
    }

    private final void execStep() {
        modelMotionController.executeStep();
        modelRotationController.executeStep();
        modelTransmissionController.executeStep();
    }

    private void simulateSensors() {
        if (!modelMotionController.isOperationBeingExecuted()) {
            return;
        }
        for (ModelSensor modelSensor : sensors) {
            modelSensor.simulate();
        }
    }

    /**
     * Called when either Power or Manual Mode or Reset Modeling button pressed
     */
    public void stopOperationOnMissionStopped() {
        for (BasicOperationExecutionController basicOperationExecutionController : basicOperationExecutionControllers) {
            basicOperationExecutionController.stopOperationOnMissionStoped();
        }
    }

    /**
     * Called when Stop Button pressed or Behavior controlles changes model behavior
     */
    public void cancelCurrentOperation() {
        for (BasicOperationExecutionController basicOperationExecutionController : basicOperationExecutionControllers) {
            basicOperationExecutionController.cancelCurrentOperation();
        }
    }

}
