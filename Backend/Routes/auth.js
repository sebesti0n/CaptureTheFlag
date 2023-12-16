const express = require('express');
const router = express.Router();
const bcrypt = require('bcrypt');
require('../connections/dbConnect')
const user = require('../models/user')
router.get('/', (req, res) => {
    res.send("Om Namah Shivaay");
})
router.post('/register', async (req, res, next) => {
    const newUser = await new user(req.body);
    const alreadyPresent = await user.findOne({ Email: newUser.Email })
    if (newUser.password !== newUser.cnfpassword) {
        return res.status(200).json({message:'confirm password does not match with password'});
    }
    else if (alreadyPresent) {
        return res.status(200).json({message:'user already registered'});
    }
    else {
        bcrypt.hash(newUser.password, 10, (err, hashedpassword) => {
            if (err) return next(err);
            newUser.set('password', hashedpassword);
            newUser.set('cnfpassword', hashedpassword);
            newUser.save().then(() => {
                console.log("all is well");
                return res.status(200).json(newUser);
            }).catch((e) => {
                console.log("dikkat");
                console.log(e);
                return res.status(400).send(e);
            })
        });
    }
});


router.get('/login/:useremail/:pwd', async (req, res) => {
    try {
        const userEmail = req.params.useremail;
        const userPass = req.params.pwd;
        const finduser = await user.findOne({ Email: userEmail });
        if (finduser) {
            const validate = await bcrypt.compare(userPass, finduser.password);
            if (!validate) {
                res.status(403).json({ message: 'wrong password' });
            }
            else res.status(200).json(finduser);
        }
        else {
            res.status(200).json({message:'No User Found'});
        }
    } catch (error) {
        console.log(error);
        res.status(500).send({message:'Internal Server Error'});

    }

});

module.exports = router;