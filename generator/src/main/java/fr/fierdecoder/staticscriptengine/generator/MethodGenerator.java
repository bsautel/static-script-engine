package fr.fierdecoder.staticscriptengine.generator;

import fr.fierdecoder.staticscriptengine.annotation.FunctionName;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import java.io.StringWriter;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;
import static javax.lang.model.type.TypeKind.VOID;

public class MethodGenerator {
    private final Template template;

    public MethodGenerator(VelocityEngine velocityEngine) {
        template = velocityEngine.getTemplate("templates/method.vm");
    }

    public String generateMethod(ExecutableElement method) {
        VelocityContext velocityContext = buildTemplateContext(method);
        StringWriter writer = new StringWriter();
        template.merge(velocityContext, writer);
        return writer.toString();
    }

    private VelocityContext buildTemplateContext(ExecutableElement method) {
        TypeKind returnTypeKind = method.getReturnType().getKind();
        String returnType = returnTypeKind == VOID ? "void" : method.getReturnType().toString();
        String parameters = method.getParameters().stream()
                .map(parameter -> parameter.asType().toString() + " " + parameter.getSimpleName().toString())
                .collect(joining(", "));
        String invocationParameters = computeInvocationParameters(method);

        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("methodName", method.getSimpleName().toString());
        velocityContext.put("invocationFunctionName", computeInvocationFunctionName(method));
        velocityContext.put("returnType", returnType);
        velocityContext.put("isVoid", returnTypeKind == VOID);
        velocityContext.put("parameters", parameters);
        velocityContext.put("invocationParameters", invocationParameters);
        return velocityContext;
    }

    private String computeInvocationFunctionName(ExecutableElement method) {
        Optional<String> functionNameParameter = method.getParameters().stream()
                .filter(parameter -> asFunctionNameAnnotation(parameter).isPresent())
                .map(VariableElement::getSimpleName)
                .map(Name::toString)
                .findFirst();
        if (functionNameParameter.isPresent()) {
            return functionNameParameter.get();
        }
        return "\"" + method.getSimpleName() + "\"";
    }

    private Optional<FunctionName> asFunctionNameAnnotation(VariableElement parameter) {
        return ofNullable(parameter.getAnnotation(FunctionName.class));
    }

    private String computeInvocationParameters(ExecutableElement method) {
        List<? extends VariableElement> parameters = method.getParameters();
        if (parameters.isEmpty()) {
            return "";
        }
        return ", " + parameters.stream()
                .filter(parameter -> !asFunctionNameAnnotation(parameter).isPresent())
                .map(parameter -> parameter.getSimpleName().toString())
                .collect(joining(", "));
    }
}
