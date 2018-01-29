package mcln.model;

/**
 * Created by Admin on 12/25/2015.
 */
public enum ArrowTipLocationPolicy {
    DETERMINED_BY_USER("Arrow tip determined by user"),
    DETERMINED_BY_KNOB_LOCATION_PLUS_ARROW_TIP_OFFSET("Arrow tip determined by knot offset"),
    DETERMINED_BY_KNOB_LOCATION_PLUS_AUTO_DETECTION("Arrow tip determined by auto detection"),
    DEFAULT_ARROW_TIP_LOCATION("Default location");

    //
    //   I n s t a n c e
    //

    private final String policy;

    private ArrowTipLocationPolicy(String policy){
        this.policy = policy;
    }

    @Override
    public String toString(){
        return policy;
    }
}
