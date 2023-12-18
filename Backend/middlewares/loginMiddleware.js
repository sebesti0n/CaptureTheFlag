const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');
const User = require('../models/user');

const loginMiddleware =async (req,res,next) =>{
    const {userEmail, userPass} = req.body;
    console.log(userEmail);
    try {
        const finduser = await User.findOne({ Email: userEmail });
        if (finduser) {
            const validate = await bcrypt.compare(userPass, finduser.password);
            if (!validate) {
                return res.status(403).json({ success : false, message: 'Invalid credential' });
            }
            else {
            req.finduser =finduser;
            next();
                }
    }
        else {
            return res.status(403).json({success:false, message:'No User Found'});
        }
    } catch (error) {
        console.log(error);
        return res.status(500).send({success:false, message:'Internal Server Error'});

    }
};
module.exports = loginMiddleware;