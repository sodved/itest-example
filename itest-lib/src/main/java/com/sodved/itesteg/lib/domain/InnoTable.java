package com.sodved.itesteg.lib.domain;

import lombok.Data;
import java.time.Instant;
import javax.persistence.*;

@Data
@Entity
@Table(name = "INNODB_SYS_TABLES", catalog = "information_schema")
public class InnoTable {

    @Id
    @Column(name="TABLE_ID")
    private Long tableId;

    @Column(name="NAME")
    private String name;

    @Column(name="FLAG")
    private Integer flag;

    @Column(name="N_COLS")
    private Integer numCols;

    @Column(name="SPACE")
    private Integer space;

    @Column(name="ROW_FORMAT")
    private String rowFormat;

    @Column(name="SPACE_TYPE")
    private String spaceType;

}
