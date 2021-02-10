import logo from '../../images/finki_mk.png'
import {ROLES} from "../../constants";
import React from 'react';
import {Link} from "react-router-dom";
import {withRouter} from "react-router";

const Header = (props) => {

    let generateMenu = () => {
        let menu = [];
        if(!props.getCurrentUser())
            return menu;
        if(props.getCurrentUser().roleName === ROLES.STUDENT){
            menu.push(
                <li className="nav-item">
                    <Link to="/student" className="nav-link">
                        <span className="fa fa-indent"/>&nbsp;Дипломска
                    </Link>
                </li>
            );
        }
        else if(props.getCurrentUser().roleName === ROLES.ST_SLUZBA){
            menu.push(
                <li className="nav-item">
                    <Link to="/st-sluzba/prijavuvanje" className="nav-link">
                        <span className="fa fa-gavel"/>&nbsp;Пријавување
                    </Link>
                </li>
            );
            menu.push(
                <li className="nav-item">
                    <Link to="/st-sluzba/branenje" className="nav-link">
                        <span className="fa fa-gavel"/>&nbsp;Бранење
                    </Link>
                </li>
            );
        }
        else if(props.getCurrentUser().roleName === ROLES.ASSISTANT){
            menu.push(
                <li className="nav-item">
                    <Link to="/komisija" className="nav-link">
                        <span className="fa fa-tasks"/>&nbsp;Комисија
                    </Link>
                </li>
            );
        }
        else{
            menu.push(
                <li className="nav-item">
                    <Link to="/mentor" className="nav-link">
                        <span className="fa fa-tasks"/>&nbsp;Менторство
                    </Link>
                </li>
            );
            menu.push(
                <li className="nav-item">
                    <Link to="/komisija" className="nav-link">
                        <span className="fa fa-tasks"/>&nbsp;Комисија
                    </Link>
                </li>
            );
            if(props.getCurrentUser().roleName === ROLES.PROFESSOR){
                menu.push(
                    <li className="nav-item">
                        <Link to="/mentor/create-diplomska" className="nav-link">
                            <span className="fa fa-gavel"/>&nbsp;Пријава
                        </Link>
                    </li>
                );
            }
            else if(props.getCurrentUser().roleName === ROLES.PRODEKAN){
                menu.push(
                    <li className="nav-item">
                        <Link to="/prodekan" className="nav-link">
                            <span className="fa fa-gavel"/>&nbsp;Продекан
                        </Link>
                    </li>
                );
            }
        }
        return menu;
    };

    let generateLoginOut = () => {
        let menu = [];
        if(props.getCurrentUser()){
            menu.push(
                <li className="nav-item">
                    <Link to={"#"} className="nav-link">
                        <i className="fa fa-user"/>&nbsp;{props.getCurrentUser().roleName} {/* TODO: SET USERNAME*/}
                    </Link>
                </li>
            );
            menu.push(
                <li className="nav-item">
                    <Link to="#" className="nav-link" onClick={() => props.logout()}>
                        <i className="fa fa-sign-out"/>&nbsp;Одјава
                    </Link>
                </li>
            );
        }
        else{
            menu.push(
                <li className="nav-item">
                    <Link to="/login" className="nav-link">
                        <i className="fa fa-sign-in"/>&nbsp;Најава
                    </Link>
                </li>
            );
        }
        return menu;
    }

    return (
        <nav className="navbar navbar-expand-lg bg-dark navbar-fixed-top">

            <button type="button" className="navbar-toggler" data-toggle="collapse"
                    data-target="#navbarSupportedContent">
                <span className="navbar-toggler-icon"/>
            </button>

            <Link to="/diplomski" className="navbar-brand">
                <img src={logo} alt={logo} className="d-inline-block align-top"/>
            </Link>

            <div className="navbar-collapse collapse" id="navbarSupportedContent">
                <ul className="nav navbar-nav mr-auto">
                    <li className="nav-item">
                        <Link to="/diplomski" className="nav-link">
                            <span className="fa fa-tasks"/>&nbsp;Листа на дипломски
                        </Link>
                    </li>
                    {generateMenu()}
                </ul>

                <div className="navbar-right" id="logoutForm">
                    <ul className="nav navbar-nav navbar-right">
                        {generateLoginOut()}
                    </ul>
                </div>
            </div>
        </nav>
    );
};

export default withRouter(Header);