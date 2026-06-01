import express from "express";
import cors from "cors";
import movimientoRouter from "./routes/movimiento.routes.js";
import errorHandler from "./middlewares/errorHandler.js";

const app = express();

app.use(cors());
app.use(express.json());

app.use("/api/movimientos", movimientoRouter);

app.use((req, res, next) => {
  res.status(404).json({ message: "Not Found" });
});

app.use(errorHandler);

export default app;