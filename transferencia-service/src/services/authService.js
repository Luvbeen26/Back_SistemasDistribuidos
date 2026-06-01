const supabase = require('../config/db');
const { CreateAccount } = require('../models/auth');
const bcrypt = require('bcrypt');

const createAccount = async (registerData) => {
    const validated = CreateAccount.parse(registerData);

    if (validated.password !== validated.confirm_password) {
        throw new Error("Las contraseñas no coinciden")
    };

    if (await checkEmail(validated.email)) {
        throw new Error("El correo ingresado ya esta en uso")
    };

    const payload = {
        name: validated.name,
        lastname: validated.lastname,
        username: validated.username,
        email: validated.email,
        phone_number: validated.phone_number,
        hash_password: await hashPassword(validated.password),
        status: "Activo",
        // user_inter_exter:true,
    };

    const { data, error } = await supabase.from('User').insert(payload).select().single();

    if (error) {
        throw new Error(`Error al crear nuevo usuario: ${error.message}`)
        //    throw new Error(error.message);

    };
    return data
}


const loginAccount = async ({ email, password }) => {

    const { data: user, error } = await supabase.from("User").select("*")
        .eq("email", email).single();

    if (error || !user) {
        throw new Error("Correo no registrado");
    }

    const validPassword = await bcrypt.compare(password, user.hash_password);

    if (!validPassword) {
        throw new Error("Credenciales invalidas");
    }

    return {
        id_user: user.id_user,
        email: user.email,
        name: user.name,
        lastname: user.lastname,
        username: user.username
    }
}

const hashPassword = async (plainPassword) => {
    const saltRounds = 10;
    return bcrypt.hash(plainPassword, saltRounds);
}

const checkEmail = async (email) => {
    const { data, error } = await supabase
        .from('User')
        .select('id_user')
        .eq('email', email)
        .limit(1);

    if (error) throw error;

    return data.length > 0;
}

module.exports = { createAccount, loginAccount }