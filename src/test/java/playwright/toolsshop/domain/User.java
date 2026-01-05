package playwright.toolsshop.domain;

import net.datafaker.Faker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public record User(String first_name,
                   String last_name,
                   String email,
                   Address address,
                   String password,
                   String phone,
                   String dob
) {
    public static record Address(String street,
                                 String city,
                                 String state,
                                 String country,
                                 String postal_code) {
    }

    public static Address randomAddress() {
        Faker faker = new Faker();
        return new Address(
                faker.address().streetAddress(),
                faker.address().city(),
                faker.address().state(),
                faker.address().country(),
                faker.address().postcode());
    }

    public static User randomUser() {
        Faker faker = new Faker();
        int year = faker.number().numberBetween(1900, 2000);
        int month = faker.number().numberBetween(1, 12);
        int day = faker.number().numberBetween(1, 28);
        LocalDate dob = LocalDate.of(year, month, day);
        String formattedDate = dob.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return new User(
                faker.name().firstName(),
                faker.name().lastName(),
                faker.internet().emailAddress(),
                randomAddress(),
                "SuperSecure@123",
                faker.phoneNumber().cellPhone(),
                formattedDate
        );
    }

    public User withPassword(String password) {
        return new User(first_name, last_name, email, address, password, phone, dob);
    }
}
