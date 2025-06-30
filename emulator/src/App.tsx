import { Routes, Route } from 'react-router-dom'
import VehicleSimulator from './routes/emulator'
import { useEffect } from 'react'
import { emulateCarPath } from './lib/emulator'

function App() {
    useEffect(() => {
        emulateCarPath({
            start: { lat: 37.499225, lng: 127.031477 },
            end: { lat: 37.495591, lng: 127.019962 },
            initSpdKmh: 10,
            maxSpdKmh: 80,
            acc: 5,
        }).next()
            .then((iter) => {
                while (!iter.done) {
                    const point = iter.value;
                    console.log(point);
                }
            })
    }, [])

    return (
        <Routes>
            <Route path="/routes/emulator" element={<VehicleSimulator />} />
            <Route path="/" element={
                <div className="p-8">
                    <h1 className="text-2xl font-bold mb-4">Roadeye Emulator</h1>
                    <div className="space-y-2">
                        <div><a href="/routes/emulator" className="text-blue-600 hover:underline">시뮬레이터로 이동</a></div>
                    </div>
                </div>
            } />
        </Routes>
    )
}

export default App
