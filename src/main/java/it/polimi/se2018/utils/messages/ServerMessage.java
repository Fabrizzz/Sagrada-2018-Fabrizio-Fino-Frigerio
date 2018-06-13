package it.polimi.se2018.utils.messages;

import it.polimi.se2018.model.ModelView;
import it.polimi.se2018.utils.enums.ErrorType;
import it.polimi.se2018.utils.enums.MessageType;

public class ServerMessage extends Message {

    private MessageType messageType;
    private ErrorType errorType;
    private ModelView modelView;


    public ServerMessage(ErrorType errorType) {
        super(MessageType.ERROR);
        this.errorType = errorType;
    }

    public ServerMessage(MessageType messageType, ModelView modelView) {
        super(messageType);
        this.modelView = modelView;
    }

    public ErrorType getErrorType(){
        return errorType;
    }

    public ModelView getModelView() {
        return modelView;
    }
}
