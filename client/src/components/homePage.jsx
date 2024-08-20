import { Link } from "react-router-dom";
import "../styles/homePage.css";

export default function Home() {
    return (
        <div className="home">
            <section className="receptions" id="homeView">
                <ul className="cards">

                    <li className="cards_item">
                        <div className="card">
                            <div className="card_image">
                                <img
                                    src="https://i0.wp.com/travellingbuzz.com/wp-content/uploads/2015/02/moussaka.jpg?resize=646%2C429&ssl=1"
                                    alt="musaka"
                                />
                            </div>
                            <div className="card_content">
                                <h2 className="card_title">Musaka</h2>
                                <center><Link to="/details" className="details-btn">Show More</Link></center>
                            </div>
                        </div>
                    </li>

                    <li className="cards_item">
                        <div className="card">
                            <div className="card_image">
                                <img
                                    src="https://m.1001recepti.com/images/photos/recipes/size_5/postna-chorba-ot-bob-cb4b5aae6b1e1e4705833a85917e8674-[100483].jpg"
                                    alt="musaka"
                                />
                            </div>
                            <div className="card_content">
                                <h2 className="card_title">Bob</h2>
                                <center><Link to="/details" className="details-btn">Show More</Link></center>
                            </div>
                        </div>
                    </li>

                    <li className="cards_item">
                        <div className="card">
                            <div className="card_image">
                                <img
                                    src="https://www.supichka.com/files/images/529/banica_s_izvara_i_mas_3.jpg"
                                    alt="musaka"
                                />
                            </div>
                            <div className="card_content">
                                <h2 className="card_title">Banica</h2>
                                <center><Link to="/details" className="details-btn">Show More</Link></center>
                            </div>
                        </div>
                    </li>
                </ul>
            </section>
        </div>
    );
}