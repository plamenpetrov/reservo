package com.pp.reservo.unit.factories;

import com.github.javafaker.Faker;
import com.pp.reservo.domain.entities.Client;
import com.pp.reservo.domain.entities.Reservation;

import java.sql.Timestamp;
import java.util.ArrayList;

public class ClientFactory {

    public static Client create() {
        ArrayList<Reservation> reservationArr = new ArrayList<>();
        reservationArr.add(new Reservation());

        Faker faker = new Faker();

        Client client = new Client();

        client.setId(faker.number().numberBetween(1, 10000));
        client.setName(faker.name().firstName());
        client.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        client.setReservations(reservationArr);

        return client;
    }
}
