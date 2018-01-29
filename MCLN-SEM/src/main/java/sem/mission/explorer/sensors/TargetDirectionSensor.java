package sem.mission.explorer.sensors;

import sem.mission.explorer.model.SemBody;
import sem.mission.explorer.model.SpaceExplorerModel;
import sem.mission.controlles.modelcontroller.interfaces.ModelSensor;

/**
 * Created by IntelliJ IDEA.
 * User: VL
 * Date: Oct 10, 2011
 * Time: 5:30:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class TargetDirectionSensor implements ModelSensor {

    private SpaceExplorerModel spaceExplorerModel;
    private SemBody seBody;
    private double targetDirection;


    public TargetDirectionSensor(SpaceExplorerModel spaceExplorerModel, SemBody seBody) {
        this.spaceExplorerModel = spaceExplorerModel;
        this.seBody = seBody;
    }

    public void setTargetDirection(double targetDirection) {
        this.targetDirection = targetDirection;
    }

    public void simulate() {
//        double direction = spaceExplorerModel.getDirection();
//        System.out.println("TargetDirectionSensor: targetDirection = " + targetDirection);
//        System.out.println("TargetDirectionSensor:       Direction = " + direction);
    }
}
