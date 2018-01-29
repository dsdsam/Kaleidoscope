package mcln.model;

/**
 * Created by Admin on 11/18/2017.
 */
public interface ExternalActionRequestProcessor {

    public void processActionRequest(String effectorID, String subject, String propertyName, String stateInterpretation);

}
