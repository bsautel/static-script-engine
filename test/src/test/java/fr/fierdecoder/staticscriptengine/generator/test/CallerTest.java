package fr.fierdecoder.staticscriptengine.generator.test;

import org.junit.Test;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class CallerTest {
    @Test
    public void shouldGeneratedCallerExist() throws ClassNotFoundException {
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngine nashorn = scriptEngineManager.getEngineByName("nashorn");
        Caller caller =
                new fr.fierdecoder.staticscriptengine.generator.test.StaticScriptEngine_Caller((Invocable) nashorn);
    }
}
