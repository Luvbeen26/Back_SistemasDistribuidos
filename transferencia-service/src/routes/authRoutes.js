const express = require('express');
const router = express.Router();
const authController = require('../controllers/authController');
const authMiddleware =  require('../middlewares/authMiddleware');

router.post('/create_account', authController.createAccount);
router.post('/login', authController.login);
router.get('/getAuthUser',authMiddleware,authController.getAuthUser);

module.exports =router;