package kr.co.ref2.repository;

import kr.co.ref2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    public User findUserByUidAndPass(String uid, String pass);

}

