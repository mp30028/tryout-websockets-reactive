package com.zonesoft.tryout.websockets.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zonesoft.tryout.websockets.entities.Profile;

public class ProfileDataEventSource {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProfileDataEventSource.class);
	private final Profile profile;
	private final ProfileDataEventType eventType;
	
	public ProfileDataEventSource(ProfileDataEventType eventType, Profile profile) {
		this.profile = profile;
		this.eventType = eventType;
	}

	public ProfileDataEventType getEventType() {
		return eventType;
	}

	public Profile getProfile() {
		return profile;
	}
	
	@Override
	public String toString() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.findAndRegisterModules();
		String json = null;
		try {
			json = objectMapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			String message = "<EXCEPTION - whilst writing ProfileDataEventSource Object to JSON. " + e.getLocalizedMessage() + ">" ;
			LOGGER.error(message);
			return message;
		}
		return json;		
	}
}
