const express = require('express');
const router = express.Router();
const adminController = require("../Controllers/adminController")
router.post('/create',adminController.addEvents);
router.post('/addTask',adminController.addQuestion);
router.get('/event',adminController.adminEvents);
router.get('/SingleEvent',adminController.adminEvent);
router.post('/addTask',)

module.exports = router