import { Link } from "@tanstack/react-router";
import { useEffect } from "react";

const navItems = [{ to: "/portfolios", label: "Portfele" }] as const;

const glassPanel =
    "bg-white/70 backdrop-blur-md border border-white/20 shadow-lg dark:bg-slate-900/60 dark:border-slate-700/30";

type SidebarProps = {
    open: boolean;
    onClose: () => void;
};

export function Sidebar({ open, onClose }: SidebarProps) {
    useEffect(() => {
        if (!open) return;

        const handleEscape = (e: KeyboardEvent) => {
            if (e.key === "Escape") onClose();
        };

        document.addEventListener("keydown", handleEscape);
        document.body.style.overflow = "hidden";

        return () => {
            document.removeEventListener("keydown", handleEscape);
            document.body.style.overflow = "";
        };
    }, [open, onClose]);

    return (
        <>
            <div
                aria-hidden={!open}
                className={`fixed inset-0 z-40 bg-slate-900/20 backdrop-blur-sm transition-opacity duration-300 md:hidden ${
                    open ? "opacity-100 pointer-events-auto" : "opacity-0 pointer-events-none"
                }`}
                onClick={onClose}
            />

            <aside
                className={`fixed inset-y-0 left-0 z-50 flex w-72 max-w-[85vw] flex-col transition-transform duration-300 ease-in-out md:z-40 md:w-64 md:max-w-none md:translate-x-0 ${glassPanel} border-r md:border-slate-200 md:bg-white md:backdrop-blur-none md:shadow-none dark:md:border-slate-800 dark:md:bg-slate-950 ${
                    open ? "translate-x-0" : "-translate-x-full"
                }`}
            >
                <div className="flex h-16 shrink-0 items-center justify-between border-b border-white/20 px-6 dark:border-slate-700/30 md:border-slate-200 dark:md:border-slate-800">
                    <Link
                        to="/"
                        className="text-lg font-semibold tracking-tight text-slate-900 dark:text-white"
                        onClick={onClose}
                    >
                        Funds
                    </Link>
                    <button
                        type="button"
                        onClick={onClose}
                        aria-label="Zamknij menu"
                        className="rounded-lg p-2 text-slate-600 transition-colors hover:bg-white/50 hover:text-slate-900 md:hidden dark:text-slate-300 dark:hover:bg-slate-800/50 dark:hover:text-white"
                    >
                        <CloseIcon />
                    </button>
                </div>

                <nav className="flex flex-1 flex-col gap-1 p-4">
                    {navItems.map(({ to, label }) => (
                        <Link
                            key={to}
                            to={to}
                            onClick={onClose}
                            className="rounded-lg px-3 py-2 text-sm font-medium text-slate-600 transition-colors hover:bg-white/50 hover:text-slate-900 dark:text-slate-300 dark:hover:bg-slate-800/50 dark:hover:text-white [&.active]:bg-white/60 [&.active]:text-slate-900 dark:[&.active]:bg-slate-800/60 dark:[&.active]:text-white md:hover:bg-slate-100 md:dark:hover:bg-slate-800 md:[&.active]:bg-slate-100 md:dark:[&.active]:bg-slate-800"
                            activeProps={{ className: "active" }}
                        >
                            {label}
                        </Link>
                    ))}
                </nav>
            </aside>
        </>
    );
}

function CloseIcon() {
    return (
        <svg
            xmlns="http://www.w3.org/2000/svg"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            strokeWidth="2"
            className="h-5 w-5"
            aria-hidden
        >
            <path strokeLinecap="round" d="M6 6l12 12M18 6L6 18" />
        </svg>
    );
}
