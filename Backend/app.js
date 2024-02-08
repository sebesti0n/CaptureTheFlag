const express = require('express')
const routes=require('./Routes/auth')
const eventRouter = require('./Routes/event')
const adminRouter = require('./Routes/admin')
const knex = require('knex')(require('./Configuration/knexfile')['development']);
const app= express();
const PORT = 3008;

app.use(express.json());
app.use(express.urlencoded({extended:true}));
app.use('/',routes);
app.use('/event',eventRouter);
app.use('/admin',adminRouter);
app.listen(PORT,'0.0.0.0',()=>{
    console.log("Listening to 3000");
});