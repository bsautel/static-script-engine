package fr.fierdecoder.staticscriptengine;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class Script {
    public static void main(String[] args) throws ScriptException, NoSuchMethodException {
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngine nashorn = scriptEngineManager.getEngineByName("nashorn");
        nashorn.eval("function returnStringArray() {return ['a', 'b'];}");
        nashorn.eval("function returnIntegerArray() {return [1, 2, 3];}");
        Invocable nashornInvocator = (Invocable) nashorn;
        Object stringArrayResult = nashornInvocator.invokeFunction("returnStringArray");
        System.out.println(convert(stringArrayResult));
        Object integerArrayResult = nashornInvocator.invokeFunction("returnIntegerArray");
        System.out.println(convert(integerArrayResult));
    }

    private static List<String> convert(Object result) {
        if (result instanceof ScriptObjectMirror) {
            ScriptObjectMirror scriptObjectMirror = (ScriptObjectMirror) result;
            if (scriptObjectMirror.isArray()) {
                Collection<Object> values = scriptObjectMirror.values();
                return values.stream()
                        .map(Script::objectToString)
                        .collect(toList());
            }
        } else if (result instanceof List) {
            List<Object> list = (List<Object>) result;
            return list.stream()
                    .map(Script::objectToString)
                    .collect(toList());
        }
        throw new IllegalArgumentException("");
    }

    private static String objectToString(Object value) {
        if (value instanceof String) {
            return (String) value;
        }
        throw new IllegalArgumentException(value + " must be of type String");
    }
}
