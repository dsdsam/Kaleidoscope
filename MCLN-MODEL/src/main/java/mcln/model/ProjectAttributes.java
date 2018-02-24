package mcln.model;

public class ProjectAttributes {

    private final String projectName;
    private final MclnDoubleRectangle mclnDoubleRectangle;

    public ProjectAttributes(String projectName, double x, double y, double width, double height) {
        this(projectName, new MclnDoubleRectangle(x, y, width, height));
    }

    public ProjectAttributes(String projectName, MclnDoubleRectangle mclnDoubleRectangle) {
        assert projectName != null : "Argument \"projectName\" is null";
        assert mclnDoubleRectangle != null : "Argument \"mclnDoubleRectangle\" is null";
        this.projectName = projectName.trim();
        this.mclnDoubleRectangle = mclnDoubleRectangle;
    }

    public ProjectAttributes getCopy(){
        return new ProjectAttributes(projectName, getMclnDoubleRectangle());
    }

    @Override
    public boolean equals(Object other){
        if(this == other){
            return true;
        }

        if(!(this instanceof ProjectAttributes)){
            return false;
        }

        ProjectAttributes otherProjectAttributes = (ProjectAttributes)other;

        if(!projectName.equals(otherProjectAttributes.getProjectName())){
            return false;
        }
        if(!mclnDoubleRectangle.equals(otherProjectAttributes.getMclnDoubleRectangle())){
            return false;
        }
        return true;
    }

    public final String getProjectName() {
        return projectName;
    }

    public MclnDoubleRectangle getMclnDoubleRectangle() {
        return new MclnDoubleRectangle(mclnDoubleRectangle);
    }

    public final double getX() {
        return mclnDoubleRectangle.getX();
    }

    public final double getY() {
        return mclnDoubleRectangle.getY();
    }

    public final double getWidth() {
        return mclnDoubleRectangle.getWidth();
    }

    public final double getHeight() {
        return mclnDoubleRectangle.getHeight();
    }

    public final boolean isProjectNameValid() {
        return projectName != null && projectName.length() > 0;
    }

    public final boolean isProjectSpaceValid() {
        if(mclnDoubleRectangle.getWidth() < 1 || mclnDoubleRectangle.getHeight() < 1) {
            return false;
        }

//        if(mclnDoubleRectangle.getX() >= mclnDoubleRectangle.getRightX()){
//            return false;
//        }
//        if(mclnDoubleRectangle.getY() <= mclnDoubleRectangle.getLowerY()){
//            return false;
//        }
        return true;
    }
}
