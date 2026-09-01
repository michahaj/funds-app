import { createFileRoute } from "@tanstack/react-router";
import { useGetPortfolios } from "../../api/queries/usePortfolios";
import { useCreatePortfolio } from "../../api/mutations/useCreatePortfolio";
import { useState } from "react";
import { useForm } from "@tanstack/react-form";

export const Route = createFileRoute("/_app/portfolios")({
    component: PortfoliosComponent,
});

function PortfoliosComponent() {
    const { data: portfolios, isLoading, isError, error } = useGetPortfolios();
    const { mutateAsync: createPortfolio, isPending } = useCreatePortfolio();

    const [isFormOpen, setIsFormOpen] = useState(false);

    const form = useForm({
        defaultValues: { name: "", currency: "PLN" },
        onSubmit: async ({ value }) => {
            try {
                await createPortfolio(value);
                setIsFormOpen(false);
                form.reset();
            } catch (err) {
                alert("Błąd podczas dodawania portfela");
            }
        },
    });

    if (isLoading) return <div className="p-8 text-center text-gray-500">Pobieranie portfeli...</div>;
    if (isError) return <div className="p-8 text-red-500">Błąd: {error.message}</div>;

    return (
        <div className="space-y-6">
            <div className="flex items-center justify-between">
                <h1 className="text-2xl font-bold text-gray-900 dark:text-white">Moje Portfele</h1>
                <button
                    onClick={() => setIsFormOpen(!isFormOpen)}
                    className="rounded-lg bg-purple-600 px-4 py-2 text-sm font-semibold text-white hover:bg-purple-700"
                >
                    {isFormOpen ? "Anuluj" : "+ Nowy Portfel"}
                </button>
            </div>

            {isFormOpen && (
                <form
                    className="flex gap-4 items-end bg-white p-4 rounded-xl shadow-sm border border-gray-200 dark:bg-gray-900 dark:border-gray-800"
                    onSubmit={(e) => {
                        e.preventDefault();
                        e.stopPropagation();
                        form.handleSubmit();
                    }}
                >
                    <form.Field
                        name="name"
                        children={(field) => (
                            <div className="flex-1">
                                <label className="block text-sm font-medium mb-1 dark:text-gray-300">
                                    Nazwa portfela
                                </label>
                                <input
                                    value={field.state.value}
                                    onChange={(e) => field.handleChange(e.target.value)}
                                    className="w-full rounded-lg border border-gray-300 px-3 py-2 dark:border-gray-700 dark:bg-gray-800 dark:text-white"
                                    placeholder="np. Krypto Emerytura"
                                    required
                                />
                            </div>
                        )}
                    />
                    <form.Field
                        name="currency"
                        children={(field) => (
                            <div className="w-32">
                                <label className="block text-sm font-medium mb-1 dark:text-gray-300">Waluta</label>
                                <select
                                    value={field.state.value}
                                    onChange={(e) => field.handleChange(e.target.value)}
                                    className="w-full rounded-lg border border-gray-300 px-3 py-2 dark:border-gray-700 dark:bg-gray-800 dark:text-white"
                                >
                                    <option value="PLN">PLN</option>
                                    <option value="USD">USD</option>
                                    <option value="EUR">EUR</option>
                                </select>
                            </div>
                        )}
                    />
                    <button
                        type="submit"
                        disabled={isPending}
                        className="rounded-lg bg-green-600 px-6 py-2 font-semibold text-white disabled:opacity-50"
                    >
                        {isPending ? "Zapisywanie..." : "Zapisz"}
                    </button>
                </form>
            )}

            <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
                {portfolios?.map((portfolio) => (
                    <div
                        key={portfolio.id}
                        className="rounded-xl border border-gray-200 bg-white p-6 shadow-sm dark:border-gray-800 dark:bg-gray-900"
                    >
                        <h3 className="text-lg font-bold text-gray-900 dark:text-white">{portfolio.name}</h3>
                        <p className="mt-2 text-3xl font-black text-gray-700 dark:text-gray-300">
                            {portfolio.totalValue ? portfolio.totalValue.toFixed(2) : "0.00"}
                            <span className="text-sm font-medium ml-1 text-gray-500">{portfolio.currency}</span>
                        </p>
                    </div>
                ))}
            </div>
        </div>
    );
}
