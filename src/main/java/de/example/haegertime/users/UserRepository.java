package de.example.haegertime.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.last like %:keyword% OR u.first like %:keyword% OR u.email like %:keyword%")
    List<User> findByKeyword(@Param("keyword") String keyword);

}
