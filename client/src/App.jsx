import {Routes, Route} from "react-router-dom";
import Home from "./components/homePage";
import Navbar from "./utils/navbar";
import Footer from "./utils/footer";
import Login from "./components/loginPage";
import {useEffect, useState} from "react";
import {AuthContextProvider} from "./Context/context.jsx";

function App() {
    const [auth, setAuth] = useState(false);

    function checkAuth() {
        if (auth === "false") {
            alert("Invalid username or password!");
        } else {
            alert("Login successfully!");
        }

        console.log(auth);
    }

    useEffect(() => {
        checkAuth();
    }, []);
    return (
        <AuthContextProvider>
            <Navbar auth={auth} setAuth={setAuth}/>
            <Routes>
                <Route path="/" element={<Home/>}/>
                <Route path="/login" element={<Login setAuth={setAuth}/>}/>
            </Routes>
            <Footer/>
        </AuthContextProvider>
    )
}

export default App
