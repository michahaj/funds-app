import { useMutation, useQueryClient } from "@tanstack/react-query";
import { api } from "../axios";

export interface CreatePortfolioData {
    name: string;
    currency: string;
}

export const useCreatePortfolio = () => {
    const queryClient = useQueryClient();

    return useMutation({
        mutationFn: async (data: CreatePortfolioData) => {
            const response = await api.post("/portfolios", data);
            return response.data;
        },
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["portfolios"] });
        },
        onError: (error) => {
            console.error("Nie udało się utworzyć portfela:", error);
        },
    });
};
