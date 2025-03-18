package com.kcjs.cloud.mysql.pojo;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@Table(name = "storage")
public class Storage  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;

    private Integer total;

    private Integer used;

    private Integer residue;
}