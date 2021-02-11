import React from 'react';
import './diplomska.css'

const Diplomska = (props) => {
    let currentDiplomska = props.data;

    let userSpecificRows =
            [<tr>
                <td style={{width: "30%"}}>
                    <span className="fa fa-calendar"/>&nbsp;
                    Датум на одбрана
                </td>
                <td>
                    <strong>{currentDiplomska.date}</strong>
                </td>
            </tr>,
            <tr>
                <td style={{width: "30%"}}>
                    <span className="fa fa-clock-o"/>&nbsp;
                    Време на одбрана
                </td>
                <td>
                    <strong>{currentDiplomska.time}</strong>
                </td>
            </tr>];

    if (props.isUserLoggedIn()) {
        let file =
            <a href="#" target="_self">
                Не постои
            </a>;
        if (currentDiplomska.file) {
            file =
                <a href={`http://localhost:8081/${currentDiplomska.file}`} target="_self">
                    Превземи
                </a>;
        }
        userSpecificRows =
            [
                <tr>
                    <td style={{width: "30%"}}>
                        <span className="fa fa-clock-o"/>&nbsp;
                        Датум на пријавување
                    </td>
                    <td>
                        <strong>{currentDiplomska.date}</strong>
                    </td>
                </tr>,
                <tr>
                    <td style={{width: "30%"}}>
                        <span className="fa fa-thumbs-up"/>&nbsp;
                        Статус
                    </td>
                    <td>
                        <strong>{currentDiplomska.status}</strong>
                    </td>
                </tr>,
                <tr>
                    <td style={{width: "30%"}}>
                        <span className="fa fa-cloud-download"/>&nbsp;
                        Датотека
                    </td>
                    <td>
                        <strong>
                            {file}
                        </strong>
                    </td>
                </tr>,
                <tr>
                    <td style={{width: "30%"}}>
                        <span className="fa fa-level-up"/>&nbsp;
                        Краток опис
                    </td>
                    <td>
                        <strong>{currentDiplomska.description}</strong>
                    </td>
                </tr>
            ];
    }

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
                    {userSpecificRows}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default Diplomska;