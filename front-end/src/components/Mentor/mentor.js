import React, {useEffect, useState} from 'react';
import diplomskiService from "./../../service/diplomskiService"
import './../Dimplomski/diplomski.css'
import MentorDiplomska from "./MentorDiplomska/mentorDiplomska";

const Mentor = () => {

    let [diplomski, setDiplomski] = useState([]);
    useEffect(() => {
        diplomskiService.getMentorDiplomaList().then((response) => {
            setDiplomski(response.data);
        });
    }, []);
    // useEffect(() => {
    //     setDiplomski(diplomskiService.getDummyDiplomski());
    // }, [])

    let diplomskiHtml = diplomski.map((dip) => {
        return <MentorDiplomska data={dip} key={dip.file}/>
    });

    return (
        <div className="container body-content">
            <h4>Листа на дипломски</h4>
            <hr/>
            {diplomskiHtml}
        </div>
    );
};

export default Mentor;