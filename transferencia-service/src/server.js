const app = require('./app');

const PORT=process.env.PORT || 8083;

app.listen(PORT, () => {
    console.log(`Servidor corriendo en puerto ${PORT}`)
});