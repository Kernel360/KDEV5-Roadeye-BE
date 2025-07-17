import CarInfo from "./emulator/CarInfo"
import EmulatorMap from "./emulator/EmulatorMap"
import SideBar from "./emulator/Sidebar"
import RouteAPI from "./emulator/RouteAPI"

function Emulator() {
    return (
        <>
            <div className="flex w-screen h-screen gap-2 p-2">
                <SideBar />
                <div className="flex-1 flex flex-row gap-2">
                    <CarInfo />
                    <EmulatorMap />
                </div>
            </div>
            <RouteAPI />
        </>
    )
}

export default Emulator;