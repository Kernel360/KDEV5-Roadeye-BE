import { useEffect } from 'react';
import { Navigate } from 'react-router-dom';
import useAuth from '~/hooks/useAuth';

interface ProtectedRouteProps {
    children: React.ReactNode;
}

export default function ProtectedRoute({ children }: ProtectedRouteProps) {
    const { isAuthenticated, isInitialized, checkSession } = useAuth();

    useEffect(() => {
        if (!isInitialized) {
            checkSession();
        }
        console.log(isInitialized);
    }, [isInitialized, checkSession]);

    if (!isAuthenticated) {
        return <Navigate to="/login" replace />;
    }

    return <>{children}</>;
} 