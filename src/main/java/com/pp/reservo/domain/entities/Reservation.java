package com.pp.reservo.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Required;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.sql.Timestamp;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    @Column(name = "employe_id", nullable = false)
    private Integer employeId;

    @Column(name = "appointment_id", nullable = false)
    private Integer appointmentId;

//    @Column(name = "client_id", nullable = false)
//    private Integer clientId;

    @Column(name = "duration", nullable = false)
    @Min(0)
    private Integer duration;

    @Column(name = "start_at", nullable = false)
    private Timestamp startAt;

    @Column(name = "created_at", nullable = false, columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="client_id", nullable = false)
    private Client client;

}
