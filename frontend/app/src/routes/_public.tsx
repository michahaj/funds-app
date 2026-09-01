import { createFileRoute, redirect, Outlet } from "@tanstack/react-router";
import { api } from "../api/axios";

export const Route = createFileRoute("/_public")({
    beforeLoad: async () => {
        let isAuthenticated = false;

        try {
            await api.get("/users/me");
            isAuthenticated = true;
        } catch (error) {
            isAuthenticated = false;
        }

        if (isAuthenticated) {
            throw redirect({
                to: "/dashboard",
            });
        }
    },
    component: PublicLayout,
});

function PublicLayout() {
    return (
        <div className="min-h-screen bg-gray-50 dark:bg-gray-950">
            <Outlet />
        </div>
    );
}
