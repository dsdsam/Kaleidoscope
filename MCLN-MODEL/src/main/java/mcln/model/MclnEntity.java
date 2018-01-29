package mcln.model;

/**
 * Created by Admin on 2/9/2016.
 */
public class MclnEntity {

    private final String UID;
    private boolean runtimeInitializationUpdatedFlag;

    MclnEntity(String uid) {
        this.UID = uid;
    }

    public boolean isRuntimeInitializationUpdatedFlag() {
        return runtimeInitializationUpdatedFlag;
    }

    public void setRuntimeInitializationUpdatedFlag() {
        this.runtimeInitializationUpdatedFlag = true;
    }

    public void resetRuntimeInitializationUpdatedFlag() {
        this.runtimeInitializationUpdatedFlag = false;
    }

    public String getUID() {
        return UID;
    }

    @Override
    public int hashCode() {
        return UID.hashCode();
    }

    @Override
    public boolean equals(Object otherMclnEntity) {
        if (this == otherMclnEntity) {
            return true;
        }
        if (!(otherMclnEntity instanceof MclnEntity)) {
            return false;
        }
        return UID.equals(((MclnEntity) otherMclnEntity).UID);
    }
}
