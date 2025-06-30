import { Routes, Route } from 'react-router-dom'
import Emulator from './routes/emulator'

function App() {
    return (
        <Routes>
            <Route path="/emulator" element={<Emulator />} />
            <Route path="/" element={
                <div className="p-8">
                    <h1 className="text-2xl font-bold mb-4">Roadeye Emulator</h1>
                    <div className="space-y-2">
                        <div><a href="/emulator" className="text-blue-600 hover:underline">시뮬레이터로 이동</a></div>
                    </div>
                </div>
            } />
        </Routes>
    )
}

export default App
