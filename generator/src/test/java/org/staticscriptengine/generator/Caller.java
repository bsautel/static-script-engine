package org.staticscriptengine.generator;

import org.staticscriptengine.annotation.FunctionName;
import org.staticscriptengine.annotation.StaticScriptInvocator;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import java.util.List;

@StaticScriptInvocator
public interface Caller {
    void voidMethod();

    void voidMethodWithParameters(String param1, int param2);

    String stringMethod();

    String stringMethodWithParametersIncludingOneThatDefinesFunctionName(@FunctionName String param1, int param2);

    List<String> listMethod();

    static Caller build(ScriptEngine scriptEngine) {
        return new StaticScriptEngine_Caller((Invocable) scriptEngine);
    }
}
