const authService = require('../services/authService')
const {GenerateJWT} = require('../utils/jwt')

const getAuthUser = async(req, res, next)=>{
    try{
        const user=req.user
        res.json(user);
    }catch (err) {
        next(err);
    };
}


const createAccount = async(req,res,next) =>{
    try{
        const account = await authService.createAccount(req.body);
        res.status(201).json(account);
    }catch(err){
        next(err);
    }
}

const login = async (req, res,next) => {
    try{
        const { email, password } = req.body;
        const user= await authService.loginAccount({ email,password });

        const token= await GenerateJWT({
            id_user:user.id_user,
            email:user.email,
            name:user.name,
            lastname:user.lastname,
            username:user.username
        });
        res.status(200).json({token});
        
    }catch(err){
        next(err);
    }
}

module.exports = { createAccount, login, getAuthUser}