package fr.fierdecoder.staticscriptengine.typing;

public class ScriptExecutorException extends RuntimeException {
    public ScriptExecutorException(Exception cause) {
        super(cause);
    }

    public ScriptExecutorException(String message) {
        super(message);
    }
}
