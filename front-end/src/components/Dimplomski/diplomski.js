import React, {Component} from 'react';
import diplomskiService from '../../service/diplomskiService'
import './diplomski.css'
import Diplomska from "./Diplomska/diplomska";

class Diplomski extends Component {

    constructor(props) {
        super(props);

        this.state = {
            diplomski: []
        }
    }

    componentDidMount() {
        if(this.props.isUserLoggedIn()){
            diplomskiService.getDiplomaList().then((response) => {
                console.log("PRIVATE DIPLOMSKI");
                console.log(response.data);
                this.setState({
                    diplomski: response.data
                });
            });
        }
        else{
            diplomskiService.getPublicDiplomaList().then((response) => {
                console.log("PUBLIC DIPLOMSKI");
                console.log(response.data);
                this.setState({
                    diplomski: response.data
                });
            })
        }
    }

    render() {
        let diplomskiHtml = this.state.diplomski.map((dip) => {
            return <Diplomska data={dip} isUserLoggedIn={this.props.isUserLoggedIn} key={dip.id}/>
        })
        return (
            <div className="container body-content">
                <h4>Листа на дипломски</h4>
                <hr/>
                {diplomskiHtml}
            </div>
        );
    }

}

export default Diplomski;