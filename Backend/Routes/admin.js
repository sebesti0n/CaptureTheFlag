const express = require('express');
const router = express.Router();
const adminController = require("../Controllers/adminController")
router.post('/create',adminController.addEvents);
// router.post('/addTask',adminController.addQuestion);
router.get('/event',adminController.adminEvents);
router.get('/event-details',adminController.adminEvent);
router.post('/addRiddles',adminController.addRiddles);

module.exports = router;