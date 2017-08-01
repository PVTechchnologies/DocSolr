package com.docsolr.controller;

import java.lang.reflect.Field;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.GrantType;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.docsolr.service.FacebookUtil;
import com.docsolr.service.OAuthServiceProvider;


public class FacebookController<FacebookApi> extends ConnectController{

	public FacebookController(ConnectionFactoryLocator connectionFactoryLocator,
			ConnectionRepository connectionRepository) {
		super(connectionFactoryLocator, connectionRepository);
		// TODO Auto-generated constructor stub
	}

	private static final Logger LOGGER = LoggerFactory
			.getLogger(FacebookController.class);
	private static final String FACEBOOK = "facebook";
	private static final String PUBLISH_SUCCESS = "success";
	private static final String PUBLISH_ERROR = "error";

	@Autowired
	private ConnectionFactoryRegistry connectionFactoryRegistry;

	@Autowired
	private OAuth2Parameters oAuth2Parameters;

	@Autowired
	FacebookUtil facebookUtil;
	@Autowired
	private ConnectionRepository connectionRepository;
	
	/*@Autowired
	private UsersConnectionRepository ucr;*/
	
	@Autowired
	@Qualifier("facebookServiceProvider")
	private OAuthServiceProvider<FacebookApi> facebookServiceProvider;

	@RequestMapping(value = "/signin", method = RequestMethod.GET)
	public ModelAndView signin(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		FacebookConnectionFactory facebookConnectionFactory = (FacebookConnectionFactory) connectionFactoryRegistry
				.getConnectionFactory(FACEBOOK);
		OAuth2Operations oauthOperations = facebookConnectionFactory
				.getOAuthOperations();
		oAuth2Parameters.setState("recivedfromfacebooktoken");
		String authorizeUrl = oauthOperations.buildAuthorizeUrl(
				GrantType.AUTHORIZATION_CODE, oAuth2Parameters);
		RedirectView redirectView = new RedirectView(authorizeUrl, true, true,
				true);

		return new ModelAndView(redirectView);
	}

	@RequestMapping(value = "/callback", method = RequestMethod.GET)
	@ResponseBody
	public String postOnWall(@RequestParam("code") String code,
			@RequestParam("state") String state, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		OAuthService oAuthService = facebookServiceProvider.getService();
		Verifier verifier = new Verifier(code);
		Token accessToken = oAuthService
				.getAccessToken(Token.empty(), verifier);

		FacebookTemplate template = new FacebookTemplate(accessToken.getToken());

		User facebookProfile = template.userOperations()
				.getUserProfile();

		String userId = facebookProfile.getId();

		LOGGER.info("Logged in User Id : {}", userId);

		MultiValueMap<String, Object> map = facebookUtil
				.publishLinkWithVisiblityRestriction(state);
		try {
			template.publish(facebookProfile.getId(), "feed", map);
		} catch (Exception ex) {
			LOGGER.error(
					"Exception Occurred while posting a link on facebook for user Id : {}, exception is : {}",
					userId, ex);
			return PUBLISH_ERROR;
		}

		return PUBLISH_SUCCESS;
	}

	@RequestMapping(value = "/callback", params = "error_reason", method = RequestMethod.GET)
	@ResponseBody
	public void error(@RequestParam("error_reason") String errorReason,
			@RequestParam("error") String error,
			@RequestParam("error_description") String description,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			LOGGER.error(
					"Error occurred while validating user, reason is : {}",
					errorReason);
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, description);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * intercept login flow and set spring security authentication
	 */
	@Override
	public synchronized RedirectView connectionStatusRedirect(String providerId, NativeWebRequest request) {
		String userName = null;
		RedirectView redirectView = new RedirectView("/user", true);
		
		

		return redirectView;
	}
}


/*For Facebook login from RedirectView */
/*Connection<Facebook> primaryConnection = connectionRepository.getPrimaryConnection(Facebook.class);
Facebook facebook = primaryConnection.getApi();
String accessToken = "";
try {
	Field fs = primaryConnection.getClass().getDeclaredField("accessToken");
	fs.setAccessible(true);
	accessToken = (String) fs.get(primaryConnection);
	FacebookTemplate template = new FacebookTemplate(accessToken);

	User facebookProfile = template.userOperations()
			.getUserProfile();

	ucr.createConnectionRepository(facebookProfile.getEmail()).updateConnection(primaryConnection);

} catch (Exception e) {
	e.printStackTrace();
}
	 String userId =
	primaryConnection.getKey().getProviderUserId();*/