import { Outlet } from "@tanstack/react-router";
import { useState } from "react";
import { Header } from "./Header";
import { Sidebar } from "./Sidebar";
import { ColorBlobs } from "./ColorBlobs";

export function AppLayout() {
  const [drawerOpen, setDrawerOpen] = useState(false);

  return (
    <div className="min-h-screen bg-gray-50 text-gray-900 dark:bg-gray-950 dark:text-gray-100 relative">
      <ColorBlobs />
      <Sidebar open={drawerOpen} onClose={() => setDrawerOpen(false)} />

      <div className="flex min-h-screen flex-col md:pl-64">
        <Header onMenuClick={() => setDrawerOpen(true)} />

        <main className="flex-1 p-6">
          <Outlet />
        </main>
      </div>
    </div>
  );
}
