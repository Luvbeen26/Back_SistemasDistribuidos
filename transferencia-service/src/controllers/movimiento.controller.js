import { getMovimientosBycuenta,RegMovimiento,getSaveTransfers } from "../services/movimiento.service.js";

export const getMovimientos = async (req, res) => {
  try {
    const { id_cuenta } = req.params;
    const movimientos = await getMovimientosBycuenta(Number(id_cuenta));
    res.json( movimientos );
  } catch (error) {
    res.status(500).json({ ok: false, message: error.message });
  }
};


export const registerMovimiento = async (req, res) => {
  try {
    
    const movimientos = await RegMovimiento(req.body);
    res.json( movimientos );
  } catch (error) {
    res.status(500).json({ ok: false, message: error.message });
  }
}


export const getAccSaves = async (req, res) => {
  try {
    const { id_cuenta } = req.params;
    const t = await getSaveTransfers(Number(id_cuenta));
    res.json( t );
  } catch (error) {
    res.status(500).json({ ok: false, message: error.message });
  }
};
