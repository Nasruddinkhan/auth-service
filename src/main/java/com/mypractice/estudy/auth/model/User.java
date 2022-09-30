package com.mypractice.estudy.auth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document( collection = "users")
public class User {


    @Id
    private String id;

   // @Column(nullable = false)
    private String name;


    //@Column(nullable = false)
    private String email;

    private String imageUrl;

    //@Column(nullable = false)
    private Boolean emailVerified = false;

    @JsonIgnore
    private String password;

   // @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    private String providerId;


}
