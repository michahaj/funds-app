import { createFileRoute } from "@tanstack/react-router";
import { PublicLayout } from "../features/layout/components/PublicLayout";

export const Route = createFileRoute("/_public")({
    component: PublicLayout,
});
