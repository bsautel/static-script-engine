package fr.fierdecoder.staticscriptengine;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class ScriptEngineResultConverter {
    public <T> List<T> convertList(Object value, Class<T> type) {
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

    private <T> T objectToType(Object value, Class<T> type) {
        if (type.isAssignableFrom(value.getClass())) {
            return (T) value;
        }
        throw new IllegalArgumentException(value + " must be of type String");
    }

    public <K, V> Map<K, V> convertMap(Object value, Class<K> keyType, Class<V> valueType) {
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
