package dev.emir.DrivingSchoolManagementInformation.security;

import dev.emir.DrivingSchoolManagementInformation.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
// Design Pattern: Singleton Pattern (Spring BEAN)
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf().disable()
                .authorizeHttpRequests(auth -> auth
                        // 1) kimliksizlik gerektirenler
                        .requestMatchers("/api/auth/**", "/api/student/register").permitAll()

                        // 2) COURSE-SESSIONS: OKUNABİLİR HER ROL İÇİN
                        .requestMatchers(HttpMethod.GET, "/api/course-sessions", "/api/course-sessions/*")
                        .hasAnyRole("ADMIN","EMPLOYEE","INSTRUCTOR","STUDENT")

                        // 3) SADECE EĞİTMENİN KENDİ OTURUMLARINA
                        .requestMatchers(HttpMethod.GET, "/api/course-sessions/my")
                        .hasRole("INSTRUCTOR")

                        // 4) OLUŞTUR/GÜNCELLE/SİL
                        .requestMatchers(HttpMethod.POST, "/api/course-sessions").hasAnyRole("ADMIN","EMPLOYEE")
                        .requestMatchers(HttpMethod.PUT, "/api/course-sessions/*").hasAnyRole("ADMIN","EMPLOYEE")
                        .requestMatchers(HttpMethod.DELETE, "/api/course-sessions/*").hasAnyRole("ADMIN","EMPLOYEE")

                        // 5) ÖĞRENCİ ATAMA/ÇIKARMA
                        .requestMatchers(HttpMethod.POST, "/api/course-sessions/*/students/*").hasAnyRole("ADMIN","EMPLOYEE")
                        .requestMatchers(HttpMethod.DELETE, "/api/course-sessions/*/students/*").hasAnyRole("ADMIN","EMPLOYEE")

                        // 6) ATANMAMIŞ ÖĞRENCİLERİ GETİRME
                        .requestMatchers(HttpMethod.GET, "/api/course-sessions/unassigned-students")
                        .hasAnyRole("ADMIN","EMPLOYEE")

                        // 7) Diğer endpoint'ler
                        .requestMatchers("/api/admin/**").hasAnyRole("ADMIN","EMPLOYEE")
                        .requestMatchers("/api/theoretical/**").hasAnyRole("ADMIN", "EMPLOYEE")
                        .requestMatchers("/api/student/course-sessions").hasAnyRole("STUDENT")
                        .requestMatchers("/api/student/dashboard").hasRole("STUDENT")
                        .requestMatchers("/api/term/**").hasAnyRole("ADMIN", "EMPLOYEE")
                        .requestMatchers("/api/appointments/all").hasAnyRole("ADMIN", "EMPLOYEE")
                        .requestMatchers("/api/appointments/student/**").hasAnyRole("ADMIN", "EMPLOYEE", "STUDENT")
                        .requestMatchers("/api/appointments/instructor/**").hasAnyRole("ADMIN", "EMPLOYEE", "INSTRUCTOR")
                        .requestMatchers("/api/student/{id}/details").hasAnyRole("ADMIN", "EMPLOYEE", "STUDENT")
                        .requestMatchers("/api/vehicles/**").hasAnyRole("ADMIN", "INSTRUCTOR")
                        .requestMatchers("/api/vehicle-types/**").hasAnyRole("ADMIN", "INSTRUCTOR")
                        .requestMatchers("/api/license-classes/**").hasAnyRole("ADMIN", "INSTRUCTOR")
                        .anyRequest().authenticated()
                )
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}
