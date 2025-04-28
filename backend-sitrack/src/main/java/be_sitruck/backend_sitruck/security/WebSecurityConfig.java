package be_sitruck.backend_sitruck.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import be_sitruck.backend_sitruck.security.jwt.JwtTokenFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    @Bean
    @Order(1)
    public SecurityFilterChain jwtFilterChain(HttpSecurity http) throws Exception {
        http.cors().and().securityMatcher("/api/**")
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(requests -> requests
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/user/**").hasAuthority("Admin")

                .requestMatchers("/api/chassis/all").hasAnyAuthority("Operasional", "Admin", "Manager", "Supervisor")
                .requestMatchers("/api/chassis/detail/**").hasAnyAuthority("Operasional", "Admin", "Manager", "Supervisor")
                .requestMatchers("/api/chassis/**").hasAnyAuthority("Admin", "Manager", "Supervisor")

                .requestMatchers("/api/customer/all").hasAnyAuthority("Operasional", "Admin", "Manager", "Supervisor")
                .requestMatchers("/api/customer/detail/**").hasAnyAuthority("Operasional", "Admin", "Manager", "Supervisor")
                .requestMatchers("/api/customer/**").hasAnyAuthority("Admin", "Supervisor", "Manager")

                .requestMatchers("/api/sopir/all").hasAnyAuthority("Operasional", "Admin", "Manager", "Supervisor")
                .requestMatchers("/api/sopir/detail/**").hasAnyAuthority("Operasional", "Admin", "Manager", "Supervisor")
                .requestMatchers("/api/sopir/**").hasAnyAuthority("Admin", "Supervisor", "Manager")

                .requestMatchers("/api/notifications/**").hasAnyAuthority("Admin", "Supervisor", "Manager", "Operasional", "Mekanik")

                .requestMatchers("/api/truck/all").hasAnyAuthority("Operasional", "Admin", "Manager", "Supervisor")
                .requestMatchers("/api/truck/detail/**").hasAnyAuthority("Operasional", "Admin", "Manager", "Supervisor")
                .requestMatchers("/api/truck/**").hasAnyAuthority("Admin","Supervisor","Manager")

                .requestMatchers("/api/order/all").hasAnyAuthority("Operasional", "Admin", "Manager", "Supervisor")
                .requestMatchers("/api/order/detail/**").hasAnyAuthority("Operasional", "Admin", "Manager", "Supervisor")
                .requestMatchers("/api/order/update/**").hasAnyAuthority("Operasional", "Admin", "Manager", "Supervisor")
                .requestMatchers("/api/order/**").hasAnyAuthority("Admin","Supervisor","Manager")

                .requestMatchers("/api/order/all").hasAuthority("Operasional")
                .requestMatchers("/api/order/detail/**").hasAuthority("Operasional")
                .requestMatchers("/api/order/update/**").hasAuthority("Operasional")

                .requestMatchers("/api/spj/add").hasAnyAuthority("Operasional", "Admin", "Manager", "Supervisor")
                .requestMatchers("/api/spj/vehicle-out").hasAnyAuthority("Operasional", "Admin", "Manager", "Supervisor")
                .requestMatchers("/api/spj/vehicle-in").hasAnyAuthority("Operasional", "Admin", "Manager", "Supervisor")
                .requestMatchers("/api/spj/detail/**").hasAnyAuthority("Operasional", "Admin", "Manager", "Supervisor")
                .requestMatchers("/api/spj/update/**").hasAnyAuthority("Operasional", "Admin", "Manager", "Supervisor")
                .requestMatchers("/api/spj/**").hasAnyAuthority("Admin","Supervisor","Manager")

                .requestMatchers("/api/asset/**").hasAnyAuthority("Admin","Supervisor","Manager","Mekanik")
                .requestMatchers("/api/asset/add").hasAnyAuthority("Admin","Supervisor","Manager")
                .requestMatchers("/api/asset/update/**").hasAnyAuthority("Admin","Supervisor","Manager")

                .requestMatchers("/api/request-assets/**").hasAnyAuthority("Admin","Supervisor","Manager","Mekanik")
                .requestMatchers("/api/request-assets/approve/**").hasAnyAuthority("Admin","Supervisor","Manager")
                
                .anyRequest().authenticated()    
            )
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(e -> e
                .authenticationEntryPoint(
                    new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)
                )
                .accessDeniedHandler(
                    new AccessDeniedHandler() {
                        @Override
                        public void handle(HttpServletRequest request, HttpServletResponse response,
                                AccessDeniedException accessDeniedException) throws IOException, ServletException {
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.getWriter().write("Anda Tidak Memiliki Akses ke Endpoint Ini!");
                        }
                    }
                )
            );
                    
        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception{
        http.csrf(Customizer.withDefaults())
            .authorizeHttpRequests (requests -> requests
                .requestMatchers (new AntPathRequestMatcher("/css/**")).permitAll() 
                .requestMatchers (new AntPathRequestMatcher("/js/**")).permitAll()
                .anyRequest().authenticated()
            )   
            .formLogin((form) -> form
                .loginPage("/login")
                .permitAll()
                .defaultSuccessUrl("/")
            )
            .logout((logout) -> logout.logoutRequestMatcher(new AntPathRequestMatcher( "/logout")) 
                    .logoutSuccessUrl("/login"));

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
    
    @Bean
    public BCryptPasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }   

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userDetailsService).passwordEncoder(encoder());
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("https://sitrack.up.railway.app");
        config.addAllowedOriginPattern("http://localhost:*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}