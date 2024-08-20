import {createContext, useState} from "react";

export const AuthContext = createContext();

export const AuthContextProvider = ({children}) => {
    // const loggedUser = async () => {
    //     const res = await api.get("/api/v1/loggedUser");
    //     setCurrentUser(res.data);
    // }

    const [currentUser, setCurrentUser] = useState({});

    const login = async (inputs) => {
        const response = await fetch("http://localhost:8080/api/auth/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(inputs)
        });

        const data = await response.json();
        setCurrentUser(data);
    };

    // const logout = async () => {
    //     const res = await api.get("api/v1/auth/logout");
    //     setCurrentUser(res.data);
    // };

    // useEffect(() => {
    //     localStorage.setItem("user", JSON.stringify(currentUser));
    // }, [currentUser]);

    return (
        <AuthContext.Provider value={{currentUser, login}}>
            {children}
        </AuthContext.Provider>
    );
};