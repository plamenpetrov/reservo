package com.pp.reservo.unit.factories;

import com.github.javafaker.Faker;
import com.pp.reservo.domain.entities.Employee;
import com.pp.reservo.domain.entities.Reservation;

import java.sql.Timestamp;
import java.util.ArrayList;

public class EmployeeFactory {

    public static Employee create() {
        ArrayList<Reservation> reservationArr = new ArrayList<>();
        reservationArr.add(new Reservation());

        Faker faker = new Faker();

        Employee employee = new Employee();

//        employee.setId(faker.number().numberBetween(1, 10000));
        employee.setName(faker.name().firstName());
        employee.setActive(true);
        employee.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        employee.setReservations(reservationArr);

        return employee;
    }
}
