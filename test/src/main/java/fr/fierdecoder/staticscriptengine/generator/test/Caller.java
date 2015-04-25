package fr.fierdecoder.staticscriptengine.generator.test;

import fr.fierdecoder.staticscriptengine.annotation.FunctionName;
import fr.fierdecoder.staticscriptengine.annotation.StaticScriptInvocator;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import java.util.List;

@StaticScriptInvocator
public interface Caller {
    void voidMethod();

    void voidMethodWithParameters(@FunctionName String param1, int param2);

    String stringMethod();

    List<String> listMethod();

    static Caller build(ScriptEngine scriptEngine) {
        return new StaticScriptEngine_Caller((Invocable) scriptEngine);
    }
}
