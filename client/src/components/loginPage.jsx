import "../styles/loginPage.css";
import {useContext, useState} from "react";
import {AuthContext} from "../context/Context.jsx";
import {useNavigate} from "react-router-dom";

export default function Login() {
    const history = useNavigate();
    const {login} = useContext(AuthContext);
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    async function authenticationFn(e) {
        e.preventDefault();

        const response = await fetch("http://localhost:8080/api/auth/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({email, password})
        });

        if (response.status !== 200) {
            alert("Invalid username or password!")

        } else {
            alert("Successful login!")
            login(await response.json());
            history("/");
        }

    }

    return (
        <div className="loginPage">
            <section className="login" id="loginView">
                <h2>Login</h2>
                <form className="loginForm" id="loginForm">
                    <input
                        type="email"
                        id="loginEmail"
                        placeholder="Email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                    />
                    <input
                        type="password"
                        id="loginPassword"
                        placeholder="Password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />
                    <input type="checkbox" id="loginCheck"/>Remember me
                    <p className="right">Forgot <a href="">password</a> ?</p>
                    <button onClick={authenticationFn}>Login</button>
                    <p>You do not have and Account ? <a id="registerBtn" href="/register">Register</a></p>
                </form>
            </section>
        </div>
    );
}