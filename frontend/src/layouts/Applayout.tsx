import { Outlet, useLocation } from "react-router-dom";
import SideBar from "../components/SideBar";
import { Navbar } from "../components/Navbar";

export default function AppLayout() {
    const location = useLocation();

    //Bu pathlerde navbar gozukmemeli
    const hideNavbarRoutes = ["/login", "/"];
    const hideNavbar = hideNavbarRoutes.includes(location.pathname);

    if (hideNavbar) {
        return (
            <div className="min-h-screen bg-gray-50">
                <Outlet />
            </div>
        );
    }

    return (
        <div className="min-h-screen bg-gray-50 flex">
            <SideBar />
            <div className="flex-1 flex flex-col">
                <Navbar />
                <main className="flex-1 p-8">
                    <Outlet />
                </main>
            </div>
        </div>
    );
}
