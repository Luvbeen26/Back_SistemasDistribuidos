const { verifyToken, verifyJWT } = require('../utils/jwt');

const authMiddleware = async (req, res, next) => {
    try {
        const authHeader = req.headers.authorization;

        if (!authHeader || !authHeader.startsWith('Bearer ')) {
            return res.status(401).json({ error: 'Token requerido' });
        }

        const token = authHeader.split(' ')[1];

        const payload = await verifyJWT(token);
        
        req.user = payload;
        //id_user,email,name,lastname,username
        
        next();
    } catch (err) {
        res.status(401).json({ error: err.message });
    }
};

module.exports = authMiddleware;
