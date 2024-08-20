import "../styles/loginPage.css";

export default function Login() {

    function authenticationFn(e){
        e.preventDefault();
    }

    return (
        <div className="loginPage">
            <section className="login" id="loginView">
                <h2>Login</h2>
                <form className="loginForm" id="loginForm">
                    <input type="email" id="loginEmail" placeholder="Email" />
                    <input type="password" id="loginPassword" placeholder="Password" />
                    <input type="checkbox" id="loginCheck" />Remember me
                    <p className="right">Forgot <a href="">password</a> ?</p>
                    <button onClick={authenticationFn}>Login</button>
                    <p>You do not have and Account ? <a id="registerBtn" href="/register">Register</a></p>
                </form>
            </section>
        </div>
    );
}