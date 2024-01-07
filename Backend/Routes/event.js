const express = require('express');
const router = express.Router();
const knex = require('knex')(require('../Configuration/knexfile')['development']);
const eventcontroller = require('../Controllers/eventController');
const answercontroller = require('../Controllers/answerController');

router.get('/upcoming',eventcontroller.upcomingEvents);
router.get('/live',eventcontroller.liveEvents);
router.get('/register',eventcontroller.registeredEventforUser)
router.get('/history',eventcontroller.historyEventofUser)
router.get('/submit',answercontroller.submitAnswer);
router.get('/registration/status',eventcontroller.isUserRegisterforEvents);
router.get('/openStatus',eventcontroller.onOpenEventPage);
router.get('/get-riddles',answercontroller.getRiddles);
module.exports = router;

