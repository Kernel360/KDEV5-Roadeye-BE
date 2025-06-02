package org.re.hq.car.dto;

public class CarCreationCommandFixture {
    public static CarCreationCommand create() {
        var carName = "Test Car";
        var carLicenseNumber = "123가 4567";
        var carImageUrl = "http://example.com/car.jpg";
        var carMileageInitial = 10000;
        return new CarCreationCommand(
            carName,
            carLicenseNumber,
            carImageUrl,
            carMileageInitial
        );
    }
}
