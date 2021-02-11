import React, {Component} from 'react';
import './../Dimplomski/diplomski.css'
import diplomskiService from "../../service/diplomskiService";
import {withRouter} from "react-router";

class CreateDiplomska extends Component {

    constructor(props) {
        super(props);

        this.state = {
            profesori: [],
            teachingStaff: [],
            index: "",
            tema: "",
            oblast: "",
            sodrzina: "",
            clen1: "",
            clen2: ""
        }
    }

    componentDidMount() {
        diplomskiService.getProfessors().then((resp) => {
            console.log(resp.data);
            this.setState({profesori: resp.data});
        });
        diplomskiService.getTeachingStaff().then((resp) => {
            console.log(resp.data);
            this.setState({teachingStaff: resp.data});
        });
    }

    onIndexChange = (e) => {
        this.setState({index: e.target.value});
    }

    onTemaChange = (e) => {
        this.setState({tema: e.target.value});
    }

    onOblastChange = (e) => {
        this.setState({oblast: e.target.value});
    }

    onSodrzinaChange = (e) => {
        this.setState({sodrzina: e.target.value});
    }

    onClen1Change = (e) => {
        this.setState({clen1: e.target.value});
    }

    onClen2Change = (e) => {
        this.setState({clen2: e.target.value});
    }

    clickVnesi = (e) => {
        e.preventDefault();
        let req = {
            indeks: this.state.index,
            title: this.state.tema,
            scope: this.state.oblast,
            description: this.state.sodrzina,
            mentor: this.props.getCurrentUser().userId,
            firstMember: this.state.clen1,
            secondMember: this.state.clen2
        };
        console.log(req);
        debugger;
        diplomskiService.createDiplomska(req).then((resp) => {
            alert("Success!");
            this.props.history.push("/mentor");
        })
    }

    render() {

        let profesoriHtml = this.state.profesori.map((prof) => {
            if(prof.id === this.props.getCurrentUser().userId){
                return null;
            }
            return (
                <option value={prof.id}>
                    {prof.fullname}
                </option>
            );
        });
        profesoriHtml.push((
            <option value={0} selected={true}>
                Избери професор
            </option>
        ));

        let teachingStaffHtml = this.state.teachingStaff.map((teach) => {
            if(teach.id === this.props.getCurrentUser().userId){
                return null;
            }
            return (
                <option value={teach.id}>
                    {teach.fullname}
                </option>
            );
        });
        teachingStaffHtml.push((
            <option value={0} selected={true}>
                Избери професор
            </option>
        ));

        return (
            <div className="container body-content">
                <h4>Пријави дипломска</h4>
                <hr/>
                <div className="form-group row">
                    <label className="col-sm-2 col-form-label font-weight-bold">Индекс</label>
                    <div className="col-sm-10">
                        <input type="text" className="form-control" onChange={this.onIndexChange}/>
                    </div>
                </div>
                <div className="form-group row">
                    <label className="col-sm-2 col-form-label font-weight-bold">Тема</label>
                    <div className="col-sm-10">
                        <textarea className="form-control" onChange={this.onTemaChange}/>
                    </div>
                </div>
                <div className="form-group row">
                    <label className="col-sm-2 col-form-label font-weight-bold">Област</label>
                    <div className="col-sm-10">
                        <textarea className="form-control" onChange={this.onOblastChange}/>
                    </div>
                </div>
                <div className="form-group row">
                    <label className="col-sm-2 col-form-label font-weight-bold">Кратка содржина</label>
                    <div className="col-sm-10">
                        <textarea className="form-control" onChange={this.onSodrzinaChange}/>
                    </div>
                </div>
                <div className="form-group row">
                    <label className="col-sm-2 col-form-label font-weight-bold">Ментор</label>
                    <div className="col-sm-10">
                        <input type="text" disabled className="form-control disabled" value={this.props.getCurrentUser().fullName}/>
                    </div>
                </div>
                <div className="form-group row">
                    <label className="col-sm-2 col-form-label font-weight-bold">Член 1</label>
                    <div className="col-sm-10">
                        <select className="form-control" onChange={this.onClen1Change}>
                            {profesoriHtml}
                        </select>
                    </div>
                </div>
                <div className="form-group row">
                    <label className="col-sm-2 col-form-label font-weight-bold">Член 2</label>
                    <div className="col-sm-10">
                        <select className="form-control" onChange={this.onClen2Change}>
                            {teachingStaffHtml}
                        </select>
                    </div>
                </div>
                <div className="form-group row">
                    <div className="col-sm-12">
                        <button className="btn btn-primary float-right" onClick={this.clickVnesi}>Внеси</button>
                    </div>
                </div>
            </div>
        );
    }
}

export default withRouter(CreateDiplomska);