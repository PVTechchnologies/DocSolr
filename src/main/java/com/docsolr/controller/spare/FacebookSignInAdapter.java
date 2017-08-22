package com.docsolr.controller.spare;

import java.lang.reflect.Field;
import java.util.Arrays;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.web.context.request.NativeWebRequest;

public class FacebookSignInAdapter implements SignInAdapter {
    @Override
    public String signIn(String localUserId, Connection<?> connection, NativeWebRequest request) {
        SecurityContextHolder.getContext().setAuthentication(
          new UsernamePasswordAuthenticationToken(
          connection.getDisplayName(), null, 
          Arrays.asList(new SimpleGrantedAuthority("FACEBOOK_USER"))));
        //Connection<Facebook> primaryConnection = connectionRepository.getPrimaryConnection(Facebook.class);
		//Facebook facebook = primaryConnection.getApi();
		/*String accessToken = "";
		try {
			Field fs = connection.getClass().getDeclaredField("accessToken");
			fs.setAccessible(true);
			accessToken = (String) fs.get(connection);
			FacebookTemplate template = new FacebookTemplate(accessToken);

			User facebookProfile = template.userOperations()
					.getUserProfile();

		} catch (Exception e) {
			e.printStackTrace();
		}*/
			// String userId =
			// primaryConnection.getKey().getProviderUserId();
		//return redirectView;
		
        return "success";
    }
}
