package fr.fierdecoder.staticscriptengine;

import javax.script.ScriptEngine;

public class NashornScriptExecutorTest extends AbstractScriptExecutorTest {

    public NashornScriptExecutorTest() {
        super("nashorn", "script.js");
    }
}