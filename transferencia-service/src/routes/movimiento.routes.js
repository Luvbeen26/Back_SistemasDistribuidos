import { Router } from "express";
import { getMovimientos,registerMovimiento,getAccSaves,validarLimiteDiario } from "../controllers/movimiento.controller.js";
import authMiddleware from '../middlewares/authMiddleware.js';


const router = Router();

router.get("/:id_cuenta", authMiddleware,getMovimientos);
router.post("/", authMiddleware, registerMovimiento);
router.get("/saveTransferencia/:id_cuenta", authMiddleware, getAccSaves);
router.post("/validar-limite", validarLimiteDiario);

export default router;