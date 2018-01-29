/*
 * Created on Aug 20, 2005
 *
 */
package adf.csys.model;

/**
 * @author xpadmin
 *
 */
public interface ModelChangeListener extends java.util.EventListener{
    public void modelChanged( double currentAngle, double[][] mat43 );
    public void modelRedefined(BasicModelEntity  basicModelEntity);
}
