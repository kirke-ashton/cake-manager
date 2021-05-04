package com.waracle.cakemanager.authorisation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.access.event.AbstractAuthorizationEvent;
import org.springframework.security.access.event.AuthorizationFailureEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthorisationListenerService implements ApplicationListener<AbstractAuthorizationEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorisationListenerService.class);

    @Override
    public void onApplicationEvent(AbstractAuthorizationEvent authorizationEvent) {
        if (!(authorizationEvent instanceof AuthorizationFailureEvent)) {
            // We are only interested in failed authorization attempts
            return;
        }
        try {
            Authentication authentication = ((AuthorizationFailureEvent) authorizationEvent).getAuthentication();

            //only log failed authentication attempts
            if (authentication.isAuthenticated())
                return;
        } catch (Exception e){
            LOGGER.error("Exception caught trying to log failed authorisation event", e);
        }
    }
}