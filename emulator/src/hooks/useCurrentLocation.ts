import { useEffect, useState } from "react";

export default function useCurrentLocation() {
    const [location, setLocation] = useState<{ lat: number, lon: number } | null>(null);

    useEffect(() => {
        navigator.geolocation.getCurrentPosition((position) => {
            setLocation({ lat: position.coords.latitude, lon: position.coords.longitude });
        });
    }, []);

    return location;
}