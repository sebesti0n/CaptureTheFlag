const express = require('express')
const routes=require('./Routes/auth')
const eventRouter = require('./Routes/event')
const adminRouter = require('./Routes/admin');
const cors = require('cors');
const validationMiddleware = require('./middlewares/validationMiddleware');
const knex = require('knex')(require('./Configuration/knexfile')['development']);
// const client = require('./Configuration/redisConfig.js');
const app= express();
const PORT = process.env.PORT || 3000;
app.use(cors());
app.use((req, res, next) => {
  console.log(`${req.method} ${req.url}`);
  next();
});
app.use(express.json());
app.use(express.urlencoded({ extended: true }));
app.use('/', routes);
app.use('/event', eventRouter);
app.use('/admin', adminRouter);
app.listen(PORT, '0.0.0.0', () => {
  console.log('Listening to 3000');
});
