package fr.fierdecoder.staticscriptengine.generator.test;

import fr.fierdecoder.staticscriptengine.annotation.StaticScriptInvocator;

import java.util.List;

@StaticScriptInvocator
public interface Caller {
    void voidMethod();

    void voidMethodWithParameters(String param1, int param2);

    String stringMethod();

    List<String> listMethod();
}
