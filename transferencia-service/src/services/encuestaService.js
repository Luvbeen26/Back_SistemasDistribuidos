const supabase = require('../config/db');
const { uploadFiles } = require('../middlewares/upload.middleware');
const { encuestas, preguntas_encuesta, SelectEncuesta, SelectPreguntaEncuesta, opciones_encuesta, SelectOpcionEncuesta, SelectArchivosEncuesta } = require('../models/encuestas');

const getAllEncuestas = async (id) => {
    const { data: encuestas, error } = await supabase
        .from('encuestas')
        .select('*')
        .eq('id_project', id);

    if (error) throw error;

    if (!encuestas || encuestas.length === 0) {
        return [];
    }

    const idsMembersProject = [
        ...new Set(encuestas.map(e => e.id_encuestador))
    ];

    const { data: miembros, error: errorMembers } = await supabase
        .from('Members_project')
        .select('id_members_project, id_user')
        .in('id_members_project', idsMembersProject);

    if (errorMembers) throw errorMembers;

    const userIds = miembros.map(m => m.id_user);

    const { data: usuarios, error: errorUsers } = await supabase
        .from('User')
        .select('id_user, name, lastname')
        .in('id_user', userIds);

    if (errorUsers) throw errorUsers;

    const miembrosMap = new Map(
        miembros.map(m => [m.id_members_project, m.id_user])
    );

    const usuariosMap = new Map(
        usuarios.map(u => [u.id_user, u])
    );

    const resultado = encuestas.map(e => {
        const userId = miembrosMap.get(e.id_encuestador);
        const user = usuariosMap.get(userId);
        return {
            id: e.id_encuesta,
            titulo: e.title,
            encuestador: user ? `${user.name} ${user.lastname}` : 'Sin asignar',
            datetime: e.fecha_aplicada,
            status: e.status,
            statusBadge: [{
                text: e.status,
                color:
                    e.status === 'Validada' ? '#12b76a33' :
                        e.status === 'Borrador' ? '#f7900933' :
                            e.status === 'Cerrada' ? '#66708533' :
                                e.status === 'Cancelada' ? '#b7121233' : '#737a9b',
                textColor:
                    e.status === 'Validada' ? '#12b76a' :
                        e.status === 'Borrador' ? '#f79009' :
                            e.status === 'Cerrada' ? '#667085' :
                                e.status === 'Cancelada' ? '#b71212' : '#737a9b33'
            }],
            objetivo: e.objetivo,
            id_project: e.id_project,
            id_encuestado: e.id_encuestado,
            id_encuestador: e.id_encuestador,
            id_rol_encuestado: e.id_rol_encuestado,
            observaciones: e.observaciones,
        };
    });

    return resultado;
};


const getEncuestaByID = async (id) => {
    SelectEncuesta.parse(id);

    // ── Traemos encuesta + preguntas + opciones de cada pregunta ──
    const { data, error } = await supabase
        .from('encuestas')
        .select(`
            *,
            preguntas_encuesta (
                *,
                opciones_encuesta (*)
            )
        `)
        .eq('id_encuesta', id)
        .order('number', {
            foreignTable: 'preguntas_encuesta',
            ascending: true
        })
        .single();

    if (error) throw error;

    // Ordenar opciones por 'number' dentro de cada pregunta
    if (data.preguntas_encuesta) {
        data.preguntas_encuesta.forEach(p => {
            if (p.opciones_encuesta) {
                p.opciones_encuesta.sort((a, b) => (a.number ?? 0) - (b.number ?? 0));
            }
        });
    }

    // ── Traemos archivos desde Archive ────────────────────────────
    const { data: archivos, error: errorArchivos } = await supabase
        .from('Archive')
        .select('*')
        .eq('entidad_origin', 'encuestas')
        .eq('id_entidad', id)
        .eq('type', 'file');

    if (errorArchivos) throw errorArchivos;

    const archivosTransformados = archivos.map(a => ({
        id_archivo: a.id,
        created_at: a.created_at,
        tipo: a.file_type,
        url_archivo: a.url_archivo,
        id_encuesta: a.id_entidad,
        nombre_original: a.name,
        nombre_storage: a.nombre_storage,
        mime_type: a.mime_type,
        tamano_bytes: a.tamano_bytes,
        path: a.path
    }));

    return {
        ...data,
        archivos_encuesta: archivosTransformados
    };
};


const addEncuesta = async (encuestaData) => {
    const validated = encuestas.parse(encuestaData);

    const { data, error } = await supabase
        .from('encuestas')
        .insert([{ ...validated }])
        .select();

    if (error) throw error;
    return data[0];
};

const updateEncuesta = async (id, encuestaData) => {
    const validated = encuestas.parse(encuestaData);

    const { data, error } = await supabase
        .from('encuestas')
        .update({ ...validated })
        .eq('id_encuesta', id)
        .select();

    if (error) throw error;
    return data[0];
};

const deleteEncuesta = async (id) => {
    SelectEncuesta.parse(id);

    const { data, error } = await supabase
        .from('encuestas')
        .delete()
        .eq('id_encuesta', id);

    if (error) throw error;
    return { message: 'Encuesta eliminada correctamente' };
};


// ── Preguntas ─────────────────────────────────────────────────

const addPregunta = async (preguntaDataArray) => {
    const validated = preguntaDataArray.map(p => preguntas_encuesta.parse(p));

    const { data, error } = await supabase
        .from('preguntas_encuesta')
        .insert(validated)
        .select();

    if (error) throw error;
    return data;
};

const upsertPregunta = async (preguntaDataArray) => {
    const validated = preguntaDataArray.map(p => preguntas_encuesta.parse(p));

    const { data, error } = await supabase
        .from('preguntas_encuesta')
        .upsert(validated, { onConflict: 'id_pregunta' })
        .select();

    if (error) throw error;
    return data;
};

const deletePregunta = async (id) => {
    SelectPreguntaEncuesta.parse(id);

    const { data, error } = await supabase
        .from('preguntas_encuesta')
        .delete()
        .eq('id_pregunta', id);

    if (error) throw error;
    return { message: 'Pregunta eliminada correctamente' };
};


// ── Opciones ──────────────────────────────────────────────────

const addOpcion = async (opcionDataArray) => {
    const validated = opcionDataArray.map(p => opciones_encuesta.parse(p));

    const { data, error } = await supabase
        .from('opciones_encuesta')
        .insert(validated)
        .select();

    if (error) throw error;
    return data;
};

const upsertOpcion = async (opcionDataArray) => {
    const validated = opcionDataArray.map(p => opciones_encuesta.parse(p));

    const { data, error } = await supabase
        .from('opciones_encuesta')
        .upsert(validated, { onConflict: 'id_opcion' })
        .select();

    if (error) throw error;
    return data;
};

const deleteOpcion = async (id) => {
    SelectOpcionEncuesta.parse(id);

    const { data, error } = await supabase
        .from('opciones_encuesta')
        .delete()
        .eq('id_opcion', id);

    if (error) throw error;
    return { message: 'Opción eliminada correctamente' };
};


// ── Archivos ──────────────────────────────────────────────────

const addArchivoEncuesta = async ({ id_encuesta, file, tipo, id_project, id_user }) => {
    if (!file) throw new Error('No se recibió archivo');

    SelectArchivosEncuesta.parse(id_encuesta);

    const { data: encuesta, error: encuestaError } = await supabase
        .from('encuestas')
        .select('id_encuesta')
        .eq('id_encuesta', id_encuesta)
        .single();

    if (encuestaError || !encuesta) throw new Error('La encuesta no existe');

    let id_folder;

    const { data: folder, error: folderError } = await supabase
        .from('Archive')
        .select('*')
        .eq('name', 'Encuestas')
        .eq('project_id', id_project)
        .eq('type', 'folder')
        .maybeSingle();

    if (folderError) throw folderError;

    if (!folder) {
        const folderData = {
            created_by: id_user,
            updated_at: new Date().toISOString(),
            name: 'Encuestas',
            type: 'folder',
            created_at: new Date().toISOString(),
            project_id: id_project,
        };

        const { data: newFolder, error: createError } = await supabase
            .from('Archive')
            .insert([folderData])
            .select()
            .single();

        if (createError) throw createError;
        id_folder = newFolder.id;
    } else {
        id_folder = folder.id;
    }

    const folderPath = `projects/${id_project}/encuestas/${id_encuesta}/${tipo}`;
    const uploadResult = await uploadFiles({ file, folderPath });

    const archivoData = {
        created_by: id_user,
        updated_at: new Date().toISOString(),
        name: uploadResult.nombre_original,
        type: 'file',
        route_storage: uploadResult.fullUrl,
        created_at: new Date().toISOString(),
        parent_id: id_folder,
        project_id: id_project,
        mime_type: uploadResult.mime_type,
        entidad_origin: 'encuestas',
        id_entidad: id_encuesta,
        nombre_storage: uploadResult.nombre_storage,
        tamano_bytes: uploadResult.tamano_bytes,
        path: uploadResult.path,
        file_type: tipo
    };

    const { data, error } = await supabase
        .from('Archive')
        .insert([archivoData])
        .select()
        .single();

    if (error) throw error;
    return data;
};

const deleteArchivo = async (id_archivo) => {
    const { data: archivo, error } = await supabase
        .from('Archive')
        .select('*')
        .eq('id_entidad', id_archivo)
        .eq('type', 'file')
        .single();

    if (error || !archivo) throw new Error('Archivo no encontrado');

    const { error: storageError } = await supabase.storage
        .from('project-files')
        .remove([archivo.path]);

    if (storageError) throw storageError;

    await supabase
        .from('Archive')
        .delete()
        .eq('id_entidad', id_archivo)
        .eq('type', 'file');

    return { message: 'Archivo eliminado correctamente' };
};



const getContenidoArchivoRespuestasService = async (id_encuesta) => {
    SelectEncuesta.parse(id_encuesta);

    // Buscar el archivo de tipo 'resultados' de esta encuesta
    const { data: archivo, error } = await supabase
        .from('Archive')
        .select('*')
        .eq('entidad_origin', 'encuestas')
        .eq('id_entidad', id_encuesta)
        .eq('file_type', 'resultados')
        .eq('type', 'file')
        .maybeSingle();

    if (error) throw error;
    if (!archivo) return null;   // sin archivo de respuestas aún

    // Descargar el contenido del archivo desde Supabase Storage usando el path
    const { data: fileData, error: downloadError } = await supabase.storage
        .from('project-files')
        .download(archivo.path);

    if (downloadError) throw downloadError;

    // Convertir Blob/Buffer a texto UTF-8
    const text = await fileData.text();

    return {
        nombre_original: archivo.name,
        id_archivo: archivo.id,
        contenido: text,      // texto plano del CSV
    };
};


const getEncuestasByProjectBasic = async (id_project) => {
    const { data, error } = await supabase
        .from('encuestas')
        .select('id_encuesta, title')
        .eq('id_project', id_project)
        .eq('status', 'Validada')
        .order('title', { ascending: true });

    if (error) throw error;

    return data;
};


module.exports = {
    getAllEncuestas, getEncuestaByID, addEncuesta, updateEncuesta, deleteEncuesta,
    addPregunta, upsertPregunta, deletePregunta,
    addOpcion, upsertOpcion, deleteOpcion,
    addArchivoEncuesta, deleteArchivo,
    getContenidoArchivoRespuestasService,
    getEncuestasByProjectBasic
};