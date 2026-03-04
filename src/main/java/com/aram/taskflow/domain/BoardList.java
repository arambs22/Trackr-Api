package com.aram.taskflow.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "board_lists")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class BoardList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    // Para ordenar columnas: 0,1,2...
    @Column(nullable = false)
    private Integer position;

    @ManyToOne(optional = false)
    @JoinColumn(name = "board_id")
    private Board board;
}