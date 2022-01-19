package de.example.haegertime.users;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.parameters.P;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository underTest;

    UserRepositoryTest(UserRepository underTest) {
        this.underTest = underTest;
    }

//    @Test
//    void itShouldFindUserByEmail() {
//        String email = "martin.stoller2@gmx.de";
//        User user = new User("Anton", "Aus Tirol", "1234567", "martin.stoller2@gmx.de", Role.EMPLOYEE);
//        underTest.save(user);
//
//        assertThat(underTest.getUserByEmail(email)).isPresent();
//    }
//
//    @Test
//    void itShouldNotFindUserByEmail() {
//        //given
//        String title = "martin.stoller2@gmx.de";
//        //then
//        assertThat(underTest.getUserByEmail(title).isPresent()).isFalse();
//    }

    @Test
    void findBylastByFirstbyEmail() {
    }

    @Test
    void getUserByEmail() {
    }

    @Test
    void existsByEmail() {
    }
}
