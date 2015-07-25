package de.chkal.backset.module.jsf.mojarra;

import com.sun.faces.spi.AnnotationProvider;
import de.chkal.backset.module.api.AnnotationDatabase;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.faces.bean.ManagedBean;
import javax.faces.component.FacesComponent;
import javax.faces.component.behavior.FacesBehavior;
import javax.faces.convert.FacesConverter;
import javax.faces.event.NamedEvent;
import javax.faces.render.FacesBehaviorRenderer;
import javax.faces.render.FacesRenderer;
import javax.faces.validator.FacesValidator;
import javax.servlet.ServletContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BacksetMojarraAnnotationProvider extends AnnotationProvider {

    private static final Logger log = LoggerFactory.getLogger(BacksetMojarraAnnotationProvider.class);

    private final static Iterable<Class<? extends Annotation>> TYPES = Arrays.asList(
            ManagedBean.class,
            FacesComponent.class,
            FacesBehavior.class,
            FacesConverter.class,
            NamedEvent.class,
            FacesRenderer.class,
            FacesBehaviorRenderer.class,
            FacesValidator.class);

    private final static Map<Class<? extends Annotation>, Set<Class<?>>> typeMap = new HashMap<>();

    public static void init(AnnotationDatabase annotationDatabase) {
        log.debug("init");
        for (Class<? extends Annotation> clazz : TYPES) {
            log.info("init with clazz: " + annotationDatabase.getTypes(clazz));
            typeMap.put(clazz, annotationDatabase.getTypes(clazz));
        }
    }

    public BacksetMojarraAnnotationProvider(ServletContext sc) {
        super(sc);
        log.debug("ctor");
    }

    @Override
    public Map<Class<? extends Annotation>, Set<Class<?>>> getAnnotatedClasses(Set<URI> urls) {
        log.debug("getAnnotatedClasses: " + typeMap);
        return typeMap;
    }

}
