import { Routes, Route } from 'react-router-dom'
import VehicleSimulator from './routes/emulator'
import GraphHopperDemo from './routes/graphhopper-demo'

function App() {
    return (
        <Routes>
            <Route path="/routes/emulator" element={<VehicleSimulator />} />
            <Route path="/routes/graphhopper-demo" element={<GraphHopperDemo />} />
            <Route path="/" element={
                <div className="p-8">
                    <h1 className="text-2xl font-bold mb-4">Roadeye Emulator</h1>
                    <div className="space-y-2">
                        <div><a href="/routes/emulator" className="text-blue-600 hover:underline">시뮬레이터로 이동</a></div>
                        <div><a href="/routes/graphhopper-demo" className="text-blue-600 hover:underline">GraphHopper 길찾기 데모</a></div>
                    </div>
                </div>
            } />
        </Routes>
    )
}

export default App
