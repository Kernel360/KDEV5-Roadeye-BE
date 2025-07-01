import { Routes, Route } from 'react-router-dom'
import Emulator from './routes/emulator'
import Login from './routes/login'
import ProtectedRoute from './components/ProtectedRoute'
import useAuth from './hooks/useAuth'

function App() {
    const { user, logout } = useAuth();

    return (
        <Routes>
            <Route path="/login" element={<Login />} />
            <Route path="/emulator" element={
                <ProtectedRoute>
                    <Emulator />
                </ProtectedRoute>
            } />
            <Route path="/" element={
                <ProtectedRoute>
                    <div className="p-8">
                        <div className="flex justify-between items-center mb-6">
                            <h1 className="text-2xl font-bold">Roadeye Emulator</h1>
                            {user && (
                                <div className="flex items-center space-x-4">
                                    <span className="text-gray-600">안녕하세요, {user.name}님</span>
                                    <button
                                        onClick={logout}
                                        className="px-4 py-2 bg-red-600 text-white rounded hover:bg-red-700"
                                    >
                                        로그아웃
                                    </button>
                                </div>
                            )}
                        </div>
                        <div className="space-y-2">
                            <div><a href="/emulator" className="text-blue-600 hover:underline">시뮬레이터로 이동</a></div>
                        </div>
                    </div>
                </ProtectedRoute>
            } />
        </Routes>
    )
}

export default App
