const express = require('express');
const router = express.Router();
const knex = require('knex')(require('../Configuration/knexfile')['development']);
const eventcontroller = require('../Controllers/eventController');

router.get('/upcoming',eventcontroller.upcomingEvents);
router.get('/live',eventcontroller.liveEvents);
router.post('/create',eventcontroller.addEvents);
router.post('/addTask',eventcontroller.addQuestion);

module.exports = router;

