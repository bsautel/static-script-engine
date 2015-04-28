package org.staticscriptengine.generator;

import org.junit.Before;
import org.junit.Test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.InputStreamReader;

import static org.assertj.core.api.Assertions.assertThat;

public class CallerTest {
    private Caller caller;

    @Before
    public void setUp() throws ScriptException {
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngine nashorn = scriptEngineManager.getEngineByName("nashorn");
        nashorn.eval(new InputStreamReader(getClass().getResourceAsStream("script.js")));
        caller = Caller.build(nashorn);
    }

    @Test
    public void shouldCallAVoidFunctionWithoutError() {
        caller.voidMethod();
    }

    @Test
    public void shouldCallAFunctionWithParameters() {
        caller.voidMethodWithParameters("a", 2);
    }

    @Test
    public void shouldCallAFunctionWithParametersIncludingOneThatDefinesFunctionName() {
        String result = caller.stringMethodWithParametersIncludingOneThatDefinesFunctionName("myFunction", 1);

        assertThat(result).isEqualTo("hello 1");
    }
}
