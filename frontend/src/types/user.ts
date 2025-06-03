export interface User {
    id: number;
    username: string;
    fullName: string;
    role: string;
    firstName?: string;
    lastName?: string;
    email?: string;
    birthDate?: string;
}