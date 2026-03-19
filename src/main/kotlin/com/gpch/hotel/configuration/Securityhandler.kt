package com.gpch.hotel.configuration

import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class Securityhandler : AuthenticationSuccessHandler {

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val roles = AuthorityUtils.authorityListToSet(authentication.authorities)
        when {
            roles.contains("ADMIN") || roles.contains("MANAGER") -> response.sendRedirect("/dashboard")
            roles.contains("STAFF") -> response.sendRedirect("/stores/manage-stores")
            roles.contains("TECHNICIAN") -> response.sendRedirect("/maintenances/manage-maintenance")
        }
    }
}
