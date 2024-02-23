package eventy.config;

import eventy.security.CustomUserDetailsService;
import eventy.services.interfaces.UserDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {
    public static final String[] ENDPOINTS_ALLOWLIST = {
            "/css/**",
            "/login",
            "/signup",
            "/js/**",
            "/uploads/**"
    };

    public static final String LOGIN_URL = "/login";

    public static final String LOGIN_ERROR_URL = LOGIN_URL.concat("?error");
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";

    public static final String DEFAULT_SUCCESS_URL = "/";

    public static final String LOGOUT_URL = "/logout";

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(request ->
                        request.requestMatchers(ENDPOINTS_ALLOWLIST).permitAll()
                                .anyRequest().authenticated())
                .csrf(csrf -> csrf.disable())
                .formLogin(form ->
                        form.loginPage(LOGIN_URL)
                                .loginProcessingUrl(LOGIN_URL)
                                .failureUrl(LOGIN_ERROR_URL)
                                .usernameParameter(EMAIL)
                                .passwordParameter(PASSWORD)
                                .defaultSuccessUrl(DEFAULT_SUCCESS_URL))
                .logout(logout -> logout
                        .logoutUrl(LOGOUT_URL)
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessUrl(LOGIN_URL.concat("?logout")))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                        .invalidSessionUrl("/invalidSession.htm")
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(true));

        return http.build();
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
