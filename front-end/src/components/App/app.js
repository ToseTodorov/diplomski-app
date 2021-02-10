import React from "react";
import Login from "../Login/login";
import Diplomski from "../Dimplomski/diplomski";
import {LOGGED_IN_USER, ROLES} from '../../constants'
import {Redirect, Route, Switch} from 'react-router-dom'
import {PrivateRoute} from "../PrivateRoute/privateRoute";
import CreateDiplomska from "../CreateDiplomska/createDiplomska";
import Komisija from "../Komisija/komisija";
import Mentor from "../Mentor/mentor";
import Prodekan from "../Prodekan/prodekan";
import Student from "../Student/student";
import Prijavuvanje from "../StSluzba/Prijavuvanje/prijavuvanje";
import Branenje from "../StSluzba/Branenje/branenje";
import Header from "../Header/header";
import {withRouter} from "react-router";

class App extends React.Component {

    constructor(props) {
        super(props);

        let user = null;
        if (localStorage.getItem(LOGGED_IN_USER)) {
            user = JSON.parse(localStorage.getItem(LOGGED_IN_USER));
        }
        this.state = {
            currentUser: user
        }

        this.login = this.login.bind(this);
        this.logout = this.logout.bind(this);
    }

    getCurrentUser = () => {
        return this.state.currentUser;
    }

    logout = () => {
        localStorage.removeItem(LOGGED_IN_USER);
        this.setState({currentUser: null});
        this.props.history.push('/diplomski');
    }

    login = (user) => {
        localStorage.setItem(LOGGED_IN_USER, JSON.stringify(user));
        this.setState({currentUser: user});
        this.props.history.push('/diplomski');
    }

    render() {
        return (
            <div>
                <Header getCurrentUser={this.getCurrentUser} logout={this.logout} {...this.props}/>
                <Switch>
                    <Route path={"/diplomski"}
                           exact
                           render={(props) => <Diplomski isUserLoggedIn={this.getCurrentUser} {...props}/>}
                    />
                    <Route path={"/login"}
                           exact
                           render={(props) => <Login login={this.login} {...props}/>}
                    />
                    <PrivateRoute path={"/mentor"}
                                  exact
                                  component={Mentor}
                                  roles={[ROLES.PROFESSOR, ROLES.PRODEKAN]}
                                  getCurrentUser={this.getCurrentUser}
                                  {...this.props}
                    />
                    <PrivateRoute path={"/mentor/create-diplomska"}
                                  exact
                                  component={CreateDiplomska}
                                  roles={[ROLES.PROFESSOR]}
                                  getCurrentUser={this.getCurrentUser}
                                  {...this.props}
                    />
                    <PrivateRoute path={"/komisija"}
                                  exact
                                  component={Komisija}
                                  roles={[ROLES.PROFESSOR, ROLES.PRODEKAN, ROLES.ASSISTANT]}
                                  getCurrentUser={this.getCurrentUser}
                                  {...this.props}
                    />
                    <PrivateRoute path={"/prodekan"}
                                  exact
                                  component={Prodekan}
                                  roles={[ROLES.PRODEKAN]}
                                  getCurrentUser={this.getCurrentUser}
                                  {...this.props}
                    />
                    <PrivateRoute path={"/student"}
                                  exact
                                  component={Student}
                                  roles={[ROLES.STUDENT]}
                                  getCurrentUser={this.getCurrentUser}
                                  {...this.props}
                    />
                    <PrivateRoute path={"/st-sluzba/prijavuvanje"}
                                  exact
                                  component={Prijavuvanje}
                                  roles={[ROLES.ST_SLUZBA]}
                                  getCurrentUser={this.getCurrentUser}
                                  {...this.props}
                    />
                    <PrivateRoute path={"/st-sluzba/branenje"}
                                  exact
                                  component={Branenje}
                                  roles={[ROLES.ST_SLUZBA]}
                                  getCurrentUser={this.getCurrentUser}
                                  {...this.props}
                    />
                    <Redirect to={"/diplomski"}/>
                </Switch>
            </div>
        );
    }
}

export default withRouter(App);