package com.gpch.hotel.configuration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    Securityhandler successHandler;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private DataSource dataSource;

    @Value("${spring.queries.users-query}")
    private String usersQuery;

    @Value("${spring.queries.roles-query}")
    private String rolesQuery;

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.
                jdbcAuthentication()
                .usersByUsernameQuery(usersQuery)
                .authoritiesByUsernameQuery(rolesQuery)
                .dataSource(dataSource)
                .passwordEncoder(bCryptPasswordEncoder);

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.
                authorizeRequests()
                .antMatchers("/loaderio-70f45f7671f7078d85fe8fbdfaad27ed.txt", "/actuator/health", "/actuator/**").permitAll()
                .antMatchers("/", "/login","/forgot-password","/confirm-reset","/reset-password").permitAll()
                .antMatchers("/dashboard", "/employees/**").hasAnyAuthority("ADMIN", "MANAGER")
                .antMatchers("/users/**").hasAuthority("ADMIN")
                .antMatchers("/profile").hasAnyAuthority("STAFF", "MANAGER")
                .antMatchers("/settings/account", "/settings/updateaccount").hasAnyAuthority("STAFF", "MANAGER")
                .antMatchers("/settings/changepassword", "settings/updatepassword","settings/report_problem").hasAnyAuthority("ADMIN", "STAFF", "MANAGER","TECHNICIAN")
                .antMatchers("/maintenances/add-maintenance").hasAnyAuthority("ADMIN", "STAFF", "MANAGER", "TECHNICIAN")
                .antMatchers("/stores/**").hasAnyAuthority("ADMIN", "STAFF", "MANAGER")
                .antMatchers("/products/**").hasAnyAuthority("ADMIN", "STAFF", "MANAGER")
                .antMatchers("/maintenances/**").hasAnyAuthority("ADMIN", "TECHNICIAN").anyRequest()
                .authenticated().and().csrf().disable().formLogin()
                .loginPage("/login").failureUrl("/login?error=true")
                .successHandler(successHandler)
                .usernameParameter("email")
                .passwordParameter("password")
                .and().logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessUrl("/").and().exceptionHandling()
                .accessDeniedPage("/access-denied")
                .and().rememberMe().tokenValiditySeconds(2592000).key("mySecret!");



    }

    @Override
    public void configure(WebSecurity web) {
        web
                .ignoring()
                .antMatchers("/resources/**", "/static/**", "/css/**", "/img/**", "/vendor/**", "/svg/**", "/js/**", "/messages/**");
    }

}