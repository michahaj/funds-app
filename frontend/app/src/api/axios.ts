import axios, { type AxiosError, type InternalAxiosRequestConfig } from "axios";

type RetryableRequestConfig = InternalAxiosRequestConfig & {
    _retryAfterRefresh?: boolean;
};

let refreshRequest: Promise<void> | null = null;

export const api = axios.create({
    baseURL: "/api",
    withCredentials: true,
    headers: {
        "Content-Type": "application/json",
    },
});

api.interceptors.response.use(
    (response) => response,
    async (error: AxiosError) => {
        const request = error.config as RetryableRequestConfig | undefined;
        const isPublicAuthRequest = ["/users/login", "/users/register", "/users/refresh"].some(
            (path) => request?.url?.includes(path)
        );

        if (error.response?.status !== 401 || !request || request._retryAfterRefresh || isPublicAuthRequest) {
            return Promise.reject(error);
        }

        request._retryAfterRefresh = true;

        try {
            refreshRequest ??= axios
                .post("/api/users/refresh", undefined, { withCredentials: true })
                .then(() => undefined)
                .finally(() => {
                    refreshRequest = null;
                });

            await refreshRequest;
            return api.request(request);
        } catch {
            return Promise.reject(error);
        }
    }
);
