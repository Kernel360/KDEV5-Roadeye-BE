import React, { } from 'react';
import { Map, MapTypeControl, ZoomControl } from 'react-kakao-maps-sdk';
import useKakaoMap from '../hooks/useKakaoMap';


const GraphHopperDemo: React.FC = () => {
    useKakaoMap();

    return (
        <div className="h-full w-full bg-gray-100 p-4">
            <h1 className="text-3xl font-bold text-gray-800 mb-6">GraphHopper</h1>
            <Map
                center={{ lat: 33.5563, lng: 126.79581 }}
                style={{ width: '100%', height: '100%' }}
            >
                <MapTypeControl position={"TOPRIGHT"} />
                <ZoomControl position={"BOTTOMRIGHT"} />
            </Map>
        </div>
    );
};

export default GraphHopperDemo; 