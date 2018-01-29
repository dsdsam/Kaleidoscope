/*
 * Created on Aug 15, 2005
 *
 */
package adf.csys.model;

import java.util.ArrayList;
import java.util.List;


/**
 * @author xpadmin
 */
public class Part extends BasicModelTreeNode {

    private List<BasicModelEntity> partEntityList = new ArrayList<BasicModelEntity>();
    private BasicModelEntity[] partEntityArray = new BasicModelEntity[0];

    private List<BasicModelEntity> assemblyEntityList = new ArrayList<BasicModelEntity>();
    private BasicModelEntity[] assemblyEntityArray = new BasicModelEntity[0];

//    private List<ModelSensor> sensors = new ArrayList();

    /**
     * @param name
     * @param nodeId
     */
    public Part(String name, String nodeId) {
        super(name, nodeId);
    }

    protected void addPartEntity(BasicModelEntity basicModelEntiry) {
        partEntityList.add(basicModelEntiry);
    }

    protected void addRartEntityList(List<BasicModelEntity> entityList) {
        partEntityList.addAll(entityList);
    }

    protected void addAssemblyEntity(BasicModelEntity basicModelEntiry) {
        assemblyEntityList.add(basicModelEntiry);
    }

    protected void addAssemblyEntityList(List<BasicModelEntity> entityList) {
        assemblyEntityList.addAll(entityList);
    }

//    public BasicModelEntity getModelEntity(int index) {
//        return partEntityList.get(index);
//    }

    /**
     * @return Returns the modelEntityList.
     */
//    public List<BasicModelEntity> getModelEntityList() {
//        return partEntityList;
//    }
    public List<BasicModelEntity> getPartEntityList() {
        return partEntityList;
    }

    public BasicModelEntity[] getPartEntityArray() {
        return partEntityArray;
    }

    public void partEntityListToArray() {
        partEntityArray = partEntityList.toArray(new BasicModelEntity[partEntityList.size()]);
    }

    public List<BasicModelEntity> getAssemblyEntityList() {
        return assemblyEntityList;
    }

    public BasicModelEntity[] getAssemblyEntityArray() {
        return assemblyEntityArray;
    }

    public void assemblyEntityListToArray() {
        assemblyEntityArray = assemblyEntityList.toArray(new BasicModelEntity[assemblyEntityList.size()]);
    }

//  -------------------------------------

}
