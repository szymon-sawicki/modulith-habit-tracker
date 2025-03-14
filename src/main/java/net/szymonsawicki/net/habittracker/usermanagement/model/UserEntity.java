package net.szymonsawicki.net.habittracker.usermanagement.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.szymonsawicki.net.habittracker.usermanagement.UserType;

@Entity
@Table(name = "users", schema = "tracker_user")
@Getter
@Setter
@ToString
public class UserEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String username;
  private String password;
  private UserType userType;
}
