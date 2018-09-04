package mcln.model;

/**
 * Created by Admin on 2/9/2016.
 */
abstract public class MclnEntity {

    private final String UID;
    private final long uidNumber;
    private boolean runtimeInitializationUpdatedFlag;

    MclnEntity(String uid) {
        this.UID = uid;
        String strUIDNumber = UID.substring(UID.indexOf("-") + 1);
        long value = 0;
        try {
            value = Long.parseLong(strUIDNumber);
        } finally {
            uidNumber = value;
        }
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

    public long getUidNumberPart() {
        return uidNumber;
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

    /**
     * This method should be overridden
     *
     * @return
     */
    abstract public String getOneLineInfoMessage();
}
