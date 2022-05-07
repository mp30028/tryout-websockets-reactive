package com.zonesoft.tryout.websockets.services;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.zonesoft.tryout.websockets.entities.Profile;
import com.zonesoft.tryout.websockets.events.ProfileDataEvent;
import com.zonesoft.tryout.websockets.events.ProfileDataEventSource;
import com.zonesoft.tryout.websockets.events.ProfileDataEventType;
import com.zonesoft.tryout.websockets.repositories.ProfileRepository;

@Service
public class ProfileService {

    private final ApplicationEventPublisher publisher;
    private final ProfileRepository profileRepository;

    ProfileService(ApplicationEventPublisher publisher, ProfileRepository profileRepository) {
        this.publisher = publisher;
        this.profileRepository = profileRepository;
    }

    public Flux<Profile> all() {
        return this.profileRepository.findAll();
    }

    public Mono<Profile> get(String id) {
        return this.profileRepository.findById(id);
    }

    public Mono<Profile> update(String id, String email) {
        return this.profileRepository
            .findById(id)
            .map(p -> {
            	p.setEmail(email);
            	return p;
            })
            .flatMap(this.profileRepository::save)
            .doOnSuccess(profile -> {
            	ProfileDataEventSource source = new ProfileDataEventSource(ProfileDataEventType.UPDATE, profile);
            	this.publisher.publishEvent(new ProfileDataEvent(source));
            });
    }

    public Mono<Profile> delete(String id) {
        return this.profileRepository
            .findById(id)
            .flatMap(p -> this.profileRepository.deleteById(p.getId()).thenReturn(p))
        	.doOnSuccess(profile -> {
        		ProfileDataEventSource source = new ProfileDataEventSource(ProfileDataEventType.DELETE, profile);
        		this.publisher.publishEvent(new ProfileDataEvent(source));
        	});
    }

    public Mono<Profile> create(String email) {
        return this.profileRepository
            .save(new Profile(null, email))
            .doOnSuccess(profile -> {
            	ProfileDataEventSource source = new ProfileDataEventSource(ProfileDataEventType.INSERT, profile);
            	this.publisher.publishEvent(new ProfileDataEvent(source));
            });		
    }
}