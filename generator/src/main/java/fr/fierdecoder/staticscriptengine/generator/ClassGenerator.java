package fr.fierdecoder.staticscriptengine.generator;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static javax.lang.model.element.Modifier.STATIC;
import static javax.lang.model.util.ElementFilter.methodsIn;

public class ClassGenerator {
    private final MethodGenerator methodGenerator;
    private final Template template;
    private Filer filer;

    public ClassGenerator(VelocityEngine velocityEngine, ProcessingEnvironment processingEnvironment) {
        template = velocityEngine.getTemplate("templates/class.vm");
        filer = processingEnvironment.getFiler();
        methodGenerator = new MethodGenerator(velocityEngine);
    }

    public void generateClassImplementation(TypeElement typeElement) throws IOException {
        PackageElement packageElement = (PackageElement) typeElement.getEnclosingElement();
        Name packageName = packageElement.getQualifiedName();
        Name interfaceName = typeElement.getSimpleName();
        List<String> methods = generateMethodsImplementations(typeElement);
        String className = "StaticScriptEngine_" + interfaceName;
        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("packageName", packageName);
        velocityContext.put("className", className);
        velocityContext.put("interfaceName", interfaceName);
        velocityContext.put("methods", methods);
        velocityContext.put("generatorClassName", StaticScriptInvocatorProcessor.class.getCanonicalName());
        JavaFileObject javaFile = filer.createSourceFile(packageName + "." + className);
        Writer writer = javaFile.openWriter();
        template.merge(velocityContext, writer);
        writer.close();
    }

    private List<String> generateMethodsImplementations(TypeElement typeElement) {
        List<ExecutableElement> methods = methodsIn(typeElement.getEnclosedElements());
        return methods.stream()
                .filter(method -> !method.getModifiers().contains(STATIC))
                .map(methodGenerator::generateMethod)
                .collect(toList());
    }
}
