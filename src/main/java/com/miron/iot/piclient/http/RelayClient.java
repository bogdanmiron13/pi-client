package com.miron.iot.piclient.http;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "relay", url = "${relay.url}")
public interface RelayClient {

    @RequestMapping(value = "/status/{relayNumber}", method = RequestMethod.GET)
    RelayStatus getStatus(@PathVariable("relayNumber") int relayNumber);

    @RequestMapping(value = "/status/{relayNumber}", method = RequestMethod.PUT)
    void setStatus(@PathVariable("relayNumber") final int relayNumber, @RequestBody RelayStatus relayStatus);

}