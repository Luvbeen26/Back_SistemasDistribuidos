import { getMovimientosBycuenta,RegMovimiento,getSaveTransfers,getTotalEgresosHoy } from "../services/movimiento.service.js";

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


export const validarLimiteDiario = async (req, res) => {
  const { id_cuenta, limite_diario, importe } = req.body;

  try {
    const totalEgresosHoy = await getTotalEgresosHoy(Number(id_cuenta));
    const disponible = Number(limite_diario) - totalEgresosHoy;
    const excede = totalEgresosHoy + Number(importe) > Number(limite_diario);

    return res.status(200).json({ excede, disponible });
  } catch (error) {
    return res.status(500).json({ message: 'Error al validar límite diario', error: error.message });
  }
};