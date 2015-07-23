package de.chkal.backset.module.jsf.mojarra;

import de.chkal.backset.module.api.Module;
import de.chkal.backset.module.api.ModuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsfMojarraModule implements Module {

    private static final Logger log = LoggerFactory.getLogger(JsfMojarraModule.class);

    @Override
    public int getPriority() {
        return 300;
    }

    @Override
    public void init(ModuleContext context) {
        log.info("initializing Mojarra JSF implementation");
        context.register(new JsfMojarraDeploymentEnricher());
        // BacksetAnnotationProvider.init(context.getAnnotationDatabase());
    }

    @Override
    public void destroy() {
    }

}
