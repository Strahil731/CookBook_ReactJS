import {Routes, Route} from "react-router-dom";
import Home from "./components/homePage";
import Navbar from "./utils/navbar";
import Footer from "./utils/footer";
import Login from "./components/loginPage";
import {useEffect, useState} from "react";

function App() {
    const [auth, setAuth] = useState(false);

    function checkAuth() {
        if (auth === "false") {
            alert("Invalid username or password!");
        } else {

        }
    }

    useEffect(() => {
        checkAuth();
    }, []);
    return (
        <>
            <Navbar auth={auth}/>
            <Routes>
                <Route path="/" element={<Home/>}/>
                <Route path="/login" element={<Login/>}/>
            </Routes>
            <Footer/>
        </>
    )
}

export default App
