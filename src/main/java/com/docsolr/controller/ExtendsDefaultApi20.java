package com.docsolr.controller;

import org.scribe.builder.api.DefaultApi20;
import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.extractors.JsonTokenExtractor;
import org.scribe.model.OAuthConfig;
import org.scribe.utils.OAuthEncoder;
import org.scribe.utils.Preconditions;

public class ExtendsDefaultApi20 extends DefaultApi20 {

	@Override
	public AccessTokenExtractor getAccessTokenExtractor()
	  {
	    return new JsonTokenExtractor();
	  }
	
	private static final String AUTHORIZE_URL = "https://www.facebook.com/dialog/oauth?client_id=%s&redirect_uri=%s";
	  private static final String SCOPED_AUTHORIZE_URL = AUTHORIZE_URL + "&scope=%s";

	  @Override
	  public String getAccessTokenEndpoint()
	  {
	    return "https://graph.facebook.com/oauth/access_token";
	  }

	  @Override
	  public String getAuthorizationUrl(OAuthConfig config)
	  {
	    Preconditions.checkValidUrl(config.getCallback(), "Must provide a valid url as callback. Facebook does not support OOB");

	    // Append scope if present
	    if(config.hasScope())
	    {
	     return String.format(SCOPED_AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()), OAuthEncoder.encode(config.getScope()));
	    }
	    else
	    {
	      return String.format(AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()));
	    }
	  }

}
