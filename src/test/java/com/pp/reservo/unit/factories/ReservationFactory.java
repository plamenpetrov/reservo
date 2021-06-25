package com.pp.reservo.unit.factories;

import com.github.javafaker.Faker;
import com.pp.reservo.domain.entities.Appointment;
import com.pp.reservo.domain.entities.Client;
import com.pp.reservo.domain.entities.Employee;
import com.pp.reservo.domain.entities.Reservation;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class ReservationFactory {

    public static Reservation create() {
        Faker faker = new Faker();

        Reservation reservation = new Reservation();

        reservation.setId(faker.number().numberBetween(1, 10000));

        Employee employee = EmployeeFactory.create();
        reservation.setEmployee(employee);

        Appointment appointment = AppointmentFactory.create();
        reservation.setAppointment(appointment);

        Client client = ClientFactory.create();
        reservation.setClient(client);

        reservation.setDuration(faker.number().numberBetween(1, 1000));

        reservation.setStartAt(new Timestamp(System.currentTimeMillis()));
        reservation.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        return reservation;
    }
}
