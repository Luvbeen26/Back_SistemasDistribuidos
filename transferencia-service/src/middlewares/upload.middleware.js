const multer = require('multer');
const { v4: uuidv4 } = require('uuid');
const supabase = require('../config/db');

const upload = multer({
    storage: multer.memoryStorage(),
    limits: { fileSize: 5 * 1024 * 1024 }, // Limite de 5MB
    fileFilter: (req, file, cb) => {
        const allowedTypes = [
            'application/pdf',
            'application/octet-stream',
            'application/vnd.openxmlformats-officedocument.wordprocessingml.document',
            'image/jpeg',
            'image/png',
            'image/gif',
            'text/csv'
        ];

        if (allowedTypes.includes(file.mimetype)) {
            cb(null, true);
        } else {
            cb(new Error('Tipo de archivo no permitido'));
        }
    }
});


const BUCKET = 'project-files';

const uploadFiles = async ({ file, folderPath }) => {

    if (!file) throw new Error('No se ha recibido ningun archivo');



    console.log("Mimetype recibido:", file.mimetype);
    const allowedMimeTypes = [
        'application/pdf',
        'application/octet-stream',
        'application/vnd.openxmlformats-officedocument.wordprocessingml.document',
        'image/jpeg',
        'image/png',
        'image/gif',
        'text/csv'
    ];

    if (!allowedMimeTypes.includes(file.mimetype)) {
        throw new Error('Tipo de archivo no permitido');
    }

    const MAX_SIZE = 15 * 1024 * 1024;

    if (file.size > MAX_SIZE) {
        throw new Error('El archivo supera el tamaño permitido');
    }

    const extension = file.originalname.split('.').pop();
    const fileName = `${uuidv4()}.${extension}`;
    const filePath = `${folderPath}/${fileName}`;

    const { data, error } = await supabase.storage
        .from(BUCKET)
        .upload(filePath, file.buffer, {
            contentType: file.mimetype
        });

    if (error) throw error;

    return {
        nombre_original: file.originalname,
        nombre_storage: fileName,
        path: data.path,
        bucket: BUCKET,
        mime_type: file.mimetype,
        tamano_bytes: file.size,
        fullUrl: `${process.env.SUPABASE_URL}/storage/v1/object/public/${BUCKET}/${filePath}`
    };
};


module.exports = { upload, uploadFiles };