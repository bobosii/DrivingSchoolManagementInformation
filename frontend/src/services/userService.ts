import axios from '../api/axios';

export interface User {
    id: number;
    username: string;
    role: string;
    firstName?: string;
    lastName?: string;
    email?: string;
    birthDate?: string;
    termId?: number;
    fullName: string;
    termName?: string;
}

export const getAllUsers = async (): Promise<User[]> => {
    try {
        const response = await axios.get('/admin/users');
        return response.data.data;
    } catch (error) {
        console.error('Error fetching users:', error);
        throw new Error('Kullanıcılar alınırken bir hata oluştu');
    }
};

export const searchUsers = (users: User[], searchTerm: string): User[] => {
    if (!searchTerm) return users;
    
    const lowerSearchTerm = searchTerm.toLowerCase();
    return users.filter(user => {
        const username = user.username?.toLowerCase() || '';
        const fullName = user.fullName?.toLowerCase() || '';
        const termName = user.termName?.toLowerCase() || '';
        
        return username.includes(lowerSearchTerm) || 
               fullName.includes(lowerSearchTerm) || 
               termName.includes(lowerSearchTerm);
    });
}; 