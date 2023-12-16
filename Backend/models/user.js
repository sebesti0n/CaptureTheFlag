const mongoose =require('mongoose')
const validator = require('validator');

const user = mongoose.Schema({
    FirstName:{
        type:String,
        required:true
    },
    LastName:{
        type:String,
        required:true
    },
    MobileNo:{
        type:String,
        length:10,
        required:true
    },
    CollegeName:{
        type:String,
        required:true
    },
    Email:{
        type:String,
        required:true,
        validator(val){
            if(!validator.isEmail(val)){
                throw new Error("Invalid Email ID");
            }
        }
    },
    password:{
        type:String,
        required:true
    },
    cnfpassword:{
        type:String,
        required:true
    }
})
const users = new mongoose.model("User",user)
module.exports = users