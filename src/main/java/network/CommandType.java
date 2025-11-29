package network;

/**
 * Represents the different types of commands used in the client-server protocol.
 * This enum is not a POJO, but rather a part of the network protocol.
 */
public enum CommandType {
    /**
     * Command to send symptoms data to the server
     */
    SEND_SYMPTOMS,
    /**
     * Command to add additional information to existing record
     */
    ADD_EXTRA_INFO,
    /**
     * Command to retrieve patient history
     */
    GET_HISTORY,
    /**
     * Command to retrieve signal data
     */
    GET_SIGNALS,
    /**
     * Command to send ECG signal data
     */
    SEND_ECG,
    /**
     * Command to send EMG signal data
     */
    SEND_EMG,
    /**
     * Command to disconnect from the server
     */
    DISCONNECT,
    /**
     * Represents an unknown or invalid command
     */
    UNKNOWN;

    /**
     * Converts a string representation to its corresponding CommandType enum value.
     *
     * @param s The string to convert
     * @return The matching CommandType, or UNKNOWN if no match is found
     */
    public static CommandType fromString(String s) {
        try {
            return CommandType.valueOf(s);
        } catch (Exception e) {
            return UNKNOWN;
        }
    }
}

