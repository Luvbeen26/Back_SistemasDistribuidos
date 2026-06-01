const express = require('express');
const router = express.Router();
const encuestasController = require('../controllers/encuestasController');
const { upload } = require('../middlewares/upload.middleware');

// EncuestaS
router.get('/project/:id_project', encuestasController.getAllEncuestas);

router.get('/respuestas/contenido/:id', encuestasController.getContenidoArchivoRespuestas
);

router.get('/basic/:id_project', encuestasController.getBasicEncuestas)

router.get('/:id', encuestasController.getEncuestaByID);

router.post('/add', encuestasController.addEncuesta);

router.put('/update/:id', encuestasController.updateEncuesta);

router.delete('/delete/:id', encuestasController.deleteEncuesta);


// PREGUNTAS
router.post('/preguntas/add', encuestasController.addPregunta);

router.put('/preguntas/upsert', encuestasController.upsertPregunta);

router.delete('/preguntas/:id', encuestasController.deletePregunta);

// Opciones
router.post('/opcion/add', encuestasController.addOpcion);

router.put('/opcion/upsert', encuestasController.upserOpcion);

router.delete('/opcion/:id', encuestasController.deleteOpcion);




// ARCHIVOS
router.post(
    '/archivos',
    upload.single('file'), // multer
    encuestasController.addArchivoEncuesta
);

router.delete('/archivos/:id', encuestasController.deleteArchivo);



module.exports = router;