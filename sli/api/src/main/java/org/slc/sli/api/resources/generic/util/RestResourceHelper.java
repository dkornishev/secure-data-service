package org.slc.sli.api.resources.generic.util;

import org.springframework.stereotype.Component;
import org.springframework.web.util.UriTemplate;

import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author jstokes
 */
@Component
public class RestResourceHelper implements ResourceHelper {
    private static final String RESOURCE_KEY = "resource";
    private static final String BASE_KEY = "base";
    private static final String VERSION_KEY = "version";
    private static final String SEP = "/";

    private static final String ID_KEY = "resource";

    @Override
    public String getResourceName(final UriInfo uriInfo, final ResourceTemplate template) {
        final Map<String, String> matchList = getMatchList(uriInfo, template);
        return matchList.get(VERSION_KEY) + SEP + matchList.get(RESOURCE_KEY);
    }

    @Override
    public String getBaseName(final UriInfo uriInfo, final ResourceTemplate template) {
        final Map<String, String> matchList = getMatchList(uriInfo, template);
        return matchList.get(BASE_KEY);
    }

    @Override
    public String getResourcePath(final UriInfo uriInfo, final ResourceTemplate template) {
        final Map<String, String> matchList = getMatchList(uriInfo, template);
        final String path = matchList.get(VERSION_KEY) + SEP + matchList.get(RESOURCE_KEY);

        switch (template) {
            case ONE_PART:
                return path;
            case TWO_PART:
                return path + SEP + "{id}";
            case THREE_PART:
                return matchList.get(VERSION_KEY) + SEP + matchList.get(BASE_KEY)
                        + SEP + "{id}" + SEP + matchList.get(RESOURCE_KEY);
            default:
                throw new AssertionError("Non-Valid Resource Template");
        }
    }

    private Map<String, String> getMatchList(UriInfo uriInfo, ResourceTemplate template) {
        final UriTemplate uriTemplate = new UriTemplate(template.getTemplate());
        return uriTemplate.match(uriInfo.getRequestUri().toString());
    }

    @Override
    public List<String> getIds(UriInfo uriInfo,ResourceTemplate template) {
//        final  UriTemplate uriTemplate = new UriTemplate(template.getTemplate());
//        final Map<String,String> matchList = uriTemplate.match(uri);
        ArrayList<String> ids = new ArrayList<String>();
//       for(String id : matchList.get(ID_KEY).split(",")) {
//           ids.add(id);
//       }
       return ids;
    }
}
