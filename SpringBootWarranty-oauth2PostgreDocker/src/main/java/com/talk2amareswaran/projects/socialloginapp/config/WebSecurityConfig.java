package com.talk2amareswaran.projects.socialloginapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.social.security.SpringSocialConfigurer;

import com.talk2amareswaran.projects.socialloginapp.entity.AppRole;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private AuthenticationSession successHandler;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.authorizeRequests().antMatchers("/", "/signup", "/login", "/logout").permitAll();
		http.authorizeRequests().antMatchers("/userInfo","/addNew","/warantys","/addNewElement","/expired").access("hasRole('" + AppRole.ROLE_USER + "')");
		http.authorizeRequests().antMatchers("/admin","/adminAddNewGet","/deleteUser/{email}","/confirmDeleteUser/{email}","/edit/{email}","adminAddNew","/adminValidWarrantys","/adminExpiredWarrantys","/deleteWarrantyLink/{id}","/confirmDeleteWarranty").access("hasRole('" + AppRole.ROLE_ADMIN + "')");
		http.authorizeRequests().antMatchers("/fileupdate","/imageDownload/{id}","/download/{id}","/displayImage/{id}","/update/{id}","/updateTrial").access("hasRole('" + AppRole.ROLE_USER + "') or hasRole('" + AppRole.ROLE_ADMIN + "')");
		http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/403");
		http.authorizeRequests().and().formLogin()
				.loginProcessingUrl("/j_spring_security_check") 
				.loginPage("/login")
				.successHandler(successHandler)
				//.defaultSuccessUrl("/addNew")
				.failureUrl("/login?error=true")
				.usernameParameter("username")
				.passwordParameter("password");
		http.authorizeRequests().and().logout().logoutUrl("/logout").logoutSuccessUrl("/");
		http.apply(new SpringSocialConfigurer()).signupUrl("/signup");
	}

    @Override
    public UserDetailsService userDetailsService() {
        return userDetailsService;
    }
}
