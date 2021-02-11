import {diplomskiApiUtils} from "../utils/apiUtils"

const diplomskiService = {
    getPublicDiplomaList: () => {
        return diplomskiApiUtils.get("/api/diplomski/public-diploma-list");
    },
    getDiplomaList: () => {
        return diplomskiApiUtils.get("/api/diplomski/diploma-list");
    },
    getMentorDiplomaList: () => {
        return diplomskiApiUtils.get("/api/diplomski/mentor-diplomski");
    },
    uploadFile: (diplomskaId, file) => {
        return diplomskiApiUtils.postFile(`/api/diplomski/upload-file?diplomskaId=${diplomskaId}`, file);
    },
    validateDiplomska: (diplomskaId) => {
        return diplomskiApiUtils.post(`/api/diplomski/validate-cekor5?diplomskaId=${diplomskaId}`, {});
    },
    submitOdbrana: (obdrana) => {
        return diplomskiApiUtils.post(`/api/diplomski/submit-odbrana`, obdrana);
    },
    getDummyDiplomski: () => {
        return [
            {
                file: null,
                statusNumber: 4,
                status: "ok"
            }
        ]
    }
};

export default diplomskiService;