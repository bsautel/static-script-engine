package org.staticscriptengine.typing;

import org.junit.Before;
import org.junit.Test;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

public abstract class AbstractScriptExecutorTest {
    private final String engineName;
    private final String scriptName;
    private ScriptExecutor scriptExecutor;

    public AbstractScriptExecutorTest(String engineName, String scriptName) {
        this.engineName = engineName;
        this.scriptName = scriptName;
    }

    @Before
    public void setUp() throws ScriptException {
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngine scriptEngine = scriptEngineManager.getEngineByName(engineName);
        InputStream scriptStream = getClass().getClassLoader().getResourceAsStream(scriptName);
        scriptEngine.eval(new InputStreamReader(scriptStream));
        scriptExecutor = new ScriptExecutor((Invocable) scriptEngine);
    }

    @Test
    public void shouldReturnABoolean() throws ScriptExecutorException {
        assertThat(scriptExecutor.returnBoolean()).isTrue();
    }

    @Test
    public void shouldReturnAString() throws ScriptExecutorException {
        assertThat(scriptExecutor.returnString()).isEqualTo("Hello World!");
    }

    @Test(expected = ScriptExecutorException.class)
    public void shouldThrowAnErrorWhenReturningABooleanAsString() throws ScriptExecutorException {
        scriptExecutor.returnBooleanAsString();
    }

    @Test(expected = ScriptExecutorException.class)
    public void shouldReturnVoidAString() throws ScriptExecutorException {
        scriptExecutor.returnVoidAsString();
    }

    @Test
    public void shouldReturnAStringArray() throws ScriptExecutorException {
        assertThat(scriptExecutor.returnStringArray()).containsExactly("a", "b");
    }

    @Test
    public void shouldReturnAnIntegerArray() throws ScriptExecutorException {
        assertThat(scriptExecutor.returnIntegerArray()).containsExactly(1, 2, 3);
    }

    @Test
    public void shouldReturnStringStringMap() throws ScriptExecutorException {
        assertThat(scriptExecutor.returnStringStringMap()).contains(entry("a", "b"), entry("c", "d"));
    }

    @Test
    public void shouldReturnStringBooleanMap() throws ScriptExecutorException {
        assertThat(scriptExecutor.returnStringBooleanMap()).contains(entry("a", true), entry("b", false));
    }
}
