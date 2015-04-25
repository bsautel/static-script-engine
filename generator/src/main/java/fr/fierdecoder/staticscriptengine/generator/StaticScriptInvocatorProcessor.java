package fr.fierdecoder.staticscriptengine.generator;

import com.google.auto.service.AutoService;
import fr.fierdecoder.staticscriptengine.annotation.StaticScriptInvocator;
import org.apache.velocity.app.VelocityEngine;

import javax.annotation.processing.*;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;

import static javax.lang.model.SourceVersion.RELEASE_8;
import static javax.lang.model.element.ElementKind.INTERFACE;
import static javax.tools.Diagnostic.Kind.ERROR;
import static javax.tools.Diagnostic.Kind.NOTE;

@AutoService(Processor.class)
@SupportedAnnotationTypes("fr.fierdecoder.staticscriptengine.annotation.StaticScriptInvocator")
@SupportedSourceVersion(RELEASE_8)
public class StaticScriptInvocatorProcessor extends AbstractProcessor {
    private final VelocityEngine velocityEngine;
    private ClassGenerator classGenerator;

    public StaticScriptInvocatorProcessor() {
        Properties properties = new Properties();
        properties.put("resource.loader", "classloader");
        properties.put("classloader.resource.loader.class",
                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        velocityEngine = new VelocityEngine(properties);
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        classGenerator = new ClassGenerator(velocityEngine, processingEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(StaticScriptInvocator.class)) {
            if (element.getKind() == INTERFACE) {
                try {
                    TypeElement typeElement = (TypeElement) element;
                    classGenerator.generateClassImplementation(typeElement);
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
}
