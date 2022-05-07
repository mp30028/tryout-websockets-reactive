package com.zonesoft.tryout.websockets.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.zonesoft.tryout.websockets.entities.Profile;

public interface ProfileRepository extends ReactiveMongoRepository<Profile, String> {
}
