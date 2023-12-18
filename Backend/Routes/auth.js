const express = require('express');
const router = express.Router();
const dotenv =require('dotenv');
const DB = require('../connections/dbConnect');
const loginMiddleware = require('../middlewares/loginMiddleware');
const registrationMiddleware = require('../middlewares/registrationMiddleware');
const validationMiddleware = require('../middlewares/validationMiddleware');


dotenv.config()


router.get('/', (req, res) => {
    return res.send("Om Namah Shivaay");
});


router.post('/register',registrationMiddleware, (req, res) => {
            const {newUser , token } = req;
            newUser.save().then(() => {
                console.log("all is well");
                return res.status(200).json({token:token, user: newUser});
            }).catch((e) => {
                console.log("dikkat");
                console.log(e);
                return res.status(400).send(e);
            })
});

router.post('/login',loginMiddleware, (req, res) => {
    const { finduser } = req;
    return res.status(200).json({success: true,message:"ok",userDetails:finduser});
});

router.get('/validate',validationMiddleware , (req, res) => {
        return res.send("Successfully Verified");
      
  });
module.exports = router;