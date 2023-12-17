const express = require('express');
const router = express.Router();
const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');
const dotenv =require('dotenv')
dotenv.config()
require('../connections/dbConnect')
const user = require('../models/user')
router.get('/', (req, res) => {
    res.send("Om Namah Shivaay");
})
router.post('/register', async (req, res, next) => {
    const newUser = await new user(req.body);
    const alreadyPresent = await user.findOne({ Email: newUser.Email })
    if (newUser.password !== newUser.cnfpassword) {
        return res.status(200).json({success:false, message:'confirm password does not match with password'});
    }
    else if (alreadyPresent) {
        return res.status(200).json({success:false, message:'user already registered'});
    }
    else {
        bcrypt.hash(newUser.password, 10, (err, hashedpassword) => {
            if (err) return next(err);
            newUser.set('password', hashedpassword);
            newUser.set('cnfpassword', hashedpassword);
            let data={
                time:Date(),
                userEmail:newUser.Email
            }
            const token = jwt.sign(data, process.env.JWT_SECRET_KEY);
            newUser.save().then(() => {
                console.log("all is well");
                return res.status(200).json({token:token, user: newUser});
            }).catch((e) => {
                console.log("dikkat");
                console.log(e);
                return res.status(400).send(e);
            })
        });
    }
});


router.post('/login', async (req, res, next) => {
    const {userEmail, userPass} = req.body;
    try {
        const finduser = await user.findOne({ Email: userEmail });
        if (finduser) {
            const validate = await bcrypt.compare(userPass, finduser.password);
            if (!validate) {
                res.status(403).json({ success : false, message: 'Invalid credential' });
            }
            else res.status(200).json({success: true,userDetails:finduser});
        }
        else {
            res.status(200).json({success:false, message:'No User Found'});
        }
    } catch (error) {
        console.log(error);
        res.status(500).send({success:false, message:'Internal Server Error'});

    }

});


router.get("/validate", (req, res) => {
    try {
      let tokenHeaderKey = process.env.TOKEN_HEADER_KEY;
      let jwtSecretKey = process.env.JWT_SECRET_KEY;
      console.log("Headers:", req.headers);
      const token = req.header('sebestian');
      if (!token) {
        return res.status(401).send("Token not provided");
      }
  
      const verified = jwt.verify(token, jwtSecretKey);
      if (verified) {
        return res.send("Successfully Verified");
      } else {
        console.log("Token not verified");
        return res.status(401).send("Token not verified");
      }
    } catch (error) {
      console.error("Error validating token:", error.message);
      return res.status(401).send("Token validation error");
    }
  });

module.exports = router;