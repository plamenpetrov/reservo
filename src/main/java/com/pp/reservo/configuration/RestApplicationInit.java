package com.pp.reservo.configuration;

import com.pp.reservo.domain.entities.Appointment;
import com.pp.reservo.domain.entities.Client;
import com.pp.reservo.domain.entities.Employee;
import com.pp.reservo.domain.entities.Reservation;
import com.pp.reservo.domain.repositories.AppointmentRepository;
import com.pp.reservo.domain.repositories.ClientRepository;
import com.pp.reservo.domain.repositories.EmployeeRepository;
import com.pp.reservo.domain.repositories.ReservationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;

@Component
public class RestApplicationInit implements CommandLineRunner {

    private final ClientRepository clientRepository;
    private final AppointmentRepository appointmentRepository;
    private final EmployeeRepository employeeRepository;
    private final ReservationRepository reservationRepository;

    RestApplicationInit(ClientRepository clientRepository,
                        AppointmentRepository appointmentRepository,
                        EmployeeRepository employeeRepository,
                        ReservationRepository reservationRepository
    ) {
        this.clientRepository = clientRepository;
        this.appointmentRepository = appointmentRepository;
        this.employeeRepository = employeeRepository;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        ArrayList<Reservation> reservationArr = new ArrayList<>();
        reservationArr.add(new Reservation());

        Client client = new Client(null, "Client one", new Timestamp(System.currentTimeMillis()), reservationArr);
        clientRepository.save(client);

        Client client2 = new Client(null, "Client two", new Timestamp(System.currentTimeMillis()), reservationArr);
        clientRepository.save(client2);

        Client client3 = new Client(null, "Client three", new Timestamp(System.currentTimeMillis()), reservationArr);
        clientRepository.save(client3);

        Appointment appointment = new Appointment(null, "Haircut", 20, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), reservationArr);
        Appointment appointment2 = new Appointment(null, "Manicure", 120, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), reservationArr);
        Appointment appointment3 = new Appointment(null, "Pedicure", 60, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), reservationArr);

        appointmentRepository.save(appointment);
        appointmentRepository.save(appointment2);
        appointmentRepository.save(appointment3);

        Employee employee = new Employee(null, "Employee one", true, new Timestamp(System.currentTimeMillis()), reservationArr);
        Employee employee2 = new Employee(null, "Employee two", false, new Timestamp(System.currentTimeMillis()), reservationArr);
        Employee employee3 = new Employee(null, "Employee three", true, new Timestamp(System.currentTimeMillis()), reservationArr);

        employeeRepository.save(employee);
        employeeRepository.save(employee2);
        employeeRepository.save(employee3);

        Reservation reservation = new Reservation(null, employee, appointment, client, appointment.getDuration(), new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));
        Reservation reservation2 = new Reservation(null, employee2, appointment2, client2, appointment2.getDuration(), new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));
        Reservation reservation3 = new Reservation(null, employee3, appointment3, client, appointment3.getDuration(), new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));

        reservationRepository.save(reservation);
        reservationRepository.save(reservation2);
        reservationRepository.save(reservation3);
    }
}
