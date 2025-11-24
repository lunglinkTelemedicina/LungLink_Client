package network;

//Not a POJO. It is part of the client-server protocol, not a data object
public enum CommandType {
    SEND_SYMPTOMS,
    ADD_EXTRA_INFO,
    GET_HISTORY,
    GET_SIGNALS,
    SEND_ECG,
    SEND_EMG,
    DISCONNECT,
    UNKNOWN;

    //Converts from string to an enum
    public static CommandType fromString(String s) {
        try {
            return CommandType.valueOf(s);
        } catch (Exception e) {
            return UNKNOWN;
        }
    }
}

