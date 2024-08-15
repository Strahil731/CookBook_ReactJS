import { Routes, Route } from "react-router-dom"
import Home from "./components/homePage"
import Navbar from "./utils/navbar"

function App() {

    return (
        <>
            <Navbar />
            <Routes>
                <Route path="/" element={<Home />} />
            </Routes>
        </>
    )
}

export default App
