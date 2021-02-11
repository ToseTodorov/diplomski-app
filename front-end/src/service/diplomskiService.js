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