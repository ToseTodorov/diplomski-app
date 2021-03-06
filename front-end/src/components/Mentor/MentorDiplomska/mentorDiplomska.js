import React, {useState} from 'react';
import './../../Dimplomski/Diplomska/diplomska.css'
import diplomskiService from "../../../service/diplomskiService";
import {withRouter} from "react-router";
import TimePicker from 'react-time-picker';
import "react-time-picker/dist/TimePicker.css"
import DatePicker from "react-date-picker";
import "react-datepicker/dist/react-datepicker.css";

const MentorDiplomska = (props) => {
    const currentDiplomska = props.data;

    const [file, setFile] = useState({});
    const [datePicked, setPickedDate] = useState(new Date());
    const [timePicked, setTimePicked] = useState("10:00");
    const [locationPicked, setLocationPicked] = useState("");

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

    const validate = (e) => {
        e.preventDefault();

        diplomskiService.validateDiplomska(currentDiplomska.id).then((reps) => {
            alert("Validated!")
            props.rerenderParent();
        });
    };

    const validateHtml = () => {
        if(currentDiplomska.statusNumber === 5){
            return (
                <tr>
                    <td style={{width: "30%"}}>
                        <span className="fa fa-clipboard"/>&nbsp;
                        Валидирај
                    </td>
                    <td>
                        <i
                            className="fa fa-check-square"
                            style={{color: "green", fontSize: "x-large", cursor: "pointer"}}
                            onClick={validate}
                        />
                    </td>
                </tr>
            );
        }
        else if(currentDiplomska.statusNumber > 5){
            return (
                <tr>
                    <td style={{width: "30%"}}>
                        <span className="fa fa-clipboard"/>&nbsp;
                        Валидирај
                    </td>
                    <td>
                        <strong>Валидирано</strong>
                    </td>
                </tr>
            );
        }
        return null;
    };

    const onLocationChange = (e) => {
        setLocationPicked(e.target.value);
    };

    const validateLocation = (e) => {
        e.preventDefault();
        let odbrana = {
            diplomskaId: currentDiplomska.id,
            date: `${datePicked.getDate()}.${datePicked.getMonth()}.${datePicked.getFullYear()}`,
            time: timePicked,
            location: locationPicked
        };
        diplomskiService.submitOdbrana(odbrana).then((resp) => {
            alert("Submitted!");
            props.rerenderParent();
        });
    }

    const datePickerHtml = () => {
        if(currentDiplomska.statusNumber === 7){
            return (
                <tr>
                    <td style={{width: "30%"}}>
                        <span className="fa fa-calendar"/>&nbsp;
                        Локација и време
                    </td>
                    <td>
                        <input type="text" onChange={onLocationChange}/>
                        <DatePicker value={datePicked} onChange={date => setPickedDate(date)} format={"dd-MM-y"}/>
                        <TimePicker value={timePicked} onChange={setTimePicked} format={"HH:mm"}/>
                        <i
                            className="fa fa-check-square"
                            style={{float: "right", color: "green", fontSize: "x-large", cursor: "pointer"}}
                            onClick={validateLocation}
                        />
                    </td>
                </tr>
            );
        }
        else if(currentDiplomska.statusNumber > 7){
            return (
                <tr>
                    <td style={{width: "30%"}}>
                        <span className="fa fa-calendar"/>&nbsp;
                        Локација и време
                    </td>
                    <td>
                        <strong>{currentDiplomska.location}, {currentDiplomska.date}, {currentDiplomska.time}</strong>
                    </td>
                </tr>
            );
        }
        return null;
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
                            <strong>{currentDiplomska.submissionDate}</strong>
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
                    {validateHtml()}
                    {datePickerHtml()}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default withRouter(MentorDiplomska);