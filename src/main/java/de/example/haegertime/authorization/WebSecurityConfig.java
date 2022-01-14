package de.example.haegertime.authorization;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncodeTest();
    }

    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/api/user").hasAnyAuthority("ADMIN","BOOKKEEPER","EMPLOYEE")
                .antMatchers(HttpMethod.POST,"/api/customer/create").hasAnyAuthority("ADMIN","BOOKKEEPER")
                .antMatchers("/api/customer/{id}").hasAnyAuthority("ADMIN","BOOKKEEPER")
                .antMatchers("/api/customer/update").hasAnyAuthority("ADMIN","BOOKKEEPER")
                .antMatchers("/api/customer/addproject/{id}").hasAnyAuthority("ADMIN","BOOKKEEPER")
                .antMatchers("/api/customer/delete/{id}").hasAnyAuthority("ADMIN","BOOKKEEPER")
                .antMatchers("/api/customer").hasAnyAuthority("ADMIN","BOOKKEEPER")
                .antMatchers("/api/projects").hasAnyAuthority("EMPLOYEE","ADMIN","BOOKKEEPER")
                .antMatchers("/api/projects/{id}").hasAnyAuthority("BOOKKEEPER","ADMIN","EMPLOYEE")
                .antMatchers(HttpMethod.PATCH, "/api/projects/update/{id}").hasAnyAuthority("ADMIN","BOOKKEEPER")
                .antMatchers("/api/user/all").hasAnyAuthority("ADMIN","BOOKKEEPER","EMPLOYEE")
                .antMatchers(HttpMethod.POST, "/api/user/").hasAnyAuthority("ADMIN")
                .antMatchers("/api/user/myacc").hasAnyAuthority("ADMIN","EMPLOYEE","BOOKKEEPER")
                .antMatchers("/api/user/myacc/update").hasAnyAuthority("ADMIN","BOOKKEEPER","EMPLOYEE")
                .antMatchers("/api/user/delete/{id}").hasAnyAuthority("ADMIN")
                .antMatchers("/api/user/reactiv/{id}").hasAnyAuthority("ADMIN")
                .antMatchers("/api/user/deactiv/{id}").hasAnyAuthority("ADMIN")
                .antMatchers("/api/user/updaterole/{id}").hasAnyAuthority("ADMIN")
                .antMatchers("/api/timetable/actualhours/{id}").hasAnyAuthority("ADMIN","BOOKKEEPER","EMPLOYEE")
                .antMatchers("/api/user/register/timetable").hasAnyAuthority("EMPLOYEE")
                .antMatchers("/api/invoice/export/excel").hasAnyAuthority("BOOKKEEPER")
                .antMatchers("/api/timetable/hours/employees/{id}").hasAnyAuthority("BOOKKEEPER")
                .antMatchers("/api/timetable/assignEmployee/**").hasAnyAuthority("ADMIN","BOOKKEEPER","EMPLOYEE")
                .antMatchers("/api/timetable/assignProject/**").hasAnyAuthority("ADMIN","BOOKKEEPER","EMPLOYEE")
                .antMatchers("/api/timetable//OvertimeByEmail/{email})").hasAnyAuthority("ADMIN","BOOKKEEPER","EMPLOYEE")
                .antMatchers("/api/invoice/export/pdf").hasAnyAuthority("BOOKKEEPER")
                .antMatchers("/api/invoice/export/xml").hasAnyAuthority("BOOKKEEPER")
                .antMatchers("/api/timetable/overhours/{id}").hasAnyAuthority("ADMIN","BOOKKEEPER","EMPLOYEE")
                .antMatchers("/absence/holiday/employee/{id}").hasAnyAuthority("BOOKKEEPER")
                .antMatchers("/employees/status/holiday").hasAnyAuthority("BOOKKEEPER")
                .antMatchers("/employees/status/sick").hasAnyAuthority("BOOKKEEPER")
                .antMatchers("/absence/sick/employee/{id}").hasAnyAuthority("BOOKKEEPER")
                .antMatchers("/apply/holiday/{id}").hasAnyAuthority("BOOKEERPER","EMPLOYEE","ADMIN")
                .antMatchers("/holiday/decline/employee/{id}").hasAnyAuthority("BOOKKEERPER","ADMIN")
                .antMatchers("/holidays").hasAnyAuthority("EMPLOYEE","BOOKKEEPER","ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin().permitAll()
                .and()
                .logout().permitAll().and().httpBasic()
                .and().csrf().disable();
                //TODO die Methode erweitern nach Wunsch

    }
}
