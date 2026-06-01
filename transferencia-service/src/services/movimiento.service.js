import prisma from "../config/db.js";

export const getMovimientosBycuenta = async (cuentaId) => {
  const movimientos = await prisma.movimiento.findMany({
    where: { id_cuenta: cuentaId },
    include: { tipo_movimiento: true },
    orderBy: { fecha_hora: "desc" },
  });

  return movimientos.map((mov) => ({
    id_movimiento: mov.id_movimiento,
    tipo_movimiento: mov.tipo_movimiento.descripcion,
    concepto: mov.concepto,
    monto: mov.importe.toString(),
    fecha: mov.fecha_hora.toISOString().replace("T", " ").slice(0, 19),
    accion: mov.tipo_movimiento.action_sum_rest,
  }));
};

export const RegMovimiento = async (data) => {
  const operaciones = [
    prisma.movimiento.create({
      data: {
        id_cuenta: data.id_origen,
        id_tipo_movimiento: 6,
        fecha_hora: new Date(),
        importe: data.importe,
        concepto: data.concepto,
        numero_autorizacion: "000000",
        referencia_numerica: data.referencia,
        referencia_alfanumerica: "AAAAAAAA",
        clabe_interbancaria: data.clabe_destino,
        estatus: "completado"
      }
    }),
    prisma.movimiento.create({
      data: {
        id_cuenta: data.id_destino,
        id_tipo_movimiento: 5,
        fecha_hora: new Date(),
        importe: data.importe,
        concepto: data.concepto,
        numero_autorizacion: "000000",
        referencia_numerica: data.referencia,
        referencia_alfanumerica: "AAAAAAAA",
        clabe_interbancaria: data.clabe_origen,
        estatus: "completado"
      }
    }),
    prisma.cuentaBancaria.update({
      where: { id_cuenta: data.id_origen },
      data: { saldo: { decrement: data.importe } }
    }),
    // Sumar saldo destino
    prisma.cuentaBancaria.update({
      where: { id_cuenta: data.id_destino },
      data: { saldo: { increment: data.importe } }
    }),
  
  ];

  if (data.guardarCuenta && data.tipo_destino) {
    operaciones.push(
      prisma.cuentasSavetransfer.upsert({
        where: {
          id_cuenta_id_destino_tipo: {
            id_cuenta: data.id_origen,
            id_destino: data.id_destino,
            tipo: data.tipo_destino,
          }
        },
        update: {},
        create: {
          id_cuenta: data.id_origen,
          id_destino: data.id_destino,
          tipo: data.tipo_destino,
        }
      })
    );
  }

  await prisma.$transaction(operaciones);
};


export const getSaveTransfers = async (cuentaId) => {
  const transfers = await prisma.cuentasSavetransfer.findMany({
    where: { id_cuenta: Number(cuentaId) },
  });

  return transfers.map((t) => ({
    id_save: t.id_save,
    id_cuenta: t.id_cuenta,
    id_destino: t.id_destino,
    tipo: t.tipo,
  }));
};