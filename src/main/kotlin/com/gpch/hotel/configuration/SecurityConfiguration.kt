package com.gpch.hotel.configuration

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import javax.sql.DataSource

@Configuration
@EnableWebSecurity
class SecurityConfiguration : WebSecurityConfigurerAdapter() {

    @Autowired
    lateinit var successHandler: Securityhandler

    @Autowired
    private lateinit var bCryptPasswordEncoder: BCryptPasswordEncoder

    @Autowired
    private lateinit var dataSource: DataSource

    @Value("\${spring.queries.users-query}")
    private lateinit var usersQuery: String

    @Value("\${spring.queries.roles-query}")
    private lateinit var rolesQuery: String

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.jdbcAuthentication()
            .usersByUsernameQuery(usersQuery)
            .authoritiesByUsernameQuery(rolesQuery)
            .dataSource(dataSource)
            .passwordEncoder(bCryptPasswordEncoder)
    }

    override fun configure(http: HttpSecurity) {
        http.authorizeRequests()
            .antMatchers("/loaderio-70f45f7671f7078d85fe8fbdfaad27ed.txt", "/actuator/health", "/actuator/**").permitAll()
            .antMatchers("/", "/login", "/forgot-password", "/confirm-reset", "/reset-password").permitAll()
            .antMatchers("/dashboard", "/employees/**").hasAnyAuthority("ADMIN", "MANAGER")
            .antMatchers("/users/**").hasAuthority("ADMIN")
            .antMatchers("/profile").hasAnyAuthority("STAFF", "MANAGER")
            .antMatchers("/settings/account", "/settings/updateaccount").hasAnyAuthority("STAFF", "MANAGER")
            .antMatchers("/settings/changepassword", "settings/updatepassword", "settings/report_problem")
            .hasAnyAuthority("ADMIN", "STAFF", "MANAGER", "TECHNICIAN")
            .antMatchers("/maintenances/add-maintenance").hasAnyAuthority("ADMIN", "STAFF", "MANAGER", "TECHNICIAN")
            .antMatchers("/stores/**").hasAnyAuthority("ADMIN", "STAFF", "MANAGER")
            .antMatchers("/products/**").hasAnyAuthority("ADMIN", "STAFF", "MANAGER")
            .antMatchers("/room-types/**", "/rooms/**", "/guests/**", "/bookings/**", "/payments/**")
            .hasAnyAuthority("ADMIN", "STAFF", "MANAGER")
            .antMatchers("/maintenances/**").hasAnyAuthority("ADMIN", "TECHNICIAN").anyRequest()
            .authenticated().and().csrf().disable().formLogin()
            .loginPage("/login").failureUrl("/login?error=true")
            .successHandler(successHandler)
            .usernameParameter("email")
            .passwordParameter("password")
            .and().logout()
            .logoutRequestMatcher(AntPathRequestMatcher("/logout"))
            .invalidateHttpSession(true)
            .deleteCookies("JSESSIONID")
            .logoutSuccessUrl("/").and().exceptionHandling()
            .accessDeniedPage("/access-denied")
            .and().rememberMe().tokenValiditySeconds(2592000).key("mySecret!")
    }

    override fun configure(web: WebSecurity) {
        web.ignoring()
            .antMatchers("/resources/**", "/static/**", "/css/**", "/img/**", "/vendor/**", "/svg/**", "/js/**", "/messages/**")
    }
}
