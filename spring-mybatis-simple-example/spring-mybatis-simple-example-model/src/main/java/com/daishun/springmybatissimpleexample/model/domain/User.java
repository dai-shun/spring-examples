package com.daishun.springmybatissimpleexample.model.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "`user`")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
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
}