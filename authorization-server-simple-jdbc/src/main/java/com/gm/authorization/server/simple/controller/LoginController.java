package com.gm.authorization.server.simple.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.approval.Approval;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static java.util.Arrays.asList;

/**
 * 自定义登录配置
 * 
 * @author Administrator
 *
 */
@Controller
public class LoginController {
	@Autowired
	private JdbcClientDetailsService jdbcClientDetailsService;

	@Autowired
	private ApprovalStore approvalStore;

	@InitBinder
	public void initBinder(WebDataBinder binder, WebRequest request) {
		// 转换日期
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));// CustomDateEditor为自定义日期编辑器
	}

	@RequestMapping("/")
	@PreAuthorize("hasAuthority('clients:view')")
	public ModelAndView root(Map<String, Object> model, Principal principal) {

		List<Approval> approvals = jdbcClientDetailsService.listClientDetails().stream()
				.map(clientDetails -> approvalStore.getApprovals(principal.getName(), clientDetails.getClientId()))
				.flatMap(Collection::stream).collect(Collectors.toList());

		model.put("approvals", approvals);
		model.put("clientDetails", jdbcClientDetailsService.listClientDetails());
		return new ModelAndView("index", model);

	}

	@Autowired
	private TokenStore tokenStore;

	@RequestMapping(value = "/approval/revoke", method = RequestMethod.POST)
	public String revokApproval(@ModelAttribute Approval approval) {

		approvalStore.revokeApprovals(asList(approval));
		tokenStore.findTokensByClientIdAndUserName(approval.getClientId(), approval.getUserId())
				.forEach(tokenStore::removeAccessToken);
		return "redirect:/";
	}

	@RequestMapping("/login")
	public String loginPage() {
		return "login";
	}
	
	@RequestMapping("/oauth/error")
	public String errorPage() {
		return "login";
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "redirect:/login?logout";
	}
}
