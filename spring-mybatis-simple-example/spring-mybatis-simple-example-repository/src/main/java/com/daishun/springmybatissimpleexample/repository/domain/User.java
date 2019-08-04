package com.daishun.springmybatissimpleexample.repository.domain;

import com.alibaba.fastjson.annotation.JSONField;
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
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    @Column(name = "`updated_at`")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

    private static final long serialVersionUID = 1L;
}