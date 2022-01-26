package de.example.haegertime.customer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository underTest;

    @Test
    void itShouldFindCustomerByName() {
        //given
        String name = "ABC";
        Customer customer = new Customer(name, "abc,xyz");
        underTest.save(customer);
        //then
        assertThat(underTest.findCustomerByName(name)).isPresent();
    }

    @Test
    void itShouldNotFindCustomerByName() {
        //given
        String name = "ABC";
        //then
        assertThat(underTest.findCustomerByName(name).isPresent()).isFalse();
    }

    @Test
    void itShouldExistingCustomerByName() {
        //given
        String name = "ABC";
        Customer customer = new Customer(name, "abc,xyz");
        underTest.save(customer);
        //then
        assertThat(underTest.existsCustomerByName(name)).isTrue();
    }

    @Test
    void itShouldNotExistingCustomerByName() {
        //given
        String name = "ABC";
        //then
        assertThat(underTest.existsCustomerByName(name)).isFalse();
    }

}