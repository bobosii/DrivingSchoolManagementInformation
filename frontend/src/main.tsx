import React from 'react';
import ReactDOM from 'react-dom/client';
import { RouterProvider } from 'react-router-dom';
import { router } from './router/router';
import './index.css';
import { SearchProvider } from './context/SearchContext';
import { AuthProvider } from './context/AuthContext';

ReactDOM.createRoot(document.getElementById('root')!).render(
    <React.StrictMode>
        <AuthProvider>
            <SearchProvider>
                <RouterProvider router={router} />
            </SearchProvider>
        </AuthProvider>
    </React.StrictMode>
);

