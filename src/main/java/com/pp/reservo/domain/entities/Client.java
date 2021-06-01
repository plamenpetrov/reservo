package com.pp.reservo.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.sql.Timestamp;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @Column(name = "name", nullable = false)
    @NotEmpty(message = "Name is required and can not be empty")
    @Length(min = 2, max = 255, message = "Name should be between 2 and 255 characters")
    private String name;

    @Column(name = "created_at", nullable = false, columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;

//    @JsonIgnore
//    @OneToMany(mappedBy = "client",
//            cascade = CascadeType.ALL,
//            fetch = FetchType.EAGER)
//    private List<Reservation> reservationList = new ArrayList<>();
}
