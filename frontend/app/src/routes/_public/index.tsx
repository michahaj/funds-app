import { createFileRoute } from "@tanstack/react-router";

export const Route = createFileRoute("/_public/")({
    component: () => (
        <div className="@container">
            <p className="py-8 px-6 z-30 text-2xl @sm:text-4xl @md:text-6xl font-bold max-w-2xl @md:max-w-5xl mx-auto text-center">
                Bezpieczne centrum dowodzenia Twoimi finansami!
            </p>
            <p className="py-2 px-4 z-30 text-md @sm:text-4xl @md:text-4xl max-w-2xl @md:max-w-5xl mx-auto text-center">
                Monitoruj swoje inwestycje, wydatki, wartość netto w jednym miejscu.
            </p>
        </div>
    ),
});
