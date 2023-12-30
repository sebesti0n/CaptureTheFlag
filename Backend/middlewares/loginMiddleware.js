const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');
const knex = require('knex')(require('../Configuration/knexfile')['development']);


const loginMiddleware =async (req,res,next) =>{
    const {userEmail, userPass} = req.body;
    try {
        const finduser = await knex('users').select('*').where('email','=',userEmail).returning('*');
    console.log(userEmail+ userPass+finduser);
        
        if (finduser.length!=0) {
    console.log(userEmail+ userPass+finduser);

            const validate = await bcrypt.compare(userPass, finduser.password);
            // console.log(validate)
            if (!validate) {
                return res.status(200).json({ success : false,message:"Invalid Credentials", userDetails: null });
            }
            else {
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