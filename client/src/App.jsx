import { Routes, Route } from "react-router-dom";
import Home from "./components/homePage";
import Navbar from "./utils/navbar";
import Footer from "./utils/footer";
import Login from "./components/loginPage";
import {useState} from "react";

function App() {
    const [auth, setAuth] = useState(false);
    return (
        <>
            <Navbar />
            <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/login" element={<Login />} />
            </Routes>
            <Footer />
        </>
    )
}

export default App
