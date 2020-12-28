package htwb.ai.beleg.repository;

import htwb.ai.beleg.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

}

