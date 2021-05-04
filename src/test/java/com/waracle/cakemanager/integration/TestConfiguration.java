package com.waracle.cakemanager.integration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Arrays;

import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import com.waracle.cakemanager.model.User;
import com.waracle.cakemanager.model.Role;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableTransactionManagement
@Profile("waracle-integration-test")
public class TestConfiguration {

    /**
     * Users with the ROLE_GLOBAL_ADMIN role have full access to all parts of the application
     * and permission to perform all actions on all data items
     */
    public static final String ROLE_GLOBAL_ADMIN = "ROLE_GLOBAL_ADMIN";

    public static final String ROLE_GLOBAL_ACCESS = "ROLE_GLOBAL_ACCESS";

    @Bean
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(TestConfiguration.getTestUsers());
    }

    public static List<UserDetails> getTestUsers() {

        Role globalAdminRole = new Role("ROLE_GLOBAL_ADMIN","Those allowed to unrestricted access to all parts of the application", ROLE_GLOBAL_ADMIN.replaceFirst("ROLE_",""));

        User globalAdminUser = new User("global", "admin", "global.admin@waracle.io", "password", null, true);
        globalAdminUser.addRole(globalAdminRole);

        Role globalAccessRole = new Role("ROLE_GLOBAL_ACCESS","Those allowed to access only data from the application", ROLE_GLOBAL_ACCESS.replaceFirst("ROLE_",""));

        User globalAccessUser = new User("global", "access", "global.access@waracle.io", "password", null,  true);
        globalAccessUser.addRole(globalAccessRole);

        return Arrays.asList(globalAdminUser, globalAccessUser);
    }
}
