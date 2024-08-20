import "../styles/navbar.css";
import {Link} from "react-router-dom";

export default function Navbar({auth}) {
    return (
        <>
            {
                auth ? <nav className="navigation" id="navigation">
                    <li className="user">
                        <p id="userMsg">Welcome, <span>Name.</span></p>
                    </li>
                    <li><Link id="homeLink" className="linkBtn" href="/home">Home</Link></li>
                    <li><Link id="searchLink" className="linkBtn" href="/search">Search</Link></li>
                    <li className="right guest"><Link id="loginLink" className="linkBtn" to="/login">Login</Link>
                    </li>
                    <li className="right guest"><Link id="registerLink" className="linkBtn"
                                                      href="/register">Register</Link></li>
                </nav> : <nav className="navigation" id="navigation">
                    <li className="user">
                        <p id="userMsg">Welcome, <span>Name.</span></p>
                    </li>
                    <li className="user"><Link id="createLink" className="linkBtn" href="/create">Create</Link></li>
                    <li><Link id="homeLink" className="linkBtn" href="/home">Home</Link></li>
                    <li><Link id="searchLink" className="linkBtn" href="/search">Search</Link></li>
                    <li className="user right guest"><Link id="logoutLink" className="linkBtn"
                                                           href="/logout">Logout</Link></li>
                </nav>
            }
        </>
    );
}


{/* <li className="user">
                    <p id="userMsg">Welcome, <span>Name.</span></p>
                </li>
                <li className="user"><Link id="createLink" className="linkBtn" href="/create">Create</Link></li>
                <li><Link id="homeLink" className="linkBtn" href="/home">Home</Link></li>
                <li><Link id="searchLink" className="linkBtn" href="/search">Search</Link></li>
                <li className="user right guest"><Link id="logoutLink" className="linkBtn" href="/logout">Logout</Link></li>
                <li className="right guest"><Link id="loginLink" className="linkBtn" to="/login">Login</Link></li>
                <li className="right guest"><Link id="registerLink" className="linkBtn" href="/register">Register</Link></li> */
}