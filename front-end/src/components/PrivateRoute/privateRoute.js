import React from 'react';
import { Route, Redirect } from 'react-router-dom';

export const PrivateRoute = ({ component: Component, roles, getCurrentUser, ...rest }) => (
    <Route {...rest} render={props => {
        const currentUser = getCurrentUser();
        if (!currentUser) {
            console.log("NE LOGIRANO");
            return <Redirect to={{ pathname: '/diplomski'}} />
        }

        // check if route is restricted by role
        if (roles && roles.indexOf(currentUser.roleName) === -1) {
            // role not authorised so redirect to home page
            console.log("NEMA ULOGA");
            return <Redirect to={{ pathname: '/diplomski'}} />
        }

        // authorised so return component;
        console.log("IT'S OKAT, GO IN")
        return <Component {...props} />
    }} />
)