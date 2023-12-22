const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');
const User = require('../models/user');

const registrationMiddleware = async(req,res,next)=>{
    try {
        const newUser = new User(req.body);
        // console.log(newUser);
        const alreadyPresent = await User.findOne({ Email: newUser.Email });

      if (alreadyPresent) {
            return res.status(200).json({ success: false, message: 'user already registered',user:null });
        } else {
            bcrypt.hash(newUser.password, 10, (err, hashedpassword) => {
                if (err) return next(err);
                newUser.set('password', hashedpassword);
                newUser.set('cnfpassword', hashedpassword);
                let data = {
                    time: Date(),
                    userEmail: newUser.Email
                };
                const token = jwt.sign(data, process.env.JWT_SECRET_KEY);
                req.token = token;
                req.newUser = newUser;
                next();
            });
        }
    } catch (error) {
        // console.log("Error in registration middleware:", error);
        return res.status(500).json({ success: false, message: 'Internal Server Error' });
    }
};

module.exports = registrationMiddleware;