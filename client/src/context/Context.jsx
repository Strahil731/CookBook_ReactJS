import {createContext, useState} from "react";

export const AuthContext = createContext();

export const AuthContextProvider = ({children}) => {

    const [currentUser, setCurrentUser] = useState(() => {
        const savedUser = sessionStorage.getItem("user");

        return savedUser ? JSON.parse(savedUser) : null;
    });

    const login = async (data) => {
        sessionStorage.setItem("user", JSON.stringify(data));
        setCurrentUser(data);
    };

    const logout = async () => {
        sessionStorage.removeItem("user");
        setCurrentUser(null);
    };

    // useEffect(() => {
    //     localStorage.setItem("user", JSON.stringify(currentUser));
    // }, [currentUser]);

    return (
        <AuthContext.Provider value={{currentUser, setCurrentUser, login, logout}}>
            {children}
        </AuthContext.Provider>
    );
};