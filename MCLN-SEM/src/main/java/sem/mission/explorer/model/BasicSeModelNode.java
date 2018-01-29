/**
 * 
 */
package sem.mission.explorer.model;

import sem.mission.controlles.modelcontroller.ModelController;
import sem.mission.controlles.modelcontroller.SeModelEvent;
import adf.csys.model.Part;

/**
 * @author Administrator
 */
public class BasicSeModelNode extends Part implements SeModelNode {

    private ModelController modelController;

    public BasicSeModelNode(String name, String nodeId) {
        super(name, nodeId);
    }

    @Override
    public void placeSeModelEvent(SeModelEvent seModelEvent) {
        if (modelController == null) {
            modelController = ModelController.getInstance();
        }
        modelController.enqueueSeModelEvent(seModelEvent);
    }

}
