package adf.flyout;

class FlyingMessage {
    static final int ALERT = 1;
    static final int WARNING = 2;
    static final int INFO = 3;

    static FlyingMessage createInstance(int messageType, String messageText) {
        return new FlyingMessage(messageType, messageText);
    }

    private final int messageType;
    private final String messageText;

    private FlyingMessage(int messageType, String messageText) {
        this.messageType = messageType;
        this.messageText = messageText;
    }

    public int getMessageType() {
        return messageType;
    }

    public boolean isAlertMessage() {
        return messageType == ALERT;
    }

    public boolean isWarningMessage() {
        return messageType == INFO;
    }

    public boolean isInfoMessage() {
        return messageType == INFO;
    }

    public String getMessageText() {
        return messageText;
    }
}
