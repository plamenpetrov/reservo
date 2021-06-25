package com.pp.reservo.unit.factories;

import com.github.javafaker.Faker;
import com.pp.reservo.domain.entities.Appointment;
import com.pp.reservo.domain.entities.Reservation;

import java.sql.Timestamp;
import java.util.ArrayList;

public class AppointmentFactory {

    public static Appointment create() {
        ArrayList<Reservation> reservationArr = new ArrayList<>();
        reservationArr.add(new Reservation());

        Faker faker = new Faker();

        Appointment appointment = new Appointment();

//        appointment.setId(faker.number().numberBetween(1, 10000));
        appointment.setName(faker.name().firstName());
        appointment.setDuration(faker.number().numberBetween(1, 1000));
        appointment.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        appointment.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        appointment.setReservations(reservationArr);

        return appointment;
    }
}
