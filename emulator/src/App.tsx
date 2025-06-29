import { Routes, Route } from 'react-router-dom'
import VehicleSimulator from './routes/emulator'

function App() {
    return (
        <div>
            <Routes>
                <Route path="/routes/emulator" element={<VehicleSimulator />} />
                <Route path="/" element={<div>홈페이지입니다. <a href="/routes/emulator">시뮬레이터로 이동</a></div>} />
            </Routes>
        </div>
    )
}

export default App
