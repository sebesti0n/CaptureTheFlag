const jwt = require('jsonwebtoken');
const dotenv =require('dotenv');
dotenv.config()


const validationMiddleware = async(req,res,next)=>{
    try {
        let jwtSecretKey = process.env.JWT_SECRET_KEY;
        // console.log("Headers:", req.headers);
        const token = req.header('sebestian');
        if (!token) {
          return res.status(401).send("Token not provided");
        }
    
        const verified = jwt.verify(token, jwtSecretKey);
        if (verified) {
          next();
        } else {
          // console.log("Token not verified");
          return res.status(401).send("Token not verified");
        }
      } catch (error) {
        console.error("Error validating token:", error.message);
        return res.status(401).send("Token validation error");
      }  
};
module.exports=validationMiddleware;