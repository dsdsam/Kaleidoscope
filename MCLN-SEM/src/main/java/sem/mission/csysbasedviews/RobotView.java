/*
 * Created on Sep 11, 2005
 *
 */
package sem.mission.csysbasedviews;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;

import adf.csys.model.ModelPolyLineEntity;
import adf.csys.view.BasicCSysEntity;
import adf.csys.view.CSysEntity;
import adf.csys.view.CSysLineEntity;
import adf.csys.view.CSysView;
import sem.mission.explorer.model.SpaceExplorerModel;
import sem.infrastructure.evdistributor.SemEventDistributor;
import sem.infrastructure.evdistributor.EventDistributionAdapter;
import sem.mission.controlles.modelcontroller.actions.CallbackListener;
import adf.csys.model.BasicModelEntity;
import adf.csys.model.ModelLineEntity;
import vw.valgebra.VAlgebra;

import javax.swing.*;

/**
 * @author xpadmin
 */
public class RobotView extends SeBasicCSysView {

    private Color lineColor = new Color(0x003300);
    private Color borderColor = new Color(0x006600);

    private boolean powerOn;

    /**
     *
     */
    private EventDistributionAdapter modelGroupEventDistributionListener = new EventDistributionAdapter<Boolean,
            CallbackListener>() {

        @Override
        public void onGuiStateModelEventFired(SemEventDistributor.EventId eventId, Component comp, Boolean poverOn,
                                              CallbackListener callbackListener) {
            if (eventId == SemEventDistributor.EventId.POWER) {
                if (RobotView.this.powerOn == poverOn) {
                    return;
                }
                RobotView.this.powerOn = poverOn;
                repaint();
            }
        }
    };

    /**
     * @param cSysX
     * @param cSysY
     * @param cSysWidth
     * @param cSysHeight
     * @param options
     */
    public RobotView(double cSysX, double cSysY, double cSysWidth,
                     double cSysHeight, int viewPadding, int options) {
        super(cSysX, cSysY, cSysWidth, cSysHeight, viewPadding, 0);
        setProjection(CSysView.ZOYProjection);
        setName("RobotView");
        SemEventDistributor.addEventDistributionListener(SemEventDistributor.EventGroup.MISSION_EVENT,
                modelGroupEventDistributionListener);
    }

    private CSysEntity[] viewWorldEntityArray;

    /**
     *
     */
    @Override
    protected void buildWorldRepresentation() {

        Color modelBorderColor = new Color(0x666666);
        List<BasicModelEntity> modelWorldEntityList = spaceExplorerModel.getWorldEntityList();
        List<CSysEntity> viewWorldEntityList = new ArrayList<CSysEntity>();

        Color currentColor;
        for (int i = 0; i < modelWorldEntityList.size(); i++) {

            BasicModelEntity basicModelEntity = modelWorldEntityList.get(i);

            if (!basicModelEntity.getColor().equals(modelBorderColor)) {
                currentColor = lineColor;
            } else {
                currentColor = borderColor;
            }

            CSysEntity cSysEntity = null;
            if (basicModelEntity instanceof ModelLineEntity) {
                cSysEntity = new SeCSysLineEntity(this, (ModelLineEntity) basicModelEntity);
                cSysEntity.setDrawColor(currentColor);
            } else if (basicModelEntity instanceof ModelPolyLineEntity) {
                cSysEntity = new SeCSysViewPolyLineEntity(this, (ModelPolyLineEntity) basicModelEntity);
                cSysEntity.setDrawColor(currentColor);
            }
            viewWorldEntityList.add(cSysEntity);
        }

        createAxis(viewWorldEntityList);

        viewWorldEntityArray = viewWorldEntityList.toArray(new CSysEntity[viewWorldEntityList.size()]);
    }

    // A X I S
    protected void createAxis(java.util.List<CSysEntity> viewWorldEntityList) {
        BasicCSysEntity basicCSysEntity;


        basicCSysEntity = new CSysLineEntity(this, new double[]{0, 0, 0}, new double[]{0.2, 0, 0});
        basicCSysEntity.setDrawColor(new Color(0xFFFF00));
        viewWorldEntityList.add(basicCSysEntity);

        basicCSysEntity = new CSysLineEntity(this, new double[]{0, 0, 0}, new double[]{-0.2, 0, 0});
        basicCSysEntity.setDrawColor(new Color(0xFFFF00));
        viewWorldEntityList.add(basicCSysEntity);

        basicCSysEntity = new CSysLineEntity(this, new double[]{0, 0, 0}, new double[]{0, -0.2, 0});
        basicCSysEntity.setDrawColor(new Color(0x00FFFF));
        viewWorldEntityList.add(basicCSysEntity);


        basicCSysEntity = new CSysLineEntity(this, new double[]{0, 0, 0}, new double[]{0, 0.2, 0});
        basicCSysEntity.setDrawColor(new Color(0x00FFFF));
        viewWorldEntityList.add(basicCSysEntity);


        basicCSysEntity = new CSysLineEntity(this, new double[]{0, 0, 0}, new double[]{0, 0, 0.7});
        basicCSysEntity.setDrawColor(new Color(0xFF00FF));
        viewWorldEntityList.add(basicCSysEntity);

//          basicCSysEntity = new CSysLineEntity(this, new double[]{0, 0, 0}, new double[]{0, 0, -5});
//          basicCSysEntity.setDrawColor(new Color(0x770077));
//          viewWorldEntityList.add(basicCSysEntity);
    }


    /**
     *
     */
    public void buildRepresentation(SpaceExplorerModel spaceExplorerModel) {
        super.buildRepresentation(spaceExplorerModel);
        VAlgebra.initYawYRotMat43(yRotatingMatrix, 0, null);
//        VAlgebra.initYawYRotMat43(zRotatingMatrix, 0, null);
        VAlgebra.initZRotMat43(zRotatingMatrix, 0, null);
        doYRotation(0.4);
        doZRotation(180);

    }

    /**
     *
     */

    private final double[][] yRotationModelMatrix = new double[4][3];

    @Override
    public void doYRotation(double angle) {
        VAlgebra.initYawYRotMat43(yRotatingMatrix, angle, null);
        VAlgebra.trf43Xtrf43(combinedRotatingMatrix, yRotatingMatrix, zRotatingMatrix);
        updateCSysEntList(combinedRotatingMatrix);
        VAlgebra.initZRotMat43(yRotationModelMatrix, 0, null);
        updateCSysEntListForViewPoint(0, yRotationModelMatrix);
    }


    private final double[][] zRotationModelMatrix = new double[4][3];

    @Override
    public void doZRotation(double angle) {
        VAlgebra.initZRotMat43(zRotatingMatrix, angle, null);
        VAlgebra.trf43Xtrf43(combinedRotatingMatrix, yRotatingMatrix, zRotatingMatrix);
        updateCSysEntList(combinedRotatingMatrix);
        VAlgebra.initZRotMat43(zRotationModelMatrix, 0, null);
        updateCSysEntListForViewPoint(0, zRotationModelMatrix);
    }

    //  T R A N S F O R M A T I O N S

    public void updateCSysEntList(double[][] mat43) {
        updateCSysEntListForViewPoint(currentAngle, mat43);
    }

    private final double[] trVec = new double[3];
    private final double[][] mat = new double[4][3];
    private final double[][] applMat = new double[4][3];
    private double currentAngle = 0;

    private void updateCSysEntListForViewPoint(double currentAngle, double[][] mat43) {
        this.currentAngle = currentAngle;
        VAlgebra.initVec3(trVec, mat43[3][0] * -1, mat43[3][1] * -1, mat43[3][2] * -1);
        VAlgebra.initMat43(mat, 0, trVec);
//        System.out.println("!!!^^^^^^^^^^^^^^^^^^^^^^^^^ "+currentAngle);
//        VAlgebra.printMat43(mat);
        VAlgebra.initMat43(applMat, -currentAngle, null);
//         VAlgebra.printMat43(applMat);
//        applMat = VAlgebra.initMat43( new double[4][3], currentAngle, trVec );
        VAlgebra.trf43Xtrf43(applMat, applMat, mat);
//        VAlgebra.trf43Xtrf43( applMat, combinedRotatingMatrix, applMat );

        for (int i = 0; i < viewWorldEntityArray.length; i++) {
            BasicCSysEntity basicCSysEntity = (BasicCSysEntity) viewWorldEntityArray[i];
//            obj.initTransformation();
            basicCSysEntity.doTransformation(applMat);
            //      obj.doXClipping( 0 );
            basicCSysEntity.doNextTransformation(combinedRotatingMatrix);
            basicCSysEntity.doProspectiveDistortion();
            basicCSysEntity.doCSysToScreenTransformation(scr0, minScale);
//            obj.doTransformation( combinedRotatingMatrix ); 
        }
        /*
        numEntities = modelEntityList.size();
        for( int i=0; i < numEntities; i++ ){
            CSysEntity obj = (CSysEntity) modelEntityList.get(i);
            obj.initTransformation();
            obj.doTransformation( applMat ); 
            obj.doXClipping( 0 );
            obj.doTransformation( combinedRotatingMatrix ); 
            obj.doPrespectiveDistortion();
//            obj.doTransformation( combinedRotatingMatrix ); 
        }
 */
//        Rectangle rect = getBounds();
//        this.paintImmediately(rect);
//        repaint();
//        Thread.dumpStack();
    }

    // Implementation of ModelChangeListener inteface
    public void modelChanged(final double currentAngle, final double[][] mat43) {
        Runnable runnable = new Runnable() {
            public void run() {
                updateCSysEntListForViewPoint(currentAngle, mat43);
                Rectangle rect = getBounds();
                rect.x = 0;
                rect.y = 0;
//         System.out.println("MainMonitorCSysView.updateCSysEntList:  x " + rect.x + " y " + rect.y + " w " + rect.width + "  h" + rect.height);
//                RobotView.this.paintImmediately(rect);
                repaint();
            }
        };

//        System.out.println("isEventDispatchThread "+SwingUtilities.isEventDispatchThread());
        if (SwingUtilities.isEventDispatchThread()) {
            runnable.run();
        } else {
            SwingUtilities.invokeLater(runnable);
        }

//      System.out.println("MainMonitorCSysView: ^^^^^^^^^^^^^^^^^^^^^^^^^ "+currentAngle);
//      updateCSysEntListForViewPoint( currentAngle, mat43 );
    }

    @Override
    protected void paintWorld(Graphics g) {
        for (int i = 0; i < viewWorldEntityArray.length; i++) {
            CSysEntity cSysEntity = viewWorldEntityArray[i];
            g.setColor(cSysEntity.getDrawColor());
//            cSysEntity.draw(g, scr0, minScale);
            cSysEntity.draw(g);
        }
    }

    public void paint(Graphics g) {

//      Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
        Rectangle rect = g.getClipBounds();
        g.setColor(Color.BLACK);
        g.fillRect(rect.x, rect.y, rect.width, rect.height);
//      minScale = minScale * 0.6;
//      super.paint( g );        

        if (powerOn) {
            paintWorld(g);
        }

        int ox = rect.width / 2;
        int oy = rect.height / 2;
        int rectHalfWidth = 15;
        int rectHalfHeight = 15;
        g.setColor(Color.DARK_GRAY);

        g.drawLine(rect.x, oy, ox - rectHalfWidth, oy);
        g.drawLine(ox + rectHalfWidth, oy, rect.width, oy);
        g.drawLine(ox, rect.y, ox, oy - rectHalfHeight);
        g.drawLine(ox, oy + rectHalfHeight, ox, rect.height);
        g.drawRect(ox - rectHalfWidth, oy - rectHalfHeight, rectHalfWidth << 1, rectHalfHeight << 1);

        int halfDist = 5;
        g.drawLine(ox - halfDist, oy, ox + halfDist, oy);
        g.drawLine(ox, oy - halfDist, ox, oy + halfDist);
//      g.setColor( Color.RED );
//      g.drawRect( rect.x+1, rect.y+1, rect.width-2, rect.height-2 );
        int yStep = 10;
        int rxPos = ox + yStep;
        int lxPos = ox - yStep;
        int cnt = 0;
        while (rxPos <= rect.width) {
            if ((cnt % 2) == 0) {
                g.drawLine(lxPos, oy - 5, lxPos, oy + 5);
            } else {
                g.drawLine(lxPos, oy - 3, lxPos, oy + 3);
            }

            if ((cnt % 2) == 0) {
                g.drawLine(rxPos, oy - 5, rxPos, oy + 5);
            } else {
                g.drawLine(rxPos, oy - 3, rxPos, oy + 3);
            }
            lxPos -= yStep;
            rxPos += yStep;
            cnt++;
        }

        int uyPos = oy - yStep;
        int lyPos = oy + yStep;
        cnt = 0;
        while (lyPos <= rect.height) {
            if ((cnt % 2) == 0) {
                g.drawLine(ox - 5, lyPos, ox + 5, lyPos);
            } else {
                g.drawLine(ox - 3, lyPos, ox + 3, lyPos);
            }

            if ((cnt % 2) == 0) {
                g.drawLine(ox - 5, uyPos, ox + 5, uyPos);
            } else {
                g.drawLine(ox - 3, uyPos, ox + 3, uyPos);
            }
            uyPos -= yStep;
            lyPos += yStep;
            cnt++;
        }

//      LEDSign.LEDPanelpaint(g);
    }

    /*
      public void paintTicks(Graphics g)  {
          Rectangle tickBounds = tickRect;
          int i;
          int maj, min, max;
          int w = tickBounds.width;
          int h = tickBounds.height;
          int centerEffect, tickHeight;

          g.setColor(slider.getBackground());
          g.fillRect(tickBounds.x, tickBounds.y, tickBounds.width, tickBounds.height);
          g.setColor(Color.black);

          maj = slider.getMajorTickSpacing();
          min = slider.getMinorTickSpacing();

          if ( slider.getOrientation() == JSlider.HORIZONTAL ) {
             g.translate( 0, tickBounds.y);

              int value = slider.getMinimum();
              int xPos = 0;

              if ( slider.getMinorTickSpacing() > 0 ) {
                  while ( value <= slider.getMaximum() ) {
                      xPos = xPositionForValue( value );
                      paintMinorTickForHorizSlider( g, tickBounds, xPos );
                      value += slider.getMinorTickSpacing();
                  }
              }

              if ( slider.getMajorTickSpacing() > 0 ) {
                  value = slider.getMinimum();

                  while ( value <= slider.getMaximum() ) {
                      xPos = xPositionForValue( value );
                      paintMajorTickForHorizSlider( g, tickBounds, xPos );
                      value += slider.getMajorTickSpacing();
                  }
              }

              g.translate( 0, -tickBounds.y);
          }
          else {
             g.translate(tickBounds.x, 0);

              int value = slider.getMinimum();
              int yPos = 0;

              if ( slider.getMinorTickSpacing() > 0 ) {
                int offset = 0;
                if(!BasicGraphicsUtils.isLeftToRight(slider)) {
                offset = tickBounds.width - tickBounds.width / 2;
                g.translate(offset, 0);
            }

                  while ( value <= slider.getMaximum() ) {
                      yPos = yPositionForValue( value );
                      paintMinorTickForVertSlider( g, tickBounds, yPos );
                      value += slider.getMinorTickSpacing();
                  }

            if(!BasicGraphicsUtils.isLeftToRight(slider)) {
                g.translate(-offset, 0);
            }
              }

              if ( slider.getMajorTickSpacing() > 0 ) {
                  value = slider.getMinimum();
                if(!BasicGraphicsUtils.isLeftToRight(slider)) {
                g.translate(2, 0);
            }

                  while ( value <= slider.getMaximum() ) {
                      yPos = yPositionForValue( value );
                      paintMajorTickForVertSlider( g, tickBounds, yPos );
                      value += slider.getMajorTickSpacing();
                  }

                if(!BasicGraphicsUtils.isLeftToRight(slider)) {
                g.translate(-2, 0);
            }
              }
              g.translate(-tickBounds.x, 0);
          }
      }

      protected void paintMinorTickForHorizSlider( Graphics g, Rectangle tickBounds, int x ) {
          g.drawLine( x, 0, x, tickBounds.height / 2 - 1 );
      }

      protected void paintMajorTickForHorizSlider( Graphics g, Rectangle tickBounds, int x ) {
          g.drawLine( x, 0, x, tickBounds.height - 2 );
      }

      protected void paintMinorTickForVertSlider( Graphics g, Rectangle tickBounds, int y ) {
          g.drawLine( 0, y, tickBounds.width / 2 - 1, y );
      }

      protected void paintMajorTickForVertSlider( Graphics g, Rectangle tickBounds, int y ) {
          g.drawLine( 0, y,  tickBounds.width - 2, y );
      }

      public void paintLabels( Graphics g ) {
          Rectangle labelBounds = labelRect;

          Dictionary dictionary = slider.getLabelTable();
          if ( dictionary != null ) {
              Enumeration keys = dictionary.keys();
              int minValue = slider.getMinimum();
              int maxValue = slider.getMaximum();
              while ( keys.hasMoreElements() ) {
                  Integer key = (Integer)keys.nextElement();
                  int value = key.intValue();
                  if (value >= minValue && value <= maxValue) {
                      Component label = (Component)dictionary.get( key );
                      if ( slider.getOrientation() == JSlider.HORIZONTAL ) {
                          g.translate( 0, labelBounds.y );
                          paintHorizontalLabel( g, value, label );
                          g.translate( 0, -labelBounds.y );
                      }
                      else {
                          int offset = 0;
                          if (!BasicGraphicsUtils.isLeftToRight(slider)) {
                              offset = labelBounds.width -
                                  label.getPreferredSize().width;
                          }
                          g.translate( labelBounds.x + offset, 0 );
                          paintVerticalLabel( g, value, label );
                          g.translate( -labelBounds.x - offset, 0 );
                      }
                  }
              }
          }

      }
    */
    /**
     * Called for every label in the label table.  Used to draw the labels for horizontal sliders.
     * The graphics have been translated to labelRect.y already.
     * @see JSlider#setLabelTable
     */
    /*
      protected void paintHorizontalLabel( Graphics g, int value, Component label ) {
          int labelCenter = xPositionForValue( value );
          int labelLeft = labelCenter - (label.getPreferredSize().width / 2);
          g.translate( labelLeft, 0 );
          label.paint( g );
          g.translate( -labelLeft, 0 );
      }
    */
    /**
     * Called for every label in the label table.  Used to draw the labels for vertical sliders.
     * The graphics have been translated to labelRect.x already.
     * @see JSlider#setLabelTable
     */
    /*
      protected void paintVerticalLabel( Graphics g, int value, Component label ) {
          int labelCenter = yPositionForValue( value );
          int labelTop = labelCenter - (label.getPreferredSize().height / 2);
          g.translate( 0, labelTop );
          label.paint( g );
          g.translate( 0, -labelTop );
      }
    */
}
