import React from 'react';
import Auth from '../../service/authService'
import {withRouter} from "react-router";

const Login = (props) => {

    const validate = (e) => {
        e.preventDefault();

        let mail = document.getElementById("address");
        let pass = document.getElementById("password");

        let request = {
            usernameOrEmail: mail.value,
            password: pass.value
        };

        Auth.login(request)
            .then((response) => {
                props.login(response.data);
            });
    };

    return (
        <form onSubmit={validate}>
            <div className="col-sm-4 ml-auto mr-auto border rounded p-5 mt-5 bg-dark text-primary"
                 style={{opacity: "0.9"}}>
                <label htmlFor="address">Корисничко име:</label>
                <input id="address" className="form-control text-center" autoFocus type="text"
                       placeholder="example@example.com"/>
                <br/>
                <label htmlFor="password">Лозинка:</label>
                <input id="password" className="form-control text-center" type="password" placeholder="******"/>
                <hr/>
                <div className="col-sm-8 ml-auto mr-auto">
                    <button type={'submit'} className="btn btn-primary btn-block">Логирај се</button>
                </div>
            </div>
        </form>
    );
}

export default withRouter(Login);