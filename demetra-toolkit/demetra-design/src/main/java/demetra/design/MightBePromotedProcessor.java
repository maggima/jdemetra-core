/*
 * Copyright 2016 National Bank of Belgium
 * 
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved 
 * by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * 
 * http://ec.europa.eu/idabc/eupl
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and 
 * limitations under the Licence.
 */
package demetra.design;

import internal.Processors;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Philippe Charles
 */
@ServiceProvider(service = Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("demetra.design.MightBePromoted")
public final class MightBePromotedProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) {
            return false;
        }
        Processors.streamOf(annotations, roundEnv)
                .forEach(element -> {
                    if (!isInternalOrNotPublic(element, processingEnv)) {
                        Processors.error(processingEnv, element, "'%s' must be in internal package or not public");
                    }
                });
        return true;
    }

    private static boolean isInternalOrNotPublic(Element element, ProcessingEnvironment env) {
        return isTypeInInternalPackage(element, env) || isNotPublic(element, env);
    }

    private static boolean isTypeInInternalPackage(Element element, ProcessingEnvironment env) {
        if (element instanceof TypeElement) {
            TypeElement type = (TypeElement) element;
            PackageElement pkg = env.getElementUtils().getPackageOf(type);
            String fullname = pkg.getQualifiedName().toString();
            String packagePattern = type.getAnnotation(MightBePromoted.class).packagePattern();
            return fullname.matches(packagePattern);
        }
        return false;
    }

    public static boolean isNotPublic(Element element, ProcessingEnvironment env) {
        return !element.getModifiers().contains(Modifier.PUBLIC);
    }
}
