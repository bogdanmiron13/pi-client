package com.miron.iot.piclient.crud;

import com.miron.iot.piclient.model.SensorStatus;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SensorStatusCrud extends CrudRepository<SensorStatus, Long> {

    List<SensorStatus> findByTimeStampBetween(final LocalDateTime start, final LocalDateTime end);

}