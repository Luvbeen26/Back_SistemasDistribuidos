import { SignJWT, jwtVerify } from 'jose';

const secret = new TextEncoder().encode(process.env.JWT_SECRET);

export const GenerateJWT = async (payload) => {
    return await new SignJWT(payload)
        .setProtectedHeader({ alg: 'HS256' })
        .setIssuedAt()
        .setExpirationTime('90d')
        .sign(secret);
};

export const verifyJWT = async (token) => {
    try {
        const { payload } = await jwtVerify(token, secret);
        return payload;
    } catch (err) {
        throw new Error('Token inválido o expirado');
    }
};