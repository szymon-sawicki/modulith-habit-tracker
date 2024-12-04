package net.szymonsawicki.net.habittracker.usermanagement.repository;

import net.szymonsawicki.net.habittracker.usermanagement.model.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
  boolean existsByUsername(String username);

  UserEntity findByUsername(String username);
}
