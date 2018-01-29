package dsdsse.designspace.initializer;

/**
 * Created by Admin on 5/19/2016.
 */
public interface EndInitializationRequestListener {
    void onISaveOrCancelControllerRequest(InitAssistantDataModel initAssistantDataModel,
                                          InitAssistantController.EndInitializationRequest requestID);
}



