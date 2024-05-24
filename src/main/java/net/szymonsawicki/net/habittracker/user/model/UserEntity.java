package net.szymonsawicki.net.habittracker.user.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import net.szymonsawicki.net.habittracker.user.type.UserType;

@Entity
@Table(name = "users")
@Getter
@Setter
public class UserEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String username;
  private String password;
  private UserType userType;
}