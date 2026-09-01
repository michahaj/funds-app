import { useQuery } from "@tanstack/react-query";
import { api } from "../axios";

export interface Portfolio {
    id: number;
    name: string;
    currency: string;
    totalValue?: number;
}

export const useGetPortfolios = () => {
    return useQuery({
        queryKey: ["portfolios"],
        queryFn: async () => {
            const response = await api.get<Portfolio[]>("/portfolios");
            return response.data;
        },
    });
};
