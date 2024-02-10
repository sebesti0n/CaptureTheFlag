const express = require('express');
const router = express.Router();
const dotenv =require('dotenv');
const loginMiddleware = require('../middlewares/loginMiddleware');
const registrationMiddleware = require('../middlewares/registrationMiddleware');
const validationMiddleware = require('../middlewares/validationMiddleware');
const userController = require('../Controllers/userController');


dotenv.config();
router.get('/', (req, res) => {
    return res.send("Om Namah Shivaay");
});
router.post('/register',registrationMiddleware,userController.userRegistration);
router.post('/login',loginMiddleware, (req, res) => {
    const { finduser } = req;
    return res.status(200).json({success: true,message:"ok",userDetails:finduser[0]});
});
router.get('/validate',validationMiddleware , (req, res) => {
    return res.send("Successfully Verified");
});
module.exports = router;