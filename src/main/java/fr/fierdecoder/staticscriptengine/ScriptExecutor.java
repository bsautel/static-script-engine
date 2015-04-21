package fr.fierdecoder.staticscriptengine;

import javax.script.Invocable;
import javax.script.ScriptException;
import java.util.List;
import java.util.Map;

public class ScriptExecutor {
    private final Invocable invocable;
    private final ScriptEngineResultConverter scriptEngineResultConverter;

    public ScriptExecutor(Invocable invocable) {
        this.invocable = invocable;
        scriptEngineResultConverter = new ScriptEngineResultConverter();
    }

    public boolean returnBoolean() throws ScriptExecutorException {
        try {
            Object result = invocable.invokeFunction("returnBoolean");
            return scriptEngineResultConverter.convertToType(result, Boolean.class);
        } catch (ScriptException | NoSuchMethodException e) {
            throw new ScriptExecutorException(e);
        }
    }

    public String returnString() throws ScriptExecutorException {
        try {
            Object result = invocable.invokeFunction("returnString");
            return scriptEngineResultConverter.convertToType(result, String.class);
        } catch (ScriptException | NoSuchMethodException e) {
            throw new ScriptExecutorException(e);
        }
    }

    public String returnBooleanAsString() throws ScriptExecutorException {
        try {
            Object result = invocable.invokeFunction("returnBoolean");
            return scriptEngineResultConverter.convertToType(result, String.class);
        } catch (ScriptException | NoSuchMethodException e) {
            throw new ScriptExecutorException(e);
        }
    }

    public String returnVoidAsString() {
        try {
            Object result = invocable.invokeFunction("returnVoid");
            return scriptEngineResultConverter.convertToType(result, String.class);
        } catch (ScriptException | NoSuchMethodException e) {
            throw new ScriptExecutorException(e);
        }
    }

    public List<String> returnStringArray() throws ScriptExecutorException {
        try {
            Object result = invocable.invokeFunction("returnStringArray");
            return scriptEngineResultConverter.convertList(result, String.class);
        } catch (ScriptException | NoSuchMethodException e) {
            throw new ScriptExecutorException(e);
        }
    }

    public List<Integer> returnIntegerArray() throws ScriptExecutorException {
        try {
            Object result = invocable.invokeFunction("returnIntegerArray");
            return scriptEngineResultConverter.convertList(result, Integer.class);
        } catch (ScriptException | NoSuchMethodException e) {
            throw new ScriptExecutorException(e);
        }
    }

    public Map<String, String> returnStringStringMap() throws ScriptExecutorException {
        try {
            Object result = invocable.invokeFunction("returnStringStringMap");
            return scriptEngineResultConverter.convertMap(result, String.class, String.class);
        } catch (ScriptException | NoSuchMethodException e) {
            throw new ScriptExecutorException(e);
        }
    }

    public Map<String, Boolean> returnStringBooleanMap() throws ScriptExecutorException {
        try {
            Object result = invocable.invokeFunction("returnStringBooleanMap");
            return scriptEngineResultConverter.convertMap(result, String.class, Boolean.class);
        } catch (ScriptException | NoSuchMethodException e) {
            throw new ScriptExecutorException(e);
        }
    }
}
