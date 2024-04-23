package com.example.bredexTest.userLogout.model;

import com.example.bredexTest.user.model.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_logouts")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserLogout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private LocalDateTime logoutTime;
}