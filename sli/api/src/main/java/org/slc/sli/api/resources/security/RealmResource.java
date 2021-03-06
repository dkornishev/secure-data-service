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

package org.slc.sli.api.resources.security;

import java.util.*;

import javax.annotation.PostConstruct;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import com.google.common.base.Joiner;
import org.slc.sli.api.config.EntityDefinition;
import org.slc.sli.api.config.EntityDefinitionStore;
import org.slc.sli.api.init.RoleInitializer;
import org.slc.sli.api.representation.EntityBody;
import org.slc.sli.api.resources.v1.HypermediaType;
import org.slc.sli.api.security.RightsAllowed;
import org.slc.sli.api.security.SLIPrincipal;
import org.slc.sli.api.security.SecurityEventBuilder;
import org.slc.sli.api.security.service.AuditLogger;
import org.slc.sli.api.security.context.resolver.EdOrgHelper;
import org.slc.sli.api.service.EntityNotFoundException;
import org.slc.sli.api.service.EntityService;
import org.slc.sli.api.util.SecurityUtil;
import org.slc.sli.api.util.SecurityUtil.SecurityTask;
import org.slc.sli.dal.convert.IdConverter;
import org.slc.sli.domain.Entity;
import org.slc.sli.domain.NeutralCriteria;
import org.slc.sli.domain.NeutralQuery;
import org.slc.sli.domain.Repository;
import org.slc.sli.domain.enums.Right;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.slc.sli.common.util.logging.SecurityEvent;

/**
 * Realm role mapping API. Allows full CRUD on realm objects. Primarily intended
 * to allow mappings between SLI roles and client roles as realms should not be
 * created or deleted frequently.
 * 
 * @author jnanney
 */
@Component
@Scope("request")
@Path("/realm")
@Produces({ HypermediaType.JSON + ";charset=utf-8" })
public class RealmResource {

    private static final Logger LOG = LoggerFactory.getLogger(RealmResource.class);

    public static final String REALM = "realm";
    public static final String ED_ORG = "edOrg";
    public static final String RESPONSE = "response";
    public static final String NAME = "name";
    public static final String UNIQUE_IDENTIFIER = "uniqueIdentifier";
    public static final String IDP_ID = "idp.id";

    public static final String ARTIFACT_RESOLUTION_ENDPOINT = "artifactResolutionEndpoint";
    public static final String SOURCE_ID = "sourceId";

    public static final String IDP = "idp";

    private static final int SOURCEID_LENGTH = 40;
    
    @Autowired
    private EntityDefinitionStore store;

    private EntityService service;

    @Autowired
    @Qualifier("validationRepo")
    private Repository<Entity> repo;

    @Autowired
    private IdConverter idConverter;

    @Autowired
    private SecurityEventBuilder securityEventBuilder;

    @Autowired
    private AuditLogger auditLogger;

    @Autowired
    private RoleInitializer roleInitializer;

    @Autowired
    private EdOrgHelper helper;

    @PostConstruct
    public void init() {
        EntityDefinition def = store.lookupByResourceName(REALM);
        setService(def.getService());
    }

    // Injector
    public void setService(EntityService service) {
        this.service = service;
    }

    @PUT
    @Path("{realmId}")
    @Consumes("application/json")
    @RightsAllowed({ Right.CRUD_REALM })
    public Response updateRealm(@PathParam("realmId") String realmId, EntityBody updatedRealm,
            @Context final UriInfo uriInfo) {

        if (updatedRealm == null) {
            throw new IllegalArgumentException("Updated Realm was null");
        }

        EntityBody oldRealm = service.get(realmId);

        if (!canEditCurrentRealm(updatedRealm) || oldRealm.get(ED_ORG) != null
                && !oldRealm.get(ED_ORG).equals(SecurityUtil.getEdOrg())) {
            EntityBody body = new EntityBody();
            body.put(RESPONSE, "You are not authorized to update this realm.");
            return Response.status(Status.FORBIDDEN).entity(body).build();
        }
        Response validateUniqueness = validateUniqueId(realmId, (String) updatedRealm.get(UNIQUE_IDENTIFIER),
                (String) updatedRealm.get(NAME),
                getIdpId(updatedRealm));
        if (validateUniqueness != null) {
            return validateUniqueness;
        }

        Map<String, Object> idp = (Map<String, Object>) updatedRealm.get(IDP);
        Response validateArtifactResolution = validateArtifactResolution((String) idp.get(ARTIFACT_RESOLUTION_ENDPOINT), (String) idp.get(SOURCE_ID));
        if (validateArtifactResolution != null) {
            LOG.debug("Invalid artifact resolution information");
            return validateArtifactResolution;
        }

        // set the tenant and edOrg
        updatedRealm.put("tenantId", SecurityUtil.getTenantId());
        updatedRealm.put(ED_ORG, SecurityUtil.getEdOrg());

        if (service.update(realmId, updatedRealm, false)) {
            logSecurityEvent(uriInfo, oldRealm, updatedRealm);
            return Response.status(Status.NO_CONTENT).build();
        }
        return Response.status(Status.BAD_REQUEST).build();
    }

    @DELETE
    @Path("{realmId}")
    @RightsAllowed({ Right.CRUD_REALM })
    public Response deleteRealm(@PathParam("realmId") String realmId, @Context final UriInfo uriInfo) {
        EntityBody deletedRealm = service.get(realmId);
        if (!canEditCurrentRealm(deletedRealm)) {
            EntityBody body = new EntityBody();
            body.put(RESPONSE, "You are not authorized to delete a realm for another ed org");
            return Response.status(Status.FORBIDDEN).entity(body).build();
        }
        service.delete(realmId);
        roleInitializer.dropRoles(realmId);
        logSecurityEvent(uriInfo, deletedRealm, null);
       
        return Response.status(Status.NO_CONTENT).build();
    }

    @POST
    @RightsAllowed({ Right.CRUD_REALM })
    public Response createRealm(EntityBody newRealm, @Context final UriInfo uriInfo) {

        if (!canEditCurrentRealm(newRealm)) {
            EntityBody body = new EntityBody();
            body.put(RESPONSE, "You are not authorized to create a realm for another ed org");
            return Response.status(Status.FORBIDDEN).entity(body).build();
        }
        Response validateUniqueness = validateUniqueId(null, (String) newRealm.get(UNIQUE_IDENTIFIER),
                (String) newRealm.get(NAME), getIdpId(newRealm));
        if (validateUniqueness != null) {
            LOG.debug("On realm create, uniqueId is not unique");
            return validateUniqueness;
        }

        Response validateArtifactResolution = validateArtifactResolution((String) newRealm.get(ARTIFACT_RESOLUTION_ENDPOINT), (String) newRealm.get(SOURCE_ID));
        if (validateArtifactResolution != null) {
            LOG.debug("Invalid artifact resolution information");
            return validateArtifactResolution;
        }

        // set the tenant and edOrg
        newRealm.put("tenantId", SecurityUtil.getTenantId());
        newRealm.put(ED_ORG, SecurityUtil.getEdOrg());

        String id = service.create(newRealm);
        logSecurityEvent(uriInfo, null, newRealm);

        // Also create custom roles
        roleInitializer.dropAndBuildRoles(id);
        
        String uri = uriToString(uriInfo) + "/" + id;

        return Response.status(Status.CREATED).header("Location", uri).build();
    }

    @GET
    @Path("{realmId}")
    @RightsAllowed({ Right.ADMIN_ACCESS })
    public Response readRealm(@PathParam("realmId") String realmId) {
        EntityBody result = service.get(realmId);
        return Response.ok(result).build();
    }

    @GET
    @RightsAllowed({ Right.ADMIN_ACCESS })
    public Response getRealms(@QueryParam(REALM) @DefaultValue("") String realm, @Context UriInfo info) {
        SLIPrincipal principal = (SLIPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        NeutralQuery neutralQuery = new NeutralQuery();
        neutralQuery.addCriteria(new NeutralCriteria("tenantId", NeutralCriteria.OPERATOR_EQUAL, principal
                .getTenantId()));
        neutralQuery.addCriteria(new NeutralCriteria(ED_ORG, NeutralCriteria.OPERATOR_EQUAL, principal.getEdOrg()));
        neutralQuery.setOffset(0);
        neutralQuery.setLimit(100);

        List<EntityBody> result = new ArrayList<EntityBody>();
        Iterable<String> realmList = service.listIds(neutralQuery);
        for (String id : realmList) {
            EntityBody curEntity = service.get(id);

            if (curEntity == null) {
                continue;
            }

            if (realm.length() == 0) {
                curEntity.put("link", info.getBaseUri() + info.getPath().replaceAll("/$", "") + "/" + id);
                result.add(curEntity);
            } else {
                if (realm.equals(curEntity.get(REALM))) {
                    result.add(curEntity);
                }
            }
        }
        return Response.ok(result).build();
    }

    private Response validateUniqueId(String realmId, String uniqueId, String displayName, String idpId) {
        // Check for uniqueness of Unique ID
        final NeutralQuery query = new NeutralQuery();
        query.addCriteria(new NeutralCriteria(UNIQUE_IDENTIFIER, "=", uniqueId));
        if (realmId != null) {
            query.addCriteria(new NeutralCriteria("_id", "!=", idConverter.toDatabaseId(realmId)));
        }
        Entity body = SecurityUtil.runWithAllTenants(new SecurityTask<Entity>() {

            @Override
            public Entity execute() {
                return repo.findOne(REALM, query);
            }
        });

        if (body != null) {
            LOG.debug("uniqueId: {}", body.getBody().get(UNIQUE_IDENTIFIER));
            Map<String, String> res = new HashMap<String, String>();
            res.put(RESPONSE, "Cannot have duplicate unique identifiers");
            return Response.status(Status.BAD_REQUEST).entity(res).build();
        }

        // Check for uniqueness of Display Name
        final NeutralQuery displayNameQuery = new NeutralQuery();
        displayNameQuery.addCriteria(new NeutralCriteria(NAME, "=", displayName));
        if (realmId != null) {
            displayNameQuery.addCriteria(new NeutralCriteria("_id", "!=", idConverter.toDatabaseId(realmId)));
        }
        Entity entity = SecurityUtil.runWithAllTenants(new SecurityTask<Entity>() {

            @Override
            public Entity execute() {
                return repo.findOne(REALM, displayNameQuery);
            }
        });

        if (entity != null) {
            LOG.debug("name: {}", entity.getBody().get(NAME));
            Map<String, String> res = new HashMap<String, String>();
            res.put(RESPONSE, "Cannot have duplicate display names");
            return Response.status(Status.BAD_REQUEST).entity(res).build();
        }

        return null;
    }

    protected Response validateArtifactResolution(String artifactResolutionEndpoint, String sourceId) {

        if (artifactResolutionEndpoint == null && sourceId == null) {
            return null;
        }
        Map<String, String> res = new HashMap<String, String>();

        if (artifactResolutionEndpoint != null && sourceId != null) {
            if ((artifactResolutionEndpoint.isEmpty() && sourceId.isEmpty())
                    || (sourceId.length() == SOURCEID_LENGTH && !artifactResolutionEndpoint.isEmpty())) {
                return null;
            } else {
                res.put(RESPONSE, "Source id needs to be 40 characters long");
            }
        } else {
            res.put(RESPONSE, "artifactResolutionEndpoint and sourceId need to be present together.");
        }

        return Response.status(Status.BAD_REQUEST).entity(res).build();
    }

    private static String uriToString(UriInfo uri) {
        return uri.getBaseUri() + uri.getPath().replaceAll("/$", "");
    }

    private boolean canEditCurrentRealm(EntityBody realm) {
        String edOrg = SecurityUtil.getEdOrg();
        return edOrg != null && edOrg.equals(realm.get(ED_ORG));
    }
    
    private String getIdpId(Map body) {
        Map idp = (Map) body.get("idp");
        return (String) idp.get("id");
    }

    private void logSecurityEvent(UriInfo uriInfo, EntityBody oldRealm, EntityBody newRealm) {
        Joiner joiner = Joiner.on("/").skipNulls();

        String oldRealmName             = (oldRealm != null) ? (String) oldRealm.get(NAME)   : null;
        String newRealmName             = (newRealm != null) ? (String) newRealm.get(NAME)   : null;
        String oldEdOrg                 = (oldRealm != null) ? (String) oldRealm.get(ED_ORG) : null;
        String newEdOrg                 = (newRealm != null) ? (String) newRealm.get(ED_ORG) : null;
        List<String> oldTargetEdOrgList = (oldEdOrg != null) ?  Arrays.asList(oldEdOrg):new ArrayList<String>();
        List<String> newTargetEdOrgList = (newEdOrg != null) ?  Arrays.asList(newEdOrg):new ArrayList<String>();

        String message      = null;
        SecurityEvent event = securityEventBuilder.createSecurityEvent(RealmResource.class.getName(), uriInfo.getRequestUri(), message, false);

        if (oldRealm == null && newRealm != null)        {//Create
            event.setLogMessage("Realm [" + newRealmName + "] created!");
            event.setTargetEdOrgList(newTargetEdOrgList);
            auditLogger.audit(event);
        } else if (oldRealm != null && newRealm == null) {//Delete
            event.setLogMessage("Realm [" + oldRealmName + "] deleted!");
            event.setTargetEdOrgList(oldTargetEdOrgList);
            auditLogger.audit(event);
        } else if (oldRealm != null && newRealm != null) {//Update. Can realm edOrg be updated? Assuming yes.
            if (oldEdOrg == null && newEdOrg == null) {
                event.setLogMessage("Realm [" + joiner.join(oldRealmName, newRealmName) + "] updated!");
                auditLogger.audit(event);
            } else if (oldEdOrg != null && newEdOrg == null) {
                event.setLogMessage("Realm [" + oldRealmName + "] deleted!");
                event.setTargetEdOrgList(oldTargetEdOrgList);
                auditLogger.audit(event);
            } else if (oldEdOrg == null && newEdOrg != null) {
                event.setLogMessage("Realm [" + newRealmName + "] created!");
                event.setTargetEdOrgList(newTargetEdOrgList);
                auditLogger.audit(event);
            } else if (oldEdOrg.equals(newEdOrg)) { //both not null and equal
                event.setLogMessage("Realm [" + joiner.join(oldRealmName, newRealmName) + "] updated!");
                event.setTargetEdOrgList(oldTargetEdOrgList);
                auditLogger.audit(event);
            } else {                                //both not null and unequal
                event.setLogMessage("Realm [" + oldRealmName + "] deleted!");
                event.setTargetEdOrgList(oldTargetEdOrgList);
                auditLogger.audit(event);

                event.setLogMessage("Realm [" + oldRealmName + "] created!");
                event.setTargetEdOrgList(newTargetEdOrgList);
                auditLogger.audit(event);
            }
        } else {                                          //None
             LOG.info("Old and New Realms are both null!");
        }
    }
}
