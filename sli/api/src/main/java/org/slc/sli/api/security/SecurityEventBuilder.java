/*
 * Copyright 2012-2013 inBloom, Inc. and its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package org.slc.sli.api.security;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.*;

import org.slc.sli.api.config.EntityDefinitionStore;
import org.slc.sli.api.constants.ResourceNames;
import org.slc.sli.api.security.context.APIAccessDeniedException;
import org.slc.sli.api.security.context.EdOrgOwnershipArbiter;
import org.slc.sli.api.security.context.PagingRepositoryDelegate;
import org.slc.sli.common.constants.ParameterConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import org.slc.sli.api.security.context.resolver.EdOrgHelper;
import org.slc.sli.api.security.context.resolver.RealmHelper;
import org.slc.sli.common.util.logging.LogLevelType;
import org.slc.sli.common.util.logging.SecurityEvent;
import org.slc.sli.domain.Entity;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

/**
 * Utility class to fill in common SecurityEvent details
 */
@Component
public class SecurityEventBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(SecurityEventBuilder.class);

    @Autowired
    private CallingApplicationInfoProvider callingApplicationInfoProvider;

    private String thisNode;
    private String thisProcess;

    @Context
    UriInfo uriInfo;

    @Autowired
    private RealmHelper realmHelper;

    @Autowired
    private EdOrgHelper edOrgHelper;

    @Autowired
    private PagingRepositoryDelegate<Entity> repository;

    @Autowired
    private EdOrgOwnershipArbiter arbiter;

    @Autowired
    private EntityDefinitionStore entityDefinitionStore;

    private final String unknownEdOrg = "UNKNOWN";

    public SecurityEventBuilder() {
        try {
            InetAddress host = InetAddress.getLocalHost();
            thisNode = host.getHostName();
        } catch (UnknownHostException e) {
            LOG.info("Could not find hostname/process for SecurityEventLogging!");
        }
        thisProcess = ManagementFactory.getRuntimeMXBean().getName();
    }


    /**
     * Creates a security event when targetEdOrgs are irrelevant (targetEdOrgList is NOT set)
     *
     * @param loggingClass  java class logging the security event
     * @param requestUri    relevant URI
     * @param slMessage     security event message text
     * @param defaultTargetToUserEdOrg  whether or not to set targetEdOrgList to be userEdOrg by default
     * @return  security event with no targetEdOrgList set
     */
    public SecurityEvent createSecurityEvent(String loggingClass, URI requestUri, String slMessage, boolean defaultTargetToUserEdOrg) {
        return createSecurityEvent( loggingClass,  requestUri,  slMessage, null, null,null, null, defaultTargetToUserEdOrg);
    }

    /**
     * Creates a security event with explicitly specified targetEdOrgList based on the passed entity ids
     *
     * @param loggingClass      java class logging the security event
     * @param requestUri        relevant URI
     * @param slMessage         security event message text
     * @param explicitRealmEntity       used instead of the realm from the security context
     * @param entityType        type of the entity ids used to determine targetEdOrgs
     * @param entityIds         entity ids used to determine targetEdOrgs
     * @return security event with targetEdOrgList determined from the entities
     */
    public SecurityEvent createSecurityEvent(String loggingClass, URI requestUri, String slMessage,
                                             Entity explicitRealmEntity, String entityType, String[] entityIds) {
        Set<String> targetEdOrgs = getTargetEdOrgStateIds(entityType, entityIds);
        return createSecurityEvent(loggingClass, requestUri, slMessage, null,null, explicitRealmEntity, targetEdOrgs, false);
    }

    /**
     * Creates a security event with explicitly specified targetEdOrgList based on the passed entities
     *
     * @param loggingClass      java class logging the security event
     * @param requestUri        relevant URI
     * @param slMessage         security event message text
     * @param explicitRealmEntity       used instead of the realm from the security context
     * @param entityType        type of the entity ids used to determine targetEdOrgs
     * @param entities          entities used to determine targetEdOrgs
     * @return security event with targetEdOrgList determined from the entities
     */
    public SecurityEvent createSecurityEvent(String loggingClass, URI requestUri, String slMessage,
                                             Entity explicitRealmEntity, String entityType, Set<Entity> entities) {
        LOG.debug("Creating security event with targetEdOrgList determined from entities of type " + entityType);
        Set<String> targetEdOrgs = getTargetEdOrgStateIds(entityType, entities);
        return createSecurityEvent(loggingClass, requestUri, slMessage, null, null, explicitRealmEntity, targetEdOrgs, false);
    }

    /**
     * Creates a security event with explicitly specified targetEdOrgList via targetEdOrgIds
     *
     * @param loggingClass              java class logging the security event
     * @param explicitUri               relevant URI
     * @param slMessage                 security event message text
     * @param explicitPrincipal         used instead of the principle from the security context
     * @param explicitRealmEntity       used instead of the realm from the security context
     * @param targetEdOrgs              targetEdOrg stateOrganizationId values (note these are not db ids)
     * @param defaultTargetToUserEdOrg  whether or not to set targetEdOrgList to be userEdOrg by default
     * @return security event
     */
    public SecurityEvent createSecurityEvent(String loggingClass, URI explicitUri, String slMessage, SLIPrincipal explicitPrincipal, String clientId, Entity explicitRealmEntity,
                                             Set<String> targetEdOrgs, boolean defaultTargetToUserEdOrg) {
        SecurityEvent event = new SecurityEvent();
        URI requestUri = explicitUri;
        SLIPrincipal principal = explicitPrincipal;

        // if not explicitly set, try to get the uri from the context
        if (explicitUri == null && uriInfo != null) {
            requestUri = uriInfo.getRequestUri();
        }

        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

			if (auth != null && principal == null) {
				principal = (SLIPrincipal) auth.getPrincipal();
			}
			if (principal != null) {
                String externalId = principal.getExternalId();
                String name = principal.getName();
				event.setTenantId(principal.getTenantId());
				if ((externalId != null && !externalId.isEmpty())
						|| (name != null && !name.isEmpty())) {
					event.setUser(externalId + ", " + name);
				}
				event.setUserEdOrg(principal.getRealmEdOrg());

				// override the userEdOrg if explicit realm passed
				if (explicitRealmEntity != null) {
					LOG.debug("Using explicit realm entity to get userEdOrg");
					Map<String, Object> body = explicitRealmEntity.getBody();
					if (body != null && body.get("edOrg") != null) {
						event.setUserEdOrg((String) body.get("edOrg"));
					}
				} else if (event.getUserEdOrg() == null
						&& principal.getSessionId() != null) {
					LOG.debug("Determining userEdOrg from the current session");
					Entity realmEntity = realmHelper
							.getRealmFromSession(principal.getSessionId());
					if (realmEntity != null) {
						Map<String, Object> body = realmEntity.getBody();
						if (body != null && body.get("edOrg") != null) {
							event.setUserEdOrg((String) body.get("edOrg"));
						}
					}
				}
			}

            // set targetEdOrgList
            if (targetEdOrgs != null && !targetEdOrgs.isEmpty()) {
                LOG.debug("Setting targetEdOrgList explicitly: " + targetEdOrgs);
                event.setTargetEdOrgList(targetEdOrgs);  // TA10431 Sec Event now handles collections
            } else if (defaultTargetToUserEdOrg) {
                LOG.debug("Setting targetEdOrgList to be userEdOrg" + event.getUserEdOrg());
                setTargetToUserEdOrg(event);
            } else if (requestUri != null && requestUri.getPath() != null) {
                LOG.debug("Not explicitly specified, doing a best effort determination of targetEdOrg based on the request uri path: " + requestUri.getPath());
                Set<String> stateOrgIds = getTargetEdOrgStateIdsFromURI(requestUri);
                if (stateOrgIds != null && !stateOrgIds.isEmpty()) {
                    event.setTargetEdOrgList(stateOrgIds);		// TA10431 Sec Event now handles collections
                } else {
                    LOG.debug("Defaulting targetEdOrgList to userEdOrg since URI has no specific id.");
                    setTargetToUserEdOrg(event);
                }
            } else {
                LOG.debug("Unable to determine targetEdOrgList");
            }

            if (auth != null) {
                Object credential = auth.getCredentials();
                if (credential != null) {
                    event.setCredential(credential.toString());
                }
            }
			if(callingApplicationInfoProvider.getClientId()!=null){
				event.setAppId(callingApplicationInfoProvider.getClientId());
			} else if(clientId!=null){
				event.setAppId(clientId);
			}

            setSecurityEvent(loggingClass, requestUri, slMessage, event);

            LOG.info(String.format("Security event created: %s", event.toString()));

        } catch (Exception e) {
            final Writer result = new StringWriter();
            final PrintWriter printWriter = new PrintWriter(result);
            e.printStackTrace(printWriter);
            LOG.debug("Security event creation failed: \n" + result.toString());
            LOG.info(String.format("Could not build SecurityEvent for [%s] [%s]", requestUri, slMessage));
        }
        return event;
    }

    private void setTargetToUserEdOrg(SecurityEvent event) {
        if (event != null && event.getUserEdOrg() != null) {
            event.setTargetEdOrgList(event.getUserEdOrg());  // TA10431 - Sec Event now handles scalar for list
        }
    }

    private Set<String> getTargetEdOrgStateIdsFromURI(URI requestURI) {
        Set<String> targetEdOrgStateIds = null;

        if (requestURI != null) {
            String uriPath = requestURI.getPath();
            LOG.debug("Using URI path: " + uriPath + " to determine targetEdOrgs");
            if (uriPath != null) {
                String[] uriPathSegments = uriPath.split("/");

                // starting from the last uri segment, find the first entity id segment (non-resource value)
                // the preceding segment is the associated resource
                for (int i = uriPathSegments.length -1; i >= 0; i--) {
                    if (!ResourceNames.SINGULAR_LINK_NAMES.containsKey(uriPathSegments[i])) {
                        if (i > 0 && ResourceNames.SINGULAR_LINK_NAMES.containsKey(uriPathSegments[i-1])) {
                            String[] entityIds = uriPathSegments[i].split(",");  // some uri patterns can contain comma separated id lists
                            String entityType = ResourceNames.toEntityName(uriPathSegments[i-1]);
                            targetEdOrgStateIds = getTargetEdOrgStateIds(entityType, entityIds);
                        }
                    }
                }

            }
        }
        LOG.debug("From URI: " + requestURI + " got targetEdOrg state ids: " + targetEdOrgStateIds);
        return targetEdOrgStateIds;
    }

    private Set<String> getTargetEdOrgStateIds(String entityType, String[] entityIds) {
        Set<Entity> entities = getEntities(entityType, entityIds);

        return getTargetEdOrgStateIds(entityType, entities);
    }

    private Set<String> getTargetEdOrgStateIds(String entityType, Set<Entity> entities) {
        Set<String> targetEdOrgStateIds = null;

        // entityType should be set
        if (entityType == null) {
            return null;
        }

        if (entities == null || entities.isEmpty()) {
            return null;
        }

        try {
            targetEdOrgStateIds = getEdOrgStateIds(arbiter.findOwner(entities, entityType, false));
        } catch (APIAccessDeniedException nestedE) {
            // we were unable to determine the targetEdOrgs
            LOG.warn(nestedE.getMessage());
            return null;
        } catch (RuntimeException nestedE) {
            // we were unable to determine the targetEdOrgs
            LOG.warn(nestedE.getMessage());
            return null;
        }

        return targetEdOrgStateIds;
    }

    private Set<String> getEdOrgStateIds(Collection<Entity> edOrgs) {
        Set<String> edorgs = new HashSet<String>();

        for (Entity edOrg : edOrgs) {
            Map<String, Object> body = edOrg.getBody();
            if (body != null) {
                String stateId = (String) body.get(ParameterConstants.STATE_ORGANIZATION_ID);
                if (stateId != null && !stateId.isEmpty()) {
                    edorgs.add(stateId);
                }
            }
        }

        return edorgs;
    }

    private Set<Entity> getEntities(String entityType, String[] entityIds) {
        Set<Entity> entities = null;

        if (entityType != null && entityIds.length != 0) {
            entities = new HashSet<Entity>();
            for (int i = 0; i < entityIds.length; i++) {
                String id = entityIds[i];
                if (id == null || id.length() <= 0) {
                    continue;
                } else {
                    String collectionName = entityDefinitionStore.lookupByEntityType(entityType).getStoredCollectionName();
                    Entity entity = repository.findById(collectionName, id.trim());
                    if (entity == null) {
                        LOG.warn("Entity of type " + entityType + " with id " + id + " could not be found in the database.");
                    } else {
                        entities.add(entity);
                    }
                }
            }
        }
        return entities;
    }

	private void setSecurityEvent(String loggingClass, URI requestUri,
			String slMessage, SecurityEvent event) {
		if (requestUri != null) {
		    event.setActionUri(requestUri.toString());
		}
		event.setTimeStamp(new Date());
		event.setProcessNameOrId(thisProcess);
		event.setExecutedOn(thisNode);
		event.setClassName(loggingClass);
		event.setLogLevel(LogLevelType.TYPE_INFO);
		event.setLogMessage(slMessage);
	}


    public CallingApplicationInfoProvider getCallingApplicationInfoProvider() {
        return callingApplicationInfoProvider;
    }

    public void setCallingApplicationInfoProvider(CallingApplicationInfoProvider callingApplicationInfoProvider) {
        this.callingApplicationInfoProvider = callingApplicationInfoProvider;
    }

}
