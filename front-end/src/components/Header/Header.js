import logo from '../../images/finki_mk.png'
import React from 'react';

const Header = () => {

    return (
        <nav className="navbar navbar-expand-lg bg-dark navbar-fixed-top">

            <button type="button" className="navbar-toggler" data-toggle="collapse"
                    data-target="#navbarSupportedContent">
                <span className="navbar-toggler-icon"/>
                <span className="navbar-toggler-icon"/>
                <span className="navbar-toggler-icon"/>
            </button>

            {/*TODO: CHANGE LINK HERE*/}
            <a href="/Home" className="navbar-brand ">
                <img src={logo} alt={logo} className="d-inline-block align-top"/>
            </a>


            <div className="navbar-collapse collapse" id="navbarSupportedContent">
                <ul className="nav navbar-nav mr-auto">
                    <li className="nav-item">
                        {/*TODO: CHANGE LINK HERE*/}
                        <a href="/Students" className="nav-link">
                            <span className="fa fa-home"/>&nbsp;Почетна
                        </a>
                    </li>
                    <li className="nav-item">
                        {/*TODO: CHANGE LINK HERE*/}
                        <a href="/Students/Diplomalist" className="nav-link">
                            <span className="fa fa-indent"/>&nbsp;Дипломска
                        </a>
                    </li>
                    <li className="nav-item">
                        {/*TODO: CHANGE LINK HERE*/}
                        <a href="/DiplomaList" className="nav-link">
                            <span className="fa fa-tasks"/>&nbsp;Листа на дипломски
                        </a>
                    </li>
                </ul>

                {/*TODO: CHANGE LINK HERE*/}
                <form action="/Account/LogOff" className="navbar-right" id="logoutForm" method="post">
                    <input
                        name="__RequestVerificationToken" type="hidden"
                        value="dUqCOnak_72lrB5YyADs0MUogRnQ1RtvNwc3te6AY_m1JF3ZFG2PBT3uWVkR62z2A-XAP1wc8rzRgXSYAAGbtAEVZO170ekNFhqQ6GVF21ruaCgR3Oucqng9pkSGktcCkh2KPnw9jc_iF_BROlR8aA2"/>
                    <ul className="nav navbar-nav navbar-right">
                        <li className="nav-item">
                            <a href={void (0)} className="nav-link"><i className="fa fa-user"/>&nbsp;173036</a>
                        </li>
                        <li className="nav-item">
                            {/*TODO: CHANGE LINK HERE*/}
                            <a href="#" className="nav-link"
                                // onClick={document.getElementById('logoutForm').submit()}
                            >
                                <i className="fa fa-sign-out"/>&nbsp;Одјава</a>
                        </li>
                    </ul>
                </form>
            </div>
        </nav>
    );
};

export default Header;