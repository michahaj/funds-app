import { createFileRoute, useNavigate } from "@tanstack/react-router";
import { useForm } from "@tanstack/react-form";
import { z } from "zod";
import { api } from "../../api/axios";
import { useState } from "react";

export const Route = createFileRoute("/_public/register")({
    component: RegisterComponent,
});

const registerSchema = z.object({
    username: z.string().min(3, "Nazwa musi zawierać co najmniej 3 znaki"),
    email: z.email("Nieprawidłowy adres email"),
    password: z.string().min(6, "Hasło musi zawierać co najmniej 6 znaków").max(50, "Hasło jest za długie"),
});

function RegisterComponent() {
    const navigate = useNavigate();
    const [serverError, setServerError] = useState<string | null>(null);

    const form = useForm({
        defaultValues: {
            username: "",
            email: "",
            password: "",
        },
        validators: {
            onSubmit: registerSchema,
        },
        onSubmit: async ({ value }) => {
            try {
                await api.post("/users/register", value);
                navigate({ to: "/login" });
            } catch {
                const backendMsg = "Wystąpił błąd serwera";
                setServerError(backendMsg);
            }
        },
    });

    const renderField = (
        name: "username" | "email" | "password",
        label: string,
        type: React.HTMLInputTypeAttribute = "text",
        autoComplete?: string
    ) => (
        <form.Field
            name={name}
            children={(field) => (
                <div className="flex flex-col">
                    <label
                        htmlFor={`register-${name}`}
                        className="mb-1 block text-sm font-medium text-gray-700 dark:text-gray-300"
                    >
                        {label}
                    </label>
                    <input
                        id={`register-${name}`}
                        name={field.name}
                        type={type}
                        autoComplete={autoComplete}
                        value={field.state.value}
                        onBlur={field.handleBlur}
                        onChange={(e) => field.handleChange(e.target.value)}
                        className="block w-full rounded-lg border border-gray-300 bg-gray-50 px-4 py-2.5 dark:border-gray-700 dark:bg-gray-800 dark:text-white focus:border-purple-500 focus:outline-none focus:ring-2 focus:ring-purple-500/50"
                    />
                    {field.state.meta.errors.length > 0 && (
                        <em className="text-xs text-red-500 mt-1.5 block">
                            {field.state.meta.errors.map((err: any) => err.message || err).join(", ")}
                        </em>
                    )}
                </div>
            )}
        />
    );

    return (
        <div className="flex min-h-[80vh] items-center justify-center px-4">
            <div className="w-full max-w-md rounded-2xl border border-gray-200 bg-white p-8 shadow-xl dark:border-gray-800 dark:bg-gray-900">
                <h2 className="text-3xl font-extrabold text-center text-gray-900 dark:text-white mb-6">
                    Dołącz do nas
                </h2>

                {serverError && (
                    <div className="mb-4 rounded-lg bg-red-50 p-3 text-sm text-red-600 dark:bg-red-950/50 dark:text-red-400">
                        ⚠️ {serverError}
                    </div>
                )}

                <form
                    className="space-y-4"
                    onSubmit={(e) => {
                        e.preventDefault();
                        e.stopPropagation();
                        form.handleSubmit();
                    }}
                >
                    {renderField("username", "Nazwa użytkownika", "text", "username")}
                    {renderField("email", "Email", "email", "email")}
                    {renderField("password", "Hasło", "password", "new-password")}

                    <form.Subscribe
                        selector={(state) => [state.canSubmit, state.isSubmitting]}
                        children={([canSubmit, isSubmitting]) => (
                            <button
                                type="submit"
                                disabled={!canSubmit || isSubmitting}
                                className="w-full rounded-lg bg-purple-900 px-4 py-3 font-semibold text-white hover:bg-purple-700 disabled:opacity-50"
                            >
                                {isSubmitting ? "Wysyłanie..." : "Zarejestruj się"}
                            </button>
                        )}
                    />
                </form>
            </div>
        </div>
    );
}
