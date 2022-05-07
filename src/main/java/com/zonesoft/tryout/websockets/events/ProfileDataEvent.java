package com.zonesoft.tryout.websockets.events;

import org.springframework.context.ApplicationEvent;

import com.zonesoft.tryout.websockets.entities.Profile;

public class ProfileDataEvent extends ApplicationEvent {

	private static final long serialVersionUID = 7121033441429890681L;
	
	public ProfileDataEvent(ProfileDataEventSource source) {
        super(source);
    }
}
