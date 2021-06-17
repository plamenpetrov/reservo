package com.pp.reservo.unit.factories;

import com.github.javafaker.Faker;
import com.pp.reservo.domain.entities.Appointment;
import com.pp.reservo.domain.entities.Client;
import com.pp.reservo.domain.entities.Employee;
import com.pp.reservo.domain.entities.Reservation;
import com.pp.reservo.domain.repositories.AppointmentRepository;
import com.pp.reservo.domain.repositories.ClientRepository;
import com.pp.reservo.domain.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class ReservationFactory {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public Reservation create() {
        Faker faker = new Faker();

        Reservation reservation = new Reservation();

        reservation.setId(faker.number().numberBetween(1, 10000));

        EmployeeFactory employeeFactory = new EmployeeFactory();
        Employee employee = employeeFactory.create();
        Employee employeeDB = employeeRepository.save(employee);
        employee.setId(employeeDB.getId());
        reservation.setEmployee(employee);

        AppointmentFactory appointmentFactory = new AppointmentFactory();
        Appointment appointment = appointmentFactory.create();
        Appointment appointmentDB = appointmentRepository.save(appointment);
        appointment.setId(appointmentDB.getId());
        reservation.setAppointment(appointment);

        ClientFactory clientFactory = new ClientFactory();
        Client client = clientFactory.create();
        Client clientDB = clientRepository.save(client);
        client.setId(clientDB.getId());
        reservation.setClient(client);

        reservation.setDuration(faker.number().numberBetween(1, 1000));

        reservation.setStartAt(new Timestamp(System.currentTimeMillis()));
        reservation.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        return reservation;
    }
}
