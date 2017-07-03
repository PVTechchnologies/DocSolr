package com.docsolr.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.docsolr.common.dao.GenericDAO;
import com.docsolr.dto.CurrentUserVO;
import com.docsolr.entity.Account;
import com.docsolr.util.CommonUtil;

@Configurable
public class SaaSSecurityFilter implements Filter {

	public static final String TENANT_ID_HEADER = "X-TENANT-ID";

	private ApplicationContext ctx;
	@Autowired
	GenericDAO<Account> accountDao;
	// private AccountDao accountDao;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(filterConfig.getServletContext());
		accountDao = ctx.getBean(GenericDAO.class);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// Retrieve user id, password and tenant Id from the login page

		CurrentUserVO currentTenant = ctx.getBean(CurrentUserVO.class);

		System.out.println("========SaaSSecurityFilter========" + currentTenant);

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		String accountId = httpRequest.getHeader(TENANT_ID_HEADER);

		/*
		 * if(StringUtils.isBlank(accountId)){
		 * httpResponse.addHeader(HttpHeaders.CONTENT_TYPE, "application/json");
		 * CommonUtil.returnError(httpResponse,
		 * HttpServletResponse.SC_UNAUTHORIZED, null, TENANT_ID_HEADER +
		 * " header missing"); return; }
		 */

		try {
			Account account = accountDao.findEntityById(Account.class, Long.valueOf(accountId));

			currentTenant.setAccount(account);
		} catch (Exception e) {
			httpResponse.addHeader(HttpHeaders.CONTENT_TYPE, "application/json");
			CommonUtil.returnError(httpResponse, HttpServletResponse.SC_UNAUTHORIZED, null,
					"Invalid " + TENANT_ID_HEADER + " header value");
			return;
		}

		chain.doFilter(request, response);

	}

	@Override
	public void destroy() {

	}

}
