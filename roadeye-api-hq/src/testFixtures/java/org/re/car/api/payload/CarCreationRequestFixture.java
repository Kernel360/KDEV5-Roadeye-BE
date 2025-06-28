package org.re.car.api.payload;

public class CarCreationRequestFixture {
    public static CarCreationRequest create() {
        var name = "Test Car";
        var licensePlate = "TEST-1234";
        var imgUrl = "https://example.com/car.jpg";
        var mileageInitial = 0;
        return new CarCreationRequest(
            name,
            licensePlate,
            imgUrl,
            mileageInitial
        );
    }
}
