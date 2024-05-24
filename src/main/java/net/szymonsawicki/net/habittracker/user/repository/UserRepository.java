package net.szymonsawicki.net.habittracker.user.repository;

import net.szymonsawicki.net.habittracker.user.model.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Long> {}
