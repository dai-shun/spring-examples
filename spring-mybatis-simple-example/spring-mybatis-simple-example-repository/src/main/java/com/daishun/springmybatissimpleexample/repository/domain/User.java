package com.daishun.springmybatissimpleexample.repository.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

@Data
@Table(name = "`user`")
public class User implements Serializable {
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "`name`")
    private String name;

    @Column(name = "`password`")
    private String password;

    @Column(name = "`created_at`")
    private Date createdAt;

    @Column(name = "`updated_at`")
    private Date updatedAt;

    private static final long serialVersionUID = 1L;
}