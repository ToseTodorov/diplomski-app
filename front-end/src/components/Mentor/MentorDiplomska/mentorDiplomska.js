import React, {useState} from 'react';
import './../../Dimplomski/Diplomska/diplomska.css'
import diplomskiService from "../../../service/diplomskiService";
import {withRouter} from "react-router";

const MentorDiplomska = (props) => {
    const currentDiplomska = props.data;

    let [file, setFile] = useState({});

    const onFileChange = (e) => {
        setFile(e.target.files[0]);
    }

    const onFileUpload = (e) => {
        e.preventDefault();
        if(file.type !== 'application/pdf'){
            alert("File must be in .pdf format!");
            return;
        }
        diplomskiService.uploadFile(currentDiplomska.id, file).then((response) => {
            alert("File has been uploaded!");
            setFile({});
            props.rerenderParent();
        })
    };

    const fileHtml = () => {
        if(currentDiplomska.file){
            return (
                <a href={`http://localhost:8081/${currentDiplomska.file}`} target="_blank">
                    Превземи
                </a>
            );
        }
        if(currentDiplomska.statusNumber === 4){
            return [
                <input type="file" className="btn" onChange={onFileChange}/>,
                <button onClick={onFileUpload} className="btn btn-outline" style={{backgroundColor: "#f2f2f2", float: "right"}}>Upload</button>
            ];
        }
        return (
            <a href="#" target="_self">
                Не постои
            </a>
        );
    };

    return (
        <div className="panel panel-default">
            <div className="panel-heading">
                <b>{currentDiplomska.title}</b>
            </div>
            <div className="panel-body">
                <table className="table table-bordered table-striped table-condensed ">
                    <tbody>
                    <tr>
                        <td style={{width: "30%"}}>
                            <span className="fa fa-graduation-cap"/>&nbsp;
                            Студент
                        </td>
                        <td>
                            <strong>{currentDiplomska.student}</strong>
                        </td>
                    </tr>
                    <tr>
                        <td style={{width: "30%"}}>
                            <span className="fa fa-user"/>&nbsp;
                            Ментор
                        </td>
                        <td>
                            <strong>{currentDiplomska.mentor}</strong>
                        </td>
                    </tr>
                    <tr>
                        <td style={{width: "30%"}}>
                            <span className="fa fa-user-md"/>&nbsp;
                            Член 1
                        </td>
                        <td>
                            <strong>{currentDiplomska.firstMember}</strong>
                        </td>
                    </tr>
                    <tr>
                        <td style={{width: "30%"}}>
                            <span className="fa fa-user-md"/>&nbsp;
                            Член 2
                        </td>
                        <td>
                            <strong>{currentDiplomska.secondMember}</strong>
                        </td>
                    </tr>
                    <tr>
                        <td style={{width: "30%"}}>
                            <span className="fa fa-clock-o"/>&nbsp;
                            Датум на пријавување
                        </td>
                        <td>
                            <strong>{currentDiplomska.date}</strong>
                        </td>
                    </tr>
                    <tr>
                        <td style={{width: "30%"}}>
                            <span className="fa fa-thumbs-up"/>&nbsp;
                            Статус
                        </td>
                        <td>
                            <strong>{currentDiplomska.status}</strong>
                        </td>
                    </tr>
                    <tr>
                        <td style={{width: "30%"}}>
                            <span className="fa fa-level-up"/>&nbsp;
                            Краток опис
                        </td>
                        <td>
                            <strong>{currentDiplomska.description}</strong>
                        </td>
                    </tr>
                    <tr>
                        <td style={{width: "30%"}}>
                            <span className="fa fa-cloud-download"/>&nbsp;
                            Датотека
                        </td>
                        <td>
                            <strong>
                                {fileHtml()}
                            </strong>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default withRouter(MentorDiplomska);