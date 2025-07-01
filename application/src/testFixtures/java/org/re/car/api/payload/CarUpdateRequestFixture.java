package org.re.car.api.payload;

public class CarUpdateRequestFixture {
    public static CarUpdateRequest create() {
        var name = "Updated Car Name";
        var imageUrl = "https://example.com/updated-car-image.jpg";
        return new CarUpdateRequest(
            name,
            imageUrl
        );
    }
}
