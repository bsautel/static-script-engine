package fr.fierdecoder.staticscriptengine;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class Script {
    public static void main(String[] args) throws ScriptException, NoSuchMethodException {
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngine nashorn = scriptEngineManager.getEngineByName("nashorn");
        nashorn.eval("function returnStringArray() {return ['a', 'b'];}");
        nashorn.eval("function returnIntegerArray() {return [1, 2, 3];}");
        nashorn.eval("function returnStringMap() {return {a: 'b', c: 'd'};}");
        Invocable nashornInvocator = (Invocable) nashorn;
        Object nashornStringArrayResult = nashornInvocator.invokeFunction("returnStringArray");
        System.out.println(convertList(nashornStringArrayResult, String.class));
        Object nashornIntegerArrayResult = nashornInvocator.invokeFunction("returnIntegerArray");
        System.out.println(convertList(nashornIntegerArrayResult, Integer.class));
        Object nashornStringMapResult = nashornInvocator.invokeFunction("returnStringMap");
        System.out.println(convertMap(nashornStringMapResult, String.class, String.class));
        ScriptEngine groovy = scriptEngineManager.getEngineByName("groovy");
        groovy.eval("def returnStringArray() { return ['a', 'b']}");
        groovy.eval("def returnIntegerArray() { return [1, 2, 3]}");
        groovy.eval("def returnStringMap() { return [a: 'b', c: 'd'];}");
        Invocable groovyInvocator = (Invocable) groovy;
        Object groovyStringArrayResult = groovyInvocator.invokeFunction("returnStringArray");
        System.out.println(convertList(groovyStringArrayResult, String.class));
        Object groovyIntegerArrayResult = groovyInvocator.invokeFunction("returnIntegerArray");
        System.out.println(convertList(groovyIntegerArrayResult, Integer.class));
        Object groovyStringMapResult = groovyInvocator.invokeFunction("returnStringMap");
        System.out.println(convertMap(groovyStringMapResult, String.class, String.class));
        ScriptEngine python = scriptEngineManager.getEngineByName("python");
        python.eval("def returnStringArray():\n    return ['a', 'b']");
        python.eval("def returnIntegerArray():\n    return [1, 2, 3]");
        python.eval("def returnStringMap():\n    return {'a': 'b', 'c': 'd'}");
        Invocable pythonInvocator = (Invocable) python;
        Object pythonStringArrayResult = pythonInvocator.invokeFunction("returnStringArray");
        System.out.println(convertList(pythonStringArrayResult, String.class));
        Object pythonIntegerArrayResult = pythonInvocator.invokeFunction("returnIntegerArray");
        System.out.println(convertList(pythonIntegerArrayResult, Integer.class));
        Object pythonStringMapResult = pythonInvocator.invokeFunction("returnStringMap");
        System.out.println(convertMap(pythonStringMapResult, String.class, String.class));
    }

    private static <T> List<T> convertList(Object value, Class<T> type) {
        if (value instanceof ScriptObjectMirror) {
            ScriptObjectMirror scriptObjectMirror = (ScriptObjectMirror) value;
            if (scriptObjectMirror.isArray()) {
                Collection<Object> values = scriptObjectMirror.values();
                return values.stream()
                        .map(listValue -> objectToType(listValue, type))
                        .collect(toList());
            }
        } else if (value instanceof List) {
            List<Object> list = (List) value;
            return list.stream()
                    .map(listValue -> objectToType(listValue, type))
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

    private static <K, V> Map<K, V> convertMap(Object value, Class<K> keyType, Class<V> valueType) {
        if (value instanceof ScriptObjectMirror) {
            ScriptObjectMirror scriptObjectMirror = (ScriptObjectMirror) value;
            String[] keys = scriptObjectMirror.getOwnKeys(true);
            Map<K, V> result = new HashMap<>();
            for (String key : keys) {
                Object member = scriptObjectMirror.getMember(key);
                K k = objectToType(key, keyType);
                V entryValue = objectToType(member, valueType);
                result.put(k, entryValue);
            }
            return result;
        }
        if (value instanceof Map) {
            Map<Object, Object> map = (Map) value;
            return map.entrySet().stream()
                    .collect(toMap(
                            entry -> objectToType(entry.getKey(), keyType),
                            entry -> objectToType(entry.getValue(), valueType)
                    ));
        }
        throw new IllegalArgumentException();
    }
}
