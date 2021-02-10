import {diplomskiApiUtils} from "../utils/apiUtils"

const diplomskiService = {
    getPublicDiplomaList: () => {
        return diplomskiApiUtils.get("/public-diploma-list");
    },
    getDiplomaList: () => {
        return diplomskiApiUtils.get("/diploma-list");
    }
};

export default diplomskiService;