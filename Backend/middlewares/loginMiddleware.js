const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');
const User = require('../models/user');

const loginMiddleware =async (req,res,next) =>{
    const {userEmail, userPass} = req.body;
    console.log(userEmail+ userPass);
    try {
        const finduser = await User.findOne({ Email: userEmail });
        if (finduser) {
            console.log("1-0")
            const validate = await bcrypt.compare(userPass, finduser.password);
            console.log(validate)
            if (!validate) {
                return res.status(200).json({ success : false,message:"Invalid Credentials", userDetails: null });
            }
            else {
            console.log("1-1")
            req.finduser =finduser;
            next();
                }
         }
        else {
            //not registered user
            return res.status(203).json({success:false,message : "Register First", userDetails:null});
        }
    } catch (error) {
        console.log(error);
        return res.status(500).send({success:false, message:'Internal Server Error'});

    }
};
module.exports = loginMiddleware;