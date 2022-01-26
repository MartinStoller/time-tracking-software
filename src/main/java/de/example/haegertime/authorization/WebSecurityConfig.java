package de.example.haegertime.authorization;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, proxyTargetClass = true)
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
        //TODO: Ornung reinbringen: Mit kommentaren und Absätzen öhnliche Methoden zusammentun. Wo es sinn macht mit * und ** UR
        http.authorizeRequests()

//                .antMatchers("/api/users").hasAnyAuthority("ADMIN","BOOKKEEPER","EMPLOYEE")
                .antMatchers("/customers").hasAnyAuthority("ADMIN", "BOOKKEEPER", "EMPLOYEE")
                .antMatchers(HttpMethod.POST, "/customers").hasAnyAuthority("ADMIN", "BOOKKEEPER")
                .antMatchers("/customers/{id}").hasAnyAuthority("ADMIN", "BOOKKEEPER", "EMPLOYEE")
                .antMatchers(HttpMethod.PUT, "/customers").hasAnyAuthority("ADMIN", "BOOKKEEPER")
                .antMatchers(HttpMethod.POST, "/customers/{id}/addproject").hasAnyAuthority("ADMIN", "BOOKKEEPER")
                .antMatchers(HttpMethod.DELETE, "/customers/{id}").hasAnyAuthority("ADMIN", "BOOKKEEPER")
                .antMatchers("/projects").hasAnyAuthority("EMPLOYEE", "ADMIN", "BOOKKEEPER")
                .antMatchers("/projects/{id}").hasAnyAuthority("BOOKKEEPER", "ADMIN", "EMPLOYEE")
                .antMatchers(HttpMethod.PUT, "/projects/{id}").hasAnyAuthority("ADMIN", "BOOKKEEPER")

//                .antMatchers(HttpMethod.POST, "/api/users/").hasAnyAuthority("ADMIN")
//                .antMatchers("/api/users/current-user").hasAnyAuthority("ADMIN", "EMPLOYEE", "BOOKKEEPER")
//                .antMatchers("/api/users/current-user/update").hasAnyAuthority("ADMIN", "BOOKKEEPER", "EMPLOYEE")
//                .antMatchers(HttpMethod.DELETE, "/api/users/{id}").hasAnyAuthority("ADMIN")
//                .antMatchers("/api/users/reactiv/{id}").hasAnyAuthority("ADMIN")
//                .antMatchers("/api/users/deactiv/{id}").hasAnyAuthority("ADMIN")
//                .antMatchers("/api/users/updaterole/{id}").hasAnyAuthority("ADMIN")
                .antMatchers("/api/timetable/actualhours/{id}").hasAnyAuthority("ADMIN", "BOOKKEEPER", "EMPLOYEE")
//                .antMatchers("/api/users/register/timetable").hasAnyAuthority("EMPLOYEE")
                .antMatchers("/api/invoice/export/excel").hasAnyAuthority("BOOKKEEPER")
                .antMatchers("/api/timetable/hours/employees/{id}").hasAnyAuthority("BOOKKEEPER")
                .antMatchers("/api/timetable/assignEmployee/**").hasAnyAuthority("ADMIN", "BOOKKEEPER", "EMPLOYEE")
                .antMatchers("/api/timetable/assignProject/**").hasAnyAuthority("ADMIN", "BOOKKEEPER", "EMPLOYEE")
                .antMatchers("/api/timetable/OvertimeByEmail/{email})").hasAnyAuthority("ADMIN", "BOOKKEEPER", "EMPLOYEE")
                .antMatchers("/api/invoice/export/pdf").hasAnyAuthority("BOOKKEEPER")
                .antMatchers("/api/invoice/export/xml").hasAnyAuthority("BOOKKEEPER")
                .antMatchers("/api/timetable/overhours/{id}").hasAnyAuthority("ADMIN", "BOOKKEEPER", "EMPLOYEE")

                .antMatchers("/absence/holiday/employee/{id}").hasAnyAuthority("BOOKKEEPER")
                .antMatchers("/employees/status/holiday").hasAnyAuthority("BOOKKEEPER")
                .antMatchers("/employees/status/sick").hasAnyAuthority("BOOKKEEPER")
                .antMatchers("/absence/sick/employee/{id}").hasAnyAuthority("BOOKKEEPER")
                .antMatchers("/apply/holiday/{id}").hasAnyAuthority("BOOKEERPER", "EMPLOYEE", "ADMIN")
                .antMatchers("/holiday/decline/employee/{id}").hasAnyAuthority("BOOKKEERPER", "ADMIN")
                .antMatchers("/holidays").hasAnyAuthority("EMPLOYEE", "BOOKKEEPER", "ADMIN")
                .antMatchers(HttpMethod.POST, "/api/timetableDays").hasAnyAuthority("EMPLOYEE", "BOOKKEEPER", "ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin().permitAll()
                .and()
                .logout().permitAll().and().httpBasic()
                .and().csrf().disable();

    }
}
