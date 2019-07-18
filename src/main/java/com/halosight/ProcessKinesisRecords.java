package com.halosight;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.KinesisEvent;

public class ProcessKinesisRecords implements RequestHandler<KinesisEvent, Void> {

    @Override
    public Void handleRequest(KinesisEvent event, Context context) {
        event.getRecords()
                .stream()
                .forEach(r -> System.out.println(new String(r.getKinesis().getData().array())));
        return null;
    }
}
