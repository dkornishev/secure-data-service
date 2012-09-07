/*
 * Copyright 2012 Shared Learning Collaborative, LLC
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
package org.slc.sli.api.security.service.mangler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.slc.sli.domain.NeutralCriteria;
import org.slc.sli.domain.NeutralQuery;
import org.springframework.stereotype.Component;

@Component
public class StudentQueryMangler extends Mangler {
    
    @Override
    public NeutralQuery mangleQuery(NeutralQuery query, NeutralCriteria securityCriteria) {
        setTheQuery(query);
        setSecurityCriteria(securityCriteria);
        // Is this a  list query or a specific one?
        boolean isList = true;
        NeutralCriteria idCriteria = null;
        for (NeutralCriteria criteria : query.getCriteria()) {
            if (criteria.getKey().equals("_id")) {
                idCriteria = criteria;
                isList = false;
            }
        }
        if (isList) {
            adjustSecurityForPaging();
            query.addOrQuery(new NeutralQuery(securityCriteria));
            return query;
        }
        else {
             Set<String> finalIdSet = new HashSet<String>((Collection) securityCriteria.getValue());
             finalIdSet.retainAll((Collection)idCriteria.getValue());
             finalIdSet = new HashSet<String>(adjustIdListForPaging(new ArrayList<String>(finalIdSet)));
             if (finalIdSet.size() != 0) {
                 //They're asking for something they CAN see.
                 query.removeCriteria(idCriteria);
                 idCriteria.setValue(finalIdSet);
                 query.addOrQuery(new NeutralQuery(idCriteria));
                 return query;
             }
        }
        // This is a 403
        return null;
    }
    
    @Override
    public boolean respondsTo(String type) {
        //FIXME get rid of this until we know we need to split by type.
       return true;
        // return (type.equals("student") || type.equals("section"));
    }
    
}
