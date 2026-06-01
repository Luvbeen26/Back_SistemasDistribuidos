const express = require("express");
const cors = require("cors");
const authRoutes = require('./routes/authRoutes')
const encuestasRouter = require('./routes/encuestasRoutes')


const errorHandler = require('./middlewares/errorHandler');
const AISRouter = require('./routes/IA_ROUTES');

const app = express();

app.use(cors());
app.use(express.json());

//rutas
app.use('/api/auth', authRoutes);
app.use('/api/encuestas', encuestasRouter);

// Error 404
app.use((req, res, next) => {
    res.status(404).json({
        message: "Not Found"
    });
})

app.use(errorHandler)

module.exports = app;