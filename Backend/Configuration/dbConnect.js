const mongoose = require('mongoose')
const dotenv =require('dotenv')
dotenv.config()

mongoose.connect(process.env.CONNECTION_URL)
.then(()=>{
console.log("Database connected successfully...");
})
.catch((e)=>{
    console.log("something went wrong while connecting with Database!");
    console.log(e);
});