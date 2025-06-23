package org.re.car.dto;

public class CarCreationCommandFixture {
    public static CarCreationCommand create() {
        var carName = "Test Car";
        return create(carName);
    }

    public static CarCreationCommand create(String carName) {
        var carLicenseNumber = "123가 4567";
        var carImageUrl = "http://example.com/car.jpg";
        var carMileageInitial = 0;
        return new CarCreationCommand(
            carName,
            carLicenseNumber,
            carImageUrl,
            carMileageInitial
        );
    }
}
