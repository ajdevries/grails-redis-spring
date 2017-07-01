package test

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter
import org.springframework.web.filter.CharacterEncodingFilter

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true)
class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/shutdown");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        http.addFilterBefore(filter, WebAsyncManagerIntegrationFilter.class);

        http.csrf()
                .ignoringAntMatchers("/stomp/**")
                .ignoringAntMatchers("/t/**")
                .ignoringAntMatchers("/api/**")
                .and()
                .headers()
                .frameOptions().sameOrigin()
                .and()
                .authorizeRequests()
                .antMatchers('/health').permitAll()
                .antMatchers('/assets/**').permitAll()
                .antMatchers('/**').hasAnyRole('USER', 'ADMIN')
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .logout().permitAll()
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().
                withUser('user').password('password').roles('USER')
    }
}