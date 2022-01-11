package de.example.haegertime.users;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class UserConfig {
    /*
    @Bean
    CommandLineRunner clrUsers(UserRepository userRepository){
        return  args -> {
            User usr1 = new User("Anton", "Aus Tirol", "1234567", "martin.stoller2@gmx.de", Role.EMPLOYEE);
            User usr2 = new User("Johanna", "Hagelücken", "abcdefg", "jolu@gmx.net", Role.BOOKKEEPER);
            User usr3 = new User("Nick", "Petersen", "alarm1", "stoller.martin@gmx.de", Role.ADMIN);
            User usr4 = new User("Albert", "Gartenzwerg", "alarm1", "gartenzwergl@gmail.com", Role.ADMIN);
            User usr5 = new User("Admiral", "Schneider", "fussball95", "spamfilter@gmail.com", Role.EMPLOYEE);
            User usr6 = new User("Franziska", "Frankenstein", "123Bergsteiger", "frafra@gmx.de", Role.BOOKKEEPER);
            User usr7 = new User("Waldo", "Holzkopf", "redbull!", "holzkopf@gmx.net", Role.EMPLOYEE);
            userRepository.saveAll(List.of(usr1, usr2, usr3, usr4, usr5, usr6, usr7));
        };
    }
    */

}
