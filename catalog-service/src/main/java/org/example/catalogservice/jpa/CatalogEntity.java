package org.example.catalogservice.jpa;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.util.Date;

import static jakarta.persistence.GenerationType.*;

@Data
@Entity
@Table(name = "catalog")
public class CatalogEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120, unique = true)
    private String productId;
    @Column(nullable = false)
    private String productName;
    @Column(nullable = false)
    private Integer stock;
    @Column(nullable = false)
    private Integer unitPrice;

    @Column(nullable = false, updatable = false, insertable = false) //생성일자는 변경 불가
    @ColumnDefault(value = "CURRENT_TIMESTAMP")
    private Date createdAt;
}
