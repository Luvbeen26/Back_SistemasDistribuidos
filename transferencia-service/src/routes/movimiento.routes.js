import { Router } from "express";
import { getMovimientos,registerMovimiento,getAccSaves } from "../controllers/movimiento.controller.js";

const router = Router();

router.get("/:id_cuenta", getMovimientos);
router.post("/", registerMovimiento);
router.get("/saveTransferencia/:id_cuenta", getAccSaves);

export default router;