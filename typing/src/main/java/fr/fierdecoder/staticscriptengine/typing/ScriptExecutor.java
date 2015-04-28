package fr.fierdecoder.staticscriptengine.typing;

import javax.script.Invocable;
import javax.script.ScriptException;
import java.util.List;
import java.util.Map;

public class ScriptExecutor {
    private final Invocable invocable;
    private final ScriptEngineResultReader scriptEngineResultReader;

    public ScriptExecutor(Invocable invocable) {
        this.invocable = invocable;
        scriptEngineResultReader = new ScriptEngineResultReader();
    }

    public boolean returnBoolean() throws ScriptExecutorException {
        try {
            Object result = invocable.invokeFunction("returnBoolean");
            return scriptEngineResultReader.readToObject(result, Boolean.class);
        } catch (ScriptException | NoSuchMethodException | ScriptEngineResultException e) {
            throw new ScriptExecutorException(e);
        }
    }

    public String returnString() throws ScriptExecutorException {
        try {
            Object result = invocable.invokeFunction("returnString");
            return scriptEngineResultReader.readToObject(result, String.class);
        } catch (ScriptException | NoSuchMethodException | ScriptEngineResultException e) {
            throw new ScriptExecutorException(e);
        }
    }

    public String returnBooleanAsString() throws ScriptExecutorException {
        try {
            Object result = invocable.invokeFunction("returnBoolean");
            return scriptEngineResultReader.readToObject(result, String.class);
        } catch (ScriptException | NoSuchMethodException | ScriptEngineResultException e) {
            throw new ScriptExecutorException(e);
        }
    }

    public String returnVoidAsString() {
        try {
            Object result = invocable.invokeFunction("returnVoid");
            return scriptEngineResultReader.readToObject(result, String.class);
        } catch (ScriptException | NoSuchMethodException | ScriptEngineResultException e) {
            throw new ScriptExecutorException(e);
        }
    }

    public List<String> returnStringArray() throws ScriptExecutorException {
        try {
            Object result = invocable.invokeFunction("returnStringArray");
            return scriptEngineResultReader.readAsList(result, String.class);
        } catch (ScriptException | NoSuchMethodException | ScriptEngineResultException e) {
            throw new ScriptExecutorException(e);
        }
    }

    public List<Integer> returnIntegerArray() throws ScriptExecutorException {
        try {
            Object result = invocable.invokeFunction("returnIntegerArray");
            return scriptEngineResultReader.readAsList(result, Integer.class);
        } catch (ScriptException | NoSuchMethodException | ScriptEngineResultException e) {
            throw new ScriptExecutorException(e);
        }
    }

    public Map<String, String> returnStringStringMap() throws ScriptExecutorException {
        try {
            Object result = invocable.invokeFunction("returnStringStringMap");
            return scriptEngineResultReader.readAsMap(result, String.class, String.class);
        } catch (ScriptException | NoSuchMethodException | ScriptEngineResultException e) {
            throw new ScriptExecutorException(e);
        }
    }

    public Map<String, Boolean> returnStringBooleanMap() throws ScriptExecutorException {
        try {
            Object result = invocable.invokeFunction("returnStringBooleanMap");
            return scriptEngineResultReader.readAsMap(result, String.class, Boolean.class);
        } catch (ScriptException | NoSuchMethodException | ScriptEngineResultException e) {
            throw new ScriptExecutorException(e);
        }
    }
}
