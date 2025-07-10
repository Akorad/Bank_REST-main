package com.example.bankcards.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "cards")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "number" , nullable = false)
    private String number;                              //номер карты

    @Column(name = "owner_name",nullable = false)
    private String ownerName;                           //имя владельца

    @Column(name = "expiration_date", nullable = false)
    private String expirationDate;                      //срок действия

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CardStatus status;                          //статус карты

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;                    //дата создания

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;                                  //владелец

}
