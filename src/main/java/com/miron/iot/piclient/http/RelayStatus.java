package com.miron.iot.piclient.http;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class RelayStatus {
    private int relayNumber;
    private boolean on;
    private LocalDateTime timeStamp;

    public RelayStatus() {
    }

    public RelayStatus(int relayNumber, boolean on) {
        this.relayNumber = relayNumber;
        this.on = on;
        this.timeStamp = LocalDateTime.now();
    }

    public int getRelayNumber() {
        return relayNumber;
    }

    public boolean isOn() {
        return on;
    }

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }
}


