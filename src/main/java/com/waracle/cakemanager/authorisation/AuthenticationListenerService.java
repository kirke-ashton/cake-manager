package com.waracle.cakemanager.authorisation;

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AuthenticationListenerService implements ApplicationListener<AbstractAuthenticationEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationListenerService.class);

    @Override
    public void onApplicationEvent(AbstractAuthenticationEvent authenticationEvent) {
//        if (authenticationEvent instanceof InteractiveAuthenticationSuccessEvent || authenticationEvent instanceof AuthenticationSuccessEvent) {
//            // ignored to prevent duplicate logging with AuthenticationSuccessEvent
//            return;
//        }

        try {
            Authentication authentication = authenticationEvent.getAuthentication();

            //only log failed authentication attempts
            if (authentication.isAuthenticated())
                return;

        } catch (Exception e) {
            LOGGER.error("Exception caught trying to log failed authentication event", e);
        }
    }
}

