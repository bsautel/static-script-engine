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
        Object nashornStringArrayResult = nashornInvocator.invokeFunction("returnStringArray");
        System.out.println(convert(nashornStringArrayResult, String.class));
        Object nashornIntegerArrayResult = nashornInvocator.invokeFunction("returnIntegerArray");
        System.out.println(convert(nashornIntegerArrayResult, Integer.class));
        ScriptEngine groovy = scriptEngineManager.getEngineByName("groovy");
        groovy.eval("def returnStringArray() { return ['a', 'b']}");
        groovy.eval("def returnIntegerArray() { return [1, 2, 3]}");
        Invocable groovyInvocator = (Invocable) groovy;
        Object groovyStringArrayResult = groovyInvocator.invokeFunction("returnStringArray");
        System.out.println(convert(groovyStringArrayResult, String.class));
        Object groovyIntegerArrayResult = groovyInvocator.invokeFunction("returnIntegerArray");
        System.out.println(convert(groovyIntegerArrayResult, Integer.class));
        ScriptEngine python = scriptEngineManager.getEngineByName("python");
        python.eval("def returnStringArray():\n    return ['a', 'b']");
        python.eval("def returnIntegerArray():\n    return [1, 2, 3]");
        Invocable pythonInvocator = (Invocable) python;
        Object pythonStringArrayResult = pythonInvocator.invokeFunction("returnStringArray");
        System.out.println(convert(pythonStringArrayResult, String.class));
        Object pythonIntegerArrayResult = pythonInvocator.invokeFunction("returnIntegerArray");
        System.out.println(convert(pythonIntegerArrayResult, Integer.class));
    }

    private static <T> List<T> convert(Object result, Class<T> type) {
        if (result instanceof ScriptObjectMirror) {
            ScriptObjectMirror scriptObjectMirror = (ScriptObjectMirror) result;
            if (scriptObjectMirror.isArray()) {
                Collection<Object> values = scriptObjectMirror.values();
                return values.stream()
                        .map(value -> objectToType(value, type))
                        .collect(toList());
            }
        } else if (result instanceof List) {
            List<Object> list = (List) result;
            return list.stream()
                    .map(value -> objectToType(value, type))
                    .collect(toList());
        }
        throw new IllegalArgumentException("");
    }

    private static <T> T objectToType(Object value, Class<T> type) {
        if (type.isAssignableFrom(value.getClass())) {
            return (T) value;
        }
        throw new IllegalArgumentException(value + " must be of type String");
    }
}
