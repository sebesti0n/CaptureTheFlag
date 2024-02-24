const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');
const knex = require('knex')(require('../Configuration/knexfile')['development']);


const loginMiddleware =async (req,res,next) =>{
    const {userEmail, userPass} = req.body;
    // console.log("req.Body:",req.body);
    try {
        const finduser = await knex('users').where('email','=',userEmail).returning('*');
    console.log("finduser:",finduser);
        
        if (finduser.length!=0) {
    console.log(userPass,finduser[0].password);


            bcrypt.compare(userPass, finduser[0].password)
            .then((result) => {
                let data={
                    time:Date(),
                    userEmail:finduser[0].email
                }
        const token = jwt.sign(data, process.env.JWT_SECRET_KEY);
                if (result) {
                    req.finduser =finduser;
                    req.token = token;
                    next();
                } else { 
                    return res.status(200).json({ success : false,message:"Invalid Credentials", userDetails: null });
                }
              })
              .catch((error) => {
                console.error('Error comparing passwords:', error);
                return res.status(500).json({ success : false,message:"Server Error", userDetails: null });
                
              });
         }
        else {
            //not registered user
            return res.status(203).json({success:false,message : "Register First", userDetails:null});
        }
    } catch (error) {
        console.log(error)
        return res.status(500).send({success:false, message:'Internal Server Error'});

    }
};
module.exports = loginMiddleware;