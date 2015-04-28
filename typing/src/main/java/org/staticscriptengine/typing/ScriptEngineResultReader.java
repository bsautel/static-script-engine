package org.staticscriptengine.typing;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

import java.util.*;
import java.util.Map.Entry;

public class ScriptEngineResultReader {
    public <T> T readToObject(Object value, Class<T> type) throws ScriptEngineResultException {
        if (value == null) {
            throw new ScriptExecutorException("null result");
        }
        if (type.isInstance(value)) {
            return (T) value;
        }
        throw new ScriptEngineResultException("Cannot convert value to " + type.getSimpleName());
    }

    public <T> List<T> readAsList(Object value, Class<T> type) throws ScriptEngineResultException {
        if (value instanceof ScriptObjectMirror) {
            ScriptObjectMirror scriptObjectMirror = (ScriptObjectMirror) value;
            if (scriptObjectMirror.isArray()) {
                Collection<Object> values = scriptObjectMirror.values();
                return readListItems(type, values);
            }
        } else if (value instanceof List) {
            List<Object> list = (List) value;
            return readListItems(type, list);
        }
        throw new IllegalArgumentException("");
    }

    private <T> List<T> readListItems(Class<T> type, Collection<Object> values) throws ScriptEngineResultException {
        List<T> result = new ArrayList<>();
        for (Object value : values) {
            result.add(readToObject(value, type));
        }
        return result;
    }

    public <K, V> Map<K, V> readAsMap(Object value, Class<K> keyType, Class<V> valueType)
            throws ScriptEngineResultException {
        if (value instanceof ScriptObjectMirror) {
            ScriptObjectMirror scriptObjectMirror = (ScriptObjectMirror) value;
            String[] javascriptKeys = scriptObjectMirror.getOwnKeys(true);
            Map<K, V> result = new HashMap<>();
            for (String javascriptKey : javascriptKeys) {
                Object member = scriptObjectMirror.getMember(javascriptKey);
                K key = readToObject(javascriptKey, keyType);
                V entryValue = readToObject(member, valueType);
                result.put(key, entryValue);
            }
            return result;
        }
        if (value instanceof Map) {
            Map<Object, Object> map = (Map) value;
            Map<K, V> result = new HashMap<>();
            for (Entry<Object, Object> entry : map.entrySet()) {
                K key = readToObject(entry.getKey(), keyType);
                V entryValue = readToObject(entry.getValue(), valueType);
                result.put(key, entryValue);
            }
            return result;
        }
        throw new IllegalArgumentException();
    }
}
