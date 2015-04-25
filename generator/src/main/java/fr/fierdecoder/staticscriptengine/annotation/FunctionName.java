package fr.fierdecoder.staticscriptengine.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.SOURCE;

@Retention(SOURCE)
@Target(PARAMETER)
public @interface FunctionName {
}
