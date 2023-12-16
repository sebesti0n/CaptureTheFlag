const express = require('express')
const mongoose = require('mongoose')
require('./connections/dbConnect')
const user = require('./models/user')
const app= express()
const PORT = 3000 


app.listen(PORT,'0.0.0.0',()=>{
    console.log("Listening to 3000")
})