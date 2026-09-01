import { createFileRoute, redirect, Outlet } from "@tanstack/react-router";
import { api } from "../api/axios";

export const Route = createFileRoute("/_app")({
    beforeLoad: async () => {
        try {
            await api.get("/users/me");
        } catch (error) {
            throw redirect({
                to: "/login",
            });
        }
    },
    component: AppLayout,
});

function AppLayout() {
    return (
        <div className="flex min-h-screen bg-gray-50 dark:bg-gray-950">
            <main className="flex-1 p-8">
                <Outlet />
            </main>
        </div>
    );
}
