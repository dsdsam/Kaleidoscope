package dsdsse.designspace.initializer;

/**
 * Created by Admin on 4/23/2016.
 */
interface ControllerRequestListener {

    void onControllerRequest(InitAssistantController initAssistantController,
                             InitAssistantDataModel initAssistantDataModel,
                             InitAssistantController.PageNavigationRequest operation,
                             InitAssistantController.InitializationPage pageAttribute);
}
