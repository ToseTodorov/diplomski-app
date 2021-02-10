import React from "react";
import Header from "../Header/Header";
import logo from "../../logo.svg";
import Login from "../Login/login";
import Diplomski from "../Dimplomski/diplomski";
import {LOGGED_IN_USER, LOGGED_IN_ROLE} from '../../constants'
import {BrowserRouter as Router, Redirect, Route} from 'react-router-dom'

class App extends React.Component{

    getCurrentUser = () => {
        if(localStorage.getItem(LOGGED_IN_USER))
            return {
                userId: localStorage.getItem(LOGGED_IN_USER),
                roleName: localStorage.getItem(LOGGED_IN_ROLE)
            };
        return null;
    }

    render() {
        return (
            <Router>
                <Header getCurrentUser={this.getCurrentUser} {... this.props}/>
                <Route path={"/login"} exact>
                    <Login/>
                </Route>
                <Route path={"/diplomski"} exact>
                    <Diplomski isUserLoggedIn={this.getCurrentUser} {...this.props}/>
                </Route>
                <Redirect to={"/diplomski"}/>
            </Router>
        );
    }
}

export default App;