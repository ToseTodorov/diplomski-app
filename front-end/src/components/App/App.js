import React from "react";
import Header from "../Header/Header";
import Login from "../Login/login";
import Diplomski from "../Dimplomski/diplomski";
import {LOGGED_IN_USER, ROLES} from '../../constants'
import {BrowserRouter as Router, Redirect, Route} from 'react-router-dom'
import {PrivateRoute} from "../PrivateRoute/privateRoute";
import CreateDiplomska from "../CreateDiplomska/createDiplomska";
import Mentorship from "../Mentorship/mentorship";

class App extends React.Component{

    getCurrentUser = () => {
        if(localStorage.getItem(LOGGED_IN_USER))
            return JSON.parse(localStorage.getItem(LOGGED_IN_USER));
        return null;
    }

    logout = () => {
        localStorage.removeItem(LOGGED_IN_USER);
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
                <PrivateRoute path={"/create-diplomska"}
                              component={CreateDiplomska}
                              roles={[ROLES.PROFESSOR]}
                              getCurrentUser={this.getCurrentUser}
                              {...this.props}
                />
                <PrivateRoute path={"/mentorship"}
                              component={Mentorship}
                              roles={[ROLES.PROFESSOR, ROLES.PRODEKAN]}
                              getCurrentUser={this.getCurrentUser}
                              {...this.props}
                />
                <PrivateRoute path={"/komisija"}
                              component={null}
                              roles={[ROLES.PROFESSOR, ROLES.PRODEKAN, ROLES.ASSISTANT]}
                              getCurrentUser={this.getCurrentUser}
                              {...this.props}
                />
                <Redirect to={"/diplomski"}/>
            </Router>
        );
    }
}

export default App;