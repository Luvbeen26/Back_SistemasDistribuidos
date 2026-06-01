const supabase = require('../config/db');

const sanitizeFileName = (name) => {
    return name
        .normalize('NFD')                 // separa tildes
        .replace(/[\u0300-\u036f]/g, '')  // elimina tildes
        .replace(/ñ/g, 'n')
        .replace(/Ñ/g, 'N')
        .replace(/[^a-zA-Z0-9.\-_]/g, '_') // reemplaza caracteres raros
        .replace(/\s+/g, '_');             // espacios → _
};

const uploadFile = async (file, folder = 'projects', id_project) => {

    const cleanName = sanitizeFileName(file.originalname);

    const fileName = `${folder}/${id_project}/${Date.now()}_${cleanName}`;

    const { error } = await supabase.storage
        .from('project-files')
        .upload(fileName, file.buffer, {
            contentType: file.mimetype,
            cacheControl: '3600',
            upsert: false
        });

    if (error) throw error;

    const { data } = supabase.storage
        .from('project-files')
        .getPublicUrl(fileName);

    return data.publicUrl;
};

module.exports = {
    uploadFile,
}