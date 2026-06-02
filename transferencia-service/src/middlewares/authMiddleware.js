import { verifyJWT } from '../utils/jwt.js';

const authMiddleware = async (req, res, next) => {
    try {
        const authHeader = req.headers.authorization;

        if (!authHeader?.startsWith('Bearer ')) {
            return res.status(401).json({ error: 'Token requerido' });
        }

        const token = authHeader.split(' ')[1];

        req.user = await verifyJWT(token);

        next();
    } catch (err) {
        return res.status(401).json({ error: err.message });
    }
};

export default authMiddleware;