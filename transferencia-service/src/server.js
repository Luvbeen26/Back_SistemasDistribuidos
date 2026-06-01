import dotenv from 'dotenv';
dotenv.config();

const { default: app } = await import('./app.js');

const PORT = process.env.PORT || 8083;

app.listen(PORT, () => {
    console.log(`Servidor corriendo en puerto ${PORT}`);
});