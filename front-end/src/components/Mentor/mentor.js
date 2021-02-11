import React, {Component, useEffect, useState} from 'react';
import diplomskiService from "./../../service/diplomskiService"
import './../Dimplomski/diplomski.css'
import MentorDiplomska from "./MentorDiplomska/mentorDiplomska";
import {withRouter} from "react-router";

class Mentor extends Component {
    constructor(props) {
        super(props);

        this.state = {
            diplomski: []
        };
    }

    componentDidMount() {
        diplomskiService.getMentorDiplomaList().then((resp) => {
            this.setState({diplomski: resp.data});
        });
    }

    rerenderParent = () => {
        diplomskiService.getMentorDiplomaList().then((resp) => {
            this.setState({diplomski: resp.data});
        });
    }

    render() {
        let diplomskiHtml = this.state.diplomski.map((dip) => {
            return <MentorDiplomska data={dip} key={dip.id} rerenderParent={this.rerenderParent} {...this.props}/>
        });

        return (
            <div className="container body-content">
                <h4>Листа на дипломски</h4>
                <hr/>
                {diplomskiHtml}
            </div>
        );
    }
}

export default withRouter(Mentor);