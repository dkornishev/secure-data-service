package org.slc.sli.api.security.context;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import org.slc.sli.api.security.context.resolver.DenyAllContextResolver;
import org.slc.sli.api.security.context.resolver.EntityContextResolver;

/**
 * Stores context based permission resolvers.
 * Can determine if a principal entity has permission to access a request entity.
 */
@Component
public class ContextResolverStore implements ApplicationContextAware {

    private static final Logger LOG = LoggerFactory.getLogger(ContextResolverStore.class);
    @Autowired
    private DenyAllContextResolver denyAllContextResolver;

    private Collection<EntityContextResolver> resolvers;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        resolvers = applicationContext.getBeansOfType(EntityContextResolver.class).values();
    }

    /**
     * Locates a resolver that can naviage the security context path from source entity type to target entity type
     *
     * @param fromEntityType
     * @param toEntityType
     * @return
     * @throws IllegalStateException
     */
    public EntityContextResolver findResolver(String fromEntityType, String toEntityType) throws IllegalStateException {

        EntityContextResolver found = null;
        for (EntityContextResolver resolver : this.resolvers) {
            if (resolver.canResolve(fromEntityType, toEntityType)) {
                found = resolver;
                break;
            }
        }

        if (found == null) {
            found = denyAllContextResolver;
            LOG.warn("No path resolver defined for {} -> {}. Returning deny-all resolver.", fromEntityType, toEntityType);
        }

        return found;
    }
}
