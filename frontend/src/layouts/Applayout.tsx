import { Outlet, useLocation } from "react-router-dom";
import SideBar from "../components/SideBar";

export default function AppLayout() {

    const location = useLocation();

    //Bu pathlerde navbar gozukmemeli
    const hideNavbarRoutes = ["/login", "/"];
    const hideNavbar = hideNavbarRoutes.includes(location.pathname);

    return (
        <div className="flex">
            {!hideNavbar && <SideBar />}
            <div className="ml-64 w-full min-h-screen bg-gray-50 p-6">
                <Outlet />
            </div>
        </div>
    );
}
