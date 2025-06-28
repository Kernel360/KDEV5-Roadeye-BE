package org.re.car;

import org.re.car.domain.Car;
import org.re.car.domain.CarProfile;
import org.re.company.CompanyFixture;
import org.re.company.domain.Company;

import java.util.List;
import java.util.stream.IntStream;

public class CarFixture {
    public static Car create() {
        var company = CompanyFixture.create();
        var carName = "name";
        return CarFixture.create(company, carName);
    }

    public static Car create(Company company) {
        var carName = "name";
        return CarFixture.create(company, carName);
    }

    public static Car create(Company company, String carName) {
        var profile = new CarProfile(carName, "123가123", "imageUrl");
        return Car.of(company, profile);
    }

    public static List<Car> createList(int size) {
        return IntStream
            .range(0, size)
            .mapToObj((c) -> CarFixture.create())
            .toList();
    }
}
