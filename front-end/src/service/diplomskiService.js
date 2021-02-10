import {diplomskiApiUtils} from "../utils/apiUtils"

const diplomskiService = {
    getPublicDiplomaList: () => {
        return diplomskiApiUtils.get("/api/diplomski/public-diploma-list");
    },
    getDiplomaList: () => {
        return diplomskiApiUtils.get("/api/diplomski/diploma-list");
    }
};

export default diplomskiService;