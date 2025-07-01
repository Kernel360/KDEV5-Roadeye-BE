import { useEffect } from 'react';
import { Navigate, useNavigate } from 'react-router-dom';
import useAuth from '~/hooks/useAuth';

interface ProtectedRouteProps {
    children: React.ReactNode;
}

export default function ProtectedRoute({ children }: ProtectedRouteProps) {
    const { isAuthenticated, isInitialized, checkSession } = useAuth();
    const navigate = useNavigate();

    useEffect(() => {
        if (!isInitialized) {
            checkSession()
                .catch(() => {
                    navigate('/login');
                })
        }
    }, [isInitialized, checkSession, navigate]);

    if (!isAuthenticated) {
        return <Navigate to="/login" replace />;
    }

    return <>{children}</>;
} 