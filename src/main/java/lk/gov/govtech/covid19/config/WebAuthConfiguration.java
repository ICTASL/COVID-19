package lk.gov.govtech.covid19.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static lk.gov.govtech.covid19.util.Constants.*;

@Configuration
@EnableWebSecurity
public class WebAuthConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    PortalUserConfiguration users;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        /*
        * Endpoints without auth
        * - /application/** (all were GETs)
        * - /dhis/** (includes both POSTS and GETs)
        *
        * Endpoints with auth: either http basic auth or login (/portal) based can be used
        * - /notification/alert/add
        * - /notification/case/add
        * - /portal/**
        *
        * */
        http
            .authorizeRequests()
                .mvcMatchers( // to exclude auth for GETs
                        APPLICATION_API_CONTEXT +"/**",
                        DHIS_API_CONTEXT + "/**")
                .permitAll()
                .and()
            .authorizeRequests()
                .antMatchers(HttpMethod.POST, // to exclude auth for POSTs
                        DHIS_API_CONTEXT + "/**")
                .permitAll()
                .and()
            .csrf().disable()

            .authorizeRequests()
                .mvcMatchers(
                        "/notification/alert/add",
                        "/notification/case/add",
                        PORTAL_API_CONTEXT + "/**")
                .hasRole("USER")
                .and()
            .httpBasic()
                .and()
            .formLogin()
                .loginPage(PORTAL_API_CONTEXT)
                .permitAll()
                .defaultSuccessUrl(PORTAL_API_CONTEXT + NEWS_PATH) //redirects once successful
                .and()
            .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher(PORTAL_API_CONTEXT + "/logout")) //logs out with a GET
                .permitAll()
                .logoutSuccessUrl(PORTAL_API_CONTEXT); //redirects once successful
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.inMemoryAuthentication()
                    .withUser(users.getUsername1())
                    .password(passwordEncoder().encode(users.getPassword1()))
                    .roles("USER")
                .and()
                    .withUser(users.getUsername2())
                    .password(passwordEncoder().encode(users.getPassword2()))
                    .roles("USER")
                .and()
                    .withUser(users.getUsername3())
                    .password(passwordEncoder().encode(users.getPassword3()))
                    .roles("USER")
                .and()
                    .withUser(users.getUsername4())
                    .password(passwordEncoder().encode(users.getPassword4()))
                    .roles("USER");
    }
}
