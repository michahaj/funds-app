import { useMutation } from "@tanstack/react-query";
import { api } from "../axios";

interface RegisterData {
    username: string;
    email: string;
    password: string;
}

export const useRegisterMutation = () => {
    return useMutation({
        mutationFn: async (data: RegisterData) => {
            const response = await api.post("/users/register", data);
            return response.data;
        },
    });
};
