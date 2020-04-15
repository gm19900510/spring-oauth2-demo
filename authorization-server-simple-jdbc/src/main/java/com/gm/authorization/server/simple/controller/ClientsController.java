package com.gm.authorization.server.simple.controller;

import java.util.Collection;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import com.gm.authorization.server.simple.config.AuthorityPropertyEditor;
import com.gm.authorization.server.simple.config.SplitCollectionEditor;

/**
 * ClientDetails管理
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("clients")
public class ClientsController {
	@Autowired
	private JdbcClientDetailsService jdbcClientDetailsService;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Collection.class, new SplitCollectionEditor(Set.class, ","));
		binder.registerCustomEditor(GrantedAuthority.class, new AuthorityPropertyEditor());
	}

	@Autowired
	BCryptPasswordEncoder passwordEncoder;

	/**
	 * @PreAuthorize使用说明 hasRole 角色授权：在自定义UserDetails返回Authority时需要加ROLE_前缀
	 *                   （数据库存储直接带有ROLE_前缀时，利用SimpleGrantedAuthority直接拼装；数据库存储时不带ROLE_前缀时，利用SimpleGrantedAuthority需加上ROLE_前缀拼装），
	 *                   Controller上使用时对于ROLE_前缀可加可不加；
	 * 
	 *                   hasAuthority
	 *                   权限授权：用户自定义的权限，返回的UserDetails的Authority只要与这里匹配就可以，这里不需要加ROLE_，名称保持一至即可。
	 */

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	@PreAuthorize("hasAuthority('clients:add')")
	public String showEditForm(@RequestParam(value = "client", required = false) String clientId, Model model) {

		ClientDetails clientDetails;
		if (clientId != null) {
			clientDetails = jdbcClientDetailsService.loadClientByClientId(clientId);
		} else {
			clientDetails = new BaseClientDetails();
		}

		model.addAttribute("clientDetails", clientDetails);
		return "add";
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	@PreAuthorize("hasAuthority('clients:edit')")
	public String editClient(@ModelAttribute BaseClientDetails clientDetails,
			@RequestParam(value = "newClient", required = false) String newClient) {
		if (newClient == null) {
			String pwdEncode = passwordEncoder.encode(clientDetails.getClientSecret());
			clientDetails.setClientSecret(pwdEncode);
			jdbcClientDetailsService.updateClientDetails(clientDetails);
		} else {
			String pwdEncode = passwordEncoder.encode(clientDetails.getClientSecret());
			clientDetails.setClientSecret(pwdEncode);
			jdbcClientDetailsService.addClientDetails(clientDetails);
		}

		if (!clientDetails.getClientSecret().isEmpty()) {
			jdbcClientDetailsService.updateClientSecret(clientDetails.getClientId(), clientDetails.getClientSecret());
		}
		return "redirect:/";
	}

	@RequestMapping(value = "{client.clientId}/delete", method = RequestMethod.GET)
	@PreAuthorize("hasAuthority('clients:delete')")
	public String deleteClient(@ModelAttribute BaseClientDetails ClientDetails,
			@PathVariable("client.clientId") String id) {
		jdbcClientDetailsService.removeClientDetails(jdbcClientDetailsService.loadClientByClientId(id).toString());
		return "redirect:/";
	}
}
