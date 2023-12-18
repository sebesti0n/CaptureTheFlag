const express = require('express')
const routes=require('./Routes/auth')
const app= express()
const PORT = 3008 
app.use(express.json());
app.use('/',routes);
app.listen(PORT,'0.0.0.0',()=>{
    console.log("Listening to 3000")
})