const express = require('express');
const router = express.Router();
const dotenv = require('dotenv');
const loginMiddleware = require('../middlewares/loginMiddleware');
const registrationMiddleware = require('../middlewares/registrationMiddleware');
const validationMiddleware = require('../middlewares/validationMiddleware');
const userController = require('../Controllers/userController');
const knex = require('knex')(
  require('../Configuration/knexfile')['development']
);

dotenv.config();
router.get('/', async (req, res) => {
  return res.send('Welcome to Capture the Flag');
});
router.post(
  '/register',
  registrationMiddleware,
  userController.userRegistration
);
router.post('/login', loginMiddleware, (req, res) => {
  const { finduser } = req;
  return res
    .status(200)
    .json({ success: true, message: 'ok', userDetails: finduser[0] });
});
router.get('/validate', validationMiddleware, (req, res) => {
  return res.send('Successfully Verified');
});
module.exports = router;
