package com.gpch.hotel.configuration

import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ReloadableResourceBundleMessageSource
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor
import org.springframework.web.servlet.i18n.SessionLocaleResolver
import java.util.Locale

@Configuration
class WebMvcConfig : WebMvcConfigurer {

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun localeResolver(): LocaleResolver {
        val localeResolver = SessionLocaleResolver()
        localeResolver.setDefaultLocale(Locale.US)
        return localeResolver
    }

    @Bean
    fun localeChangeInterceptor(): LocaleChangeInterceptor {
        val localeChangeInterceptor = LocaleChangeInterceptor()
        localeChangeInterceptor.paramName = "lang"
        return localeChangeInterceptor
    }

    @Bean
    fun messageSource(): MessageSource {
        val messageSource = ReloadableResourceBundleMessageSource()
        messageSource.setBasenames(
            "classpath:/messages/account/message",
            "classpath:/messages/button/message",
            "classpath:/messages/changepassword/message",
            "classpath:/messages/dashboard/message",
            "classpath:/messages/employee/message",
            "classpath:/messages/error/message",
            "classpath:/messages/index/message",
            "classpath:/messages/maintenances/message",
            "classpath:/messages/product/message",
            "classpath:/messages/profile/message",
            "classpath:/messages/sidebar/message",
            "classpath:/messages/store/message",
            "classpath:/messages/table/message",
            "classpath:/messages/topbar/message",
            "classpath:/messages/user/message",
            "classpath:/messages/reportproblem/message",
            "classpath:/messages/resetpassword/message",
            "classpath:/messages/forgotpassword/message"
        )
        return messageSource
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(localeChangeInterceptor())
    }
}
