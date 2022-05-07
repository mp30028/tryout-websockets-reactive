package com.zonesoft.tryout.websockets.events.publishers;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import com.zonesoft.tryout.websockets.events.ProfileDataEvent;

import reactor.core.publisher.FluxSink;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

@Component
public class ProfileDataEventPublisher implements
    ApplicationListener<ProfileDataEvent>,
    Consumer<FluxSink<ProfileDataEvent>> {

    private final Executor executor;
    private final BlockingQueue<ProfileDataEvent> queue =
        new LinkedBlockingQueue<>();

    ProfileDataEventPublisher(Executor executor) {
        this.executor = executor;
    }


    @Override
    public void onApplicationEvent(ProfileDataEvent event) {
        this.queue.offer(event);
    }

     @Override
    public void accept(FluxSink<ProfileDataEvent> sink) {
        this.executor.execute(() -> {
            while (true)
                try {
                	ProfileDataEvent event = queue.take();
                    sink.next(event);
                }
                catch (InterruptedException e) {
                    ReflectionUtils.rethrowRuntimeException(e);
                }
        });
    }
}
