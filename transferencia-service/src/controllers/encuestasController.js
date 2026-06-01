const encuestaService = require('../services/encuestaService');
const {SelectProject}=require('../models/project');

// Encuestas

const getAllEncuestas = async (req, res, next) => {
    try {
        const { id_project } = req.params;
        const data = await encuestaService.getAllEncuestas(id_project);
        res.json(data);
    } catch (err) {
        next(err);
    }
};

const getEncuestaByID = async (req, res, next) => {
    try {
        const { id } = req.params;
        const data = await encuestaService.getEncuestaByID(id);
        res.json(data);
    } catch (err) {
        next(err);
    }
};

const addEncuesta = async (req, res, next) => {
    try {
        const data = await encuestaService.addEncuesta(req.body);
        res.status(201).json(data);
    } catch (err) {
        next(err);
    }
};

const updateEncuesta = async (req, res, next) => {
    try {
        const { id } = req.params;
        const data = await encuestaService.updateEncuesta(id, req.body);
        res.json(data);
    } catch (err) {
        next(err);
    }
};

const deleteEncuesta = async (req, res, next) => {
    try {
        const { id } = req.params;
        const data = await encuestaService.deleteEncuesta(id);
        res.json(data);
    } catch (err) {
        next(err);
    }
};


// Preguntas
const addPregunta = async (req, res, next) => {
    try {
        const data = await encuestaService.addPregunta(req.body);
        res.status(201).json(data);
    } catch (err) {
        next(err);
    }
};

const upsertPregunta = async (req, res, next) => {
    try {
        const data = await encuestaService.upsertPregunta(req.body);
        res.json(data);
    } catch (err) {
        next(err);
    }
};

const deletePregunta = async (req, res, next) => {
    try {
        const { id } = req.params;
        const data = await encuestaService.deletePregunta(id);
        res.json(data);
    } catch (err) {
        next(err);
    }
};

// Opciones
const addOpcion = async (req, res, next) => {
    try {
        const data = await encuestaService.addOpcion(req.body);
        res.status(201).json(data);
    } catch (err) {
        next(err);
    }
};

const upserOpcion = async (req, res, next) => {
    try {
        const data = await encuestaService.upserOpcion(req.body);
        res.json(data);
    } catch (err) {
        next(err);
    }
};

const deleteOpcion = async (req, res, next) => {
    try {
        const { id } = req.params;
        const data = await encuestaService.deleteOpcion(id);
        res.json(data);
    } catch (err) {
        next(err);
    }
};



// Archivos
const addArchivoEncuesta = async (req, res, next) => {
    try {
        const { id_encuesta, tipo, id_project } = req.body;

        const file = req.file; // multer single

        const data = await encuestaService.addArchivoEncuesta({
            id_encuesta,
            tipo,
            file, id_project
        });

        res.status(201).json(data);
    } catch (err) {
        next(err);
    }
};

const deleteArchivo = async (req, res, next) => {
    try {
        const { id } = req.params;
        const data = await encuestaService.deleteArchivo(id);
        res.json(data);
    } catch (err) {
        next(err);
    }
};

const getContenidoArchivoRespuestas = async (req, res, next) => {
    try {
        const { id } = req.params;
        const data = await encuestaService.getContenidoArchivoRespuestasService(id);

        if (!data) {
            return res.status(404).json({ message: 'No hay archivo de respuestas para esta encuesta' });
        }

        res.status(200).json(data);
    } catch (error) {
        next(error);
    }
};


const getBasicEncuestas = async (req, res) => {
    try {
        const id_project = SelectProject.parse(req.params.id_project);

        const encuestas = await encuestaService.getEncuestasByProjectBasic(id_project);

        return res.status(200).json(encuestas);
    } catch (err) {
        console.error('ERROR REAL →', err);
        return res.status(500).json({
            message: 'Error obteniendo encuestas'
        });
    }
}

module.exports = {
    getAllEncuestas, getEncuestaByID, addEncuesta, updateEncuesta, deleteEncuesta,
    addPregunta, upsertPregunta, deletePregunta,
    addOpcion, upserOpcion, deleteOpcion,
    addArchivoEncuesta, deleteArchivo, getContenidoArchivoRespuestas,
    getBasicEncuestas
}
