import { useEffect, useState } from "react";

export default function useCurrentLocation() {
    const [location, setLocation] = useState<{ lat: number, lng: number } | null>(null);

    useEffect(() => {
        navigator.geolocation.getCurrentPosition((position) => {
            setLocation({ lat: position.coords.latitude, lng: position.coords.longitude });
        });
    }, []);

    return location;
}