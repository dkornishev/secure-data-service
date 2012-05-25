package org.slc.sli.api.resources.v1.entity;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import org.slc.sli.api.config.EntityDefinitionStore;
import org.slc.sli.api.resources.v1.DefaultCrudResource;
import org.slc.sli.client.constants.ResourceNames;
import org.slc.sli.client.constants.v1.ParameterConstants;
import org.slc.sli.client.constants.v1.PathConstants;

/**
 * Represents the time span for which grades are reported.
 *
 * @author jstokes
 *
 */
@Path(PathConstants.V1 + "/" + PathConstants.GRADING_PERIODS)
@Component
@Scope("request")
public class GradingPeriodResource extends DefaultCrudResource {

    @Autowired
    public GradingPeriodResource(EntityDefinitionStore entityDefs) {
        super(entityDefs, ResourceNames.GRADING_PERIODS);
    }

    /**
     * Returns each $$ReportCard$$ of the given $$GradingPeriod$$.
     *
     * @param gradingPeriodId
     *            The Id of the $$GradingPeriod$$
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return each $$ReportCard$$ of the given $$GradingPeriod$$
     */
    @GET
    @Path("{" + ParameterConstants.GRADING_PERIOD_ID + "}" + "/" + PathConstants.REPORT_CARDS)
    public Response getReportCards(@PathParam(ParameterConstants.GRADING_PERIOD_ID) final String gradingPeriodId,
            @Context HttpHeaders headers,
            @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.REPORT_CARDS, "gradingPeriodId", gradingPeriodId, headers, uriInfo);
    }

    /**
     * Returns each $$Grade$$ of the given $$GradingPeriod$$.
     *
     * @param gradingPeriodId
     *            The Id of the $$GradingPeriod$$
     * @param headers
     *            HTTP Request Headers
     * @param uriInfo
     *            URI information including path and query parameters
     * @return each $$Grade$$ of the given $$GradingPeriod$$
     */
    @GET
    @Path("{" + ParameterConstants.GRADING_PERIOD_ID + "}" + "/" + PathConstants.GRADES)
    public Response getGrades(@PathParam(ParameterConstants.GRADING_PERIOD_ID) final String gradingPeriodId,
            @Context HttpHeaders headers,
            @Context final UriInfo uriInfo) {
        return super.read(ResourceNames.GRADES, "gradingPeriodId", gradingPeriodId, headers, uriInfo);
    }

}
