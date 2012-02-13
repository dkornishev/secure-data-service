package org.slc.sli.api.security.oauth;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.oauth2.common.ExpiringOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.OAuth2ProviderTokenServices;
import org.springframework.security.oauth2.provider.token.RandomValueOAuth2ProviderTokenServices;

import org.slc.sli.api.representation.EntityBody;
import org.slc.sli.api.security.SLIPrincipal;
import org.slc.sli.dal.repository.EntityRepository;
import org.slc.sli.domain.Entity;

/**
 * Responsible for storage and management of access and refresh tokens for OAuth
 * 2.0 implementation.
 * 
 * 
{
  "_id" : UUID [auto-generated by Mongo],
  "user_id" : String,  
  "user_realm" : String,  
  "user_roles" : String,  
  "access_token" : 
  {
    "value" : String,
    "expiration" : Date,
    "token_type" : String,
    "refresh_token" :
    {
      "value" : String,
      "expiration" : Date
    }
  }
}
 * 
 * @author shalka
 */
public class TokenManager extends RandomValueOAuth2ProviderTokenServices
        implements OAuth2ProviderTokenServices {
    
    @Autowired
    EntityRepository repo;


    @Override
    protected OAuth2Authentication<?, ?> readAuthentication(
            OAuth2AccessToken token) {
        token.getValue();
        return null;
    }

    @Override
    protected void storeAccessToken(OAuth2AccessToken token,
            @SuppressWarnings("rawtypes") OAuth2Authentication authentication) {
        
        EntityBody container = new EntityBody();
        SLIPrincipal principal = (SLIPrincipal) authentication.getPrincipal();
        container.put("user_id", principal.getId());
        container.put("user_realm", principal.getRealm());
        container.put("user_roles", principal.getRoles());  // ?

        EntityBody accessToken = new EntityBody();
        accessToken.put("value", token.getValue());
        accessToken.put("expiration", token.getExpiration());
        accessToken.put("token_type", token.getTokenType());
        ExpiringOAuth2RefreshToken rt = (ExpiringOAuth2RefreshToken) token.getRefreshToken();
        EntityBody refreshToken = new EntityBody();
        refreshToken.put("value", token.getRefreshToken().getValue());
        refreshToken.put("expiration", rt.getExpiration());
        accessToken.put("refresh_token", refreshToken);
        
        container.put("access_token", accessToken);
        repo.create("tokens", container);
    }

    @Override
    protected OAuth2AccessToken readAccessToken(String tokenValue) {
        OAuth2AccessToken result = new OAuth2AccessToken();
        Iterable<Entity> results = repo.findByQuery("tokens", 
                new Query(Criteria.where("body.access_token.value").is(tokenValue)), 0, 1);
        for (Entity entity : results) {
            Map<String, Object> accessToken = (Map<String, Object>) entity.getBody().get("access_token");
            result.setValue((String) accessToken.get("value")); 
            result.setExpiration((Date) accessToken.get("expiration"));
            result.setTokenType((String) accessToken.get("token_type"));
            
            //TODO - Also add the Refresh Token
        }
        return result;
    }

    @Override
    protected void removeAccessToken(String tokenValue) {
        Iterable<Entity> results = repo.findByQuery("tokens", 
                new Query(Criteria.where("body.access_token.value").is(tokenValue)), 0, 1);
        for (Entity entity : results) {
            repo.delete("tokens", entity.getEntityId());
        }
    }

    @Override
    protected OAuth2Authentication<?, ?> readAuthentication(
            ExpiringOAuth2RefreshToken token) {
        return null;
    }

    @Override
    protected void storeRefreshToken(ExpiringOAuth2RefreshToken refreshToken,
            @SuppressWarnings("rawtypes") OAuth2Authentication authentication) {

    }

    @Override
    protected ExpiringOAuth2RefreshToken readRefreshToken(String tokenValue) {
        ExpiringOAuth2RefreshToken result = new ExpiringOAuth2RefreshToken();
        Iterable<Entity> results = repo.findByQuery("tokens", new Query(Criteria.where("body.refresh_token.value").is(tokenValue)), 0, 1);
        for (Entity cur : results) {
            Map<String, Object> refreshToken = (Map<String, Object>) cur.getBody().get("refresh_token");
            result.setExpiration((Date) refreshToken.get("expiration"));
            result.setValue((String) refreshToken.get("value"));
        }
        return result;
    }

    @Override
    protected void removeRefreshToken(String tokenValue) {
        Iterable<Entity> results = repo.findByQuery("tokens", new Query(Criteria.where("body.refresh_token.value").is(tokenValue)), 0, 1);
        for (Entity cur : results) {
            cur.getBody().put("refresh_token", null);
            repo.update("tokens", cur);
        }
    }

    @Override
    protected void removeAccessTokenUsingRefreshToken(String refreshToken) {
        Iterable<Entity> results = repo.findByQuery("tokens", new Query(Criteria.where("body.refresh_token.value").is(refreshToken)), 0, 1);
        for (Entity cur : results) {
            repo.delete("tokens", cur.getEntityId());
        }

    }
}
