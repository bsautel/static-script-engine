package fr.fierdecoder.staticscriptengine.generator;

import com.google.auto.service.AutoService;
import fr.fierdecoder.staticscriptengine.generator.annotation.ScriptInvocator;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import javax.annotation.processing.*;
import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.Properties;
import java.util.Set;

import static javax.lang.model.SourceVersion.RELEASE_8;
import static javax.lang.model.element.ElementKind.INTERFACE;
import static javax.tools.Diagnostic.Kind.ERROR;
import static javax.tools.Diagnostic.Kind.NOTE;

@AutoService(Processor.class)
@SupportedAnnotationTypes("fr.fierdecoder.staticscriptengine.generator.annotation.ScriptInvocator")
@SupportedSourceVersion(RELEASE_8)
public class ScriptInvocatorProcessor extends AbstractProcessor {
    private final Template template;

    public ScriptInvocatorProcessor() {
        Properties properties = new Properties();
        properties.put("resource.loader", "classloader");
        properties.put("classloader.resource.loader.class",
                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        VelocityEngine velocityEngine = new VelocityEngine(properties);
        template = velocityEngine.getTemplate("generator-template.vm");
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(ScriptInvocator.class)) {
            if (element.getKind() == INTERFACE) {
                try {
                    TypeElement typeElement = (TypeElement) element;
                    generateImplementation(typeElement);
                    processingEnv.getMessager().printMessage(NOTE, "Generating " + typeElement.getQualifiedName());
                } catch (IOException e) {
                    processingEnv.getMessager().printMessage(ERROR, "Exception " + e);
                }
            } else {
                processingEnv.getMessager().printMessage(ERROR, "@ScriptInvocationProcessor can only be applied to interfaces");
            }
        }
        return true;
    }

    private void generateImplementation(TypeElement typeElement) throws IOException {
        PackageElement packageElement = (PackageElement) typeElement.getEnclosingElement();
        Name packageName = packageElement.getQualifiedName();
        Name interfaceName = typeElement.getSimpleName();
        String className = "StaticScriptEngine_" + interfaceName;
        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("packageName", packageName);
        velocityContext.put("className", className);
        velocityContext.put("interfaceName", interfaceName);
        JavaFileObject javaFile = processingEnv.getFiler().createSourceFile(packageName + className);
        Writer writer = javaFile.openWriter();
        template.merge(velocityContext, writer);
        writer.close();
    }
}
