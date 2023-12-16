const express = require('express');
const router = express.Router();
require('../connections/dbConnect')
const user = require('../models/user')
router.get('/',(req,res)=>{
    res.send("Om Namah Shivaay");
})
router.post('/register', async (req,res)=>{
const newUser = await new user(req.body);
newUser.save().then(()=>{
    console.log("all is well");
    res.status(200).send(newUser);
}).catch((e)=>{
    console.log("dikkat");
    console.log(e);
    res.status(400).send(e);
})


});
router.get('/login/:useremail',async (req,res)=>{
    try {
        const userEmail = req.params.useremail;
const finduser = await user.findOne({Email:userEmail});
if(finduser){
    res.status(200).json(finduser);
}
else{
    res.status(200).send("No User Found");
}
    } catch (error) {
        console.log(error);
        res.status(500).send("Internal Server Error");
        
    }

});

module.exports = router;