import { useCarList } from '~/hooks/useCarList';
import { FixedSizeList as List } from 'react-window';
import { useEffect, useState } from 'react';
import { useEmulatorStore } from '~/stores/emulatorStore';

function SideBar() {
    return (
        <div className="min-w-60 max-w-60 h-full">
            <CarListView />
        </div>
    )
}

function CarListView() {
    const { cars, isLoading, error, refetch, clearError } = useCarList();
    const [listHeight, setListHeight] = useState(400);

    useEffect(() => {
        const updateHeight = () => {
            const windowHeight = window.innerHeight;
            const headerHeight = 60; // 헤더 높이
            const padding = 40; // 여백
            setListHeight(windowHeight - headerHeight - padding);
        };

        updateHeight();
        window.addEventListener('resize', updateHeight);
        return () => window.removeEventListener('resize', updateHeight);
    }, []);

    const renderContent = () => {
        if (error) {
            return (
                <div className="p-4">
                    <div className="bg-red-50 border border-red-200 rounded-md p-4">
                        <div className="flex">
                            <div className="flex-shrink-0">
                                <svg className="h-5 w-5 text-red-400" viewBox="0 0 20 20" fill="currentColor">
                                    <path fillRule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z" clipRule="evenodd" />
                                </svg>
                            </div>
                            <div className="ml-3">
                                <h3 className="text-sm font-medium text-red-800">오류가 발생했습니다</h3>
                                <div className="mt-2 text-sm text-red-700">
                                    <p>{error}</p>
                                </div>
                                <div className="mt-4">
                                    <button
                                        onClick={() => {
                                            clearError();
                                            refetch();
                                        }}
                                        className="bg-red-100 text-red-800 px-3 py-1 rounded-md text-sm font-medium hover:bg-red-200 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500"
                                    >
                                        다시 시도
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            );
        }

        if (cars.length === 0) {
            return (
                <div className="p-4">
                    <div className="text-center py-8">
                        <svg className="mx-auto h-12 w-12 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10" />
                        </svg>
                        <h3 className="mt-2 text-sm font-medium text-gray-900">차량이 없습니다</h3>
                        <p className="mt-1 text-sm text-gray-500">등록된 차량이 없습니다.</p>
                    </div>
                </div>
            );
        }

        const ITEM_HEIGHT = 140; // 아이템 간격을 위해 높이 증가

        const CarItem = ({ index, style }: { index: number; style: React.CSSProperties }) => {
            const car = cars[index];
            const { selectedCar, setSelectedCar } = useEmulatorStore();

            useEffect(() => {
                if (cars && !cars.find(c => c.id === car.id)) {
                    setSelectedCar(null);
                }
            }, [car.id, setSelectedCar]);

            const isSelected = selectedCar?.id === car.id;

            return (
                <div style={style} className="px-4 py-2">
                    <div
                        className={`border rounded-lg p-4 hover:shadow-md transition-shadow h-full cursor-pointer ${isSelected
                            ? 'bg-blue-50 border-blue-300 shadow-md'
                            : 'bg-white border-gray-200'
                            }`}
                        onClick={() => setSelectedCar(car)}
                    >
                        <div className="flex items-center justify-between">
                            <div className="flex-1">
                                <div className="flex items-center gap-2">
                                    <h3 className="text-sm font-medium text-gray-900">
                                        {car.name || `차량 ${car.id}`}
                                    </h3>
                                    {isSelected && (
                                        <span className="inline-flex items-center px-2 py-0.5 rounded-full text-xs font-medium bg-blue-100 text-blue-800">
                                            선택됨
                                        </span>
                                    )}
                                </div>
                                {car.plateNumber && (
                                    <p className="text-sm text-gray-500 mt-1">
                                        번호판: {car.plateNumber}
                                    </p>
                                )}
                                <div className="flex items-center mt-2 text-xs text-gray-400">
                                    <svg className="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" />
                                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 11a3 3 0 11-6 0 3 3 0 016 0z" />
                                    </svg>
                                    <span>{car.latitude.toFixed(6)}, {car.longitude.toFixed(6)}</span>
                                </div>
                            </div>
                            <div className="ml-4">
                                {car.status && (
                                    <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${car.status === 'active'
                                        ? 'bg-green-100 text-green-800'
                                        : 'bg-gray-100 text-gray-800'
                                        }`}>
                                        {car.status === 'active' ? '활성' : car.status}
                                    </span>
                                )}
                            </div>
                        </div>
                    </div>
                </div>
            );
        };

        return (
            <List
                height={listHeight}
                itemCount={cars.length}
                itemSize={ITEM_HEIGHT}
                width="100%"
                className="[&::-webkit-scrollbar]:hidden [-ms-overflow-style:none] [scrollbar-width:none] gap-2"
            >
                {CarItem}
            </List>
        );
    };

    return (
        <div className="h-full flex flex-col">
            <div className="flex items-center justify-between p-4 border-b border-gray-200 bg-white">
                <h2 className="text-lg font-semibold text-gray-900">차량 목록</h2>
                <button
                    onClick={refetch}
                    className="text-sm text-blue-600 hover:text-blue-800 font-medium"
                >
                    새로고침
                </button>
            </div>

            <div className="flex-1 overflow-hidden">
                {isLoading ? (
                    <div className="flex items-center justify-center h-full">
                        <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
                        <span className="ml-2 text-gray-600">불러오는 중...</span>
                    </div>
                ) : (
                    renderContent()
                )}
            </div>
        </div>
    );
}

export default SideBar;