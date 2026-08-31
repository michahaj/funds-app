import { Outlet } from "@tanstack/react-router";
import { Header } from "./Header";
import { ColorBlobs } from "./ColorBlobs";

export function PublicLayout() {
  return (
    <div className="min-h-screen bg-gray-50 text-gray-900 dark:bg-gray-950 dark:text-gray-100 relative">
      <ColorBlobs />
      <Header />

      <main className="flex-1 p-6">
        <Outlet />
      </main>
    </div>
  );
}
