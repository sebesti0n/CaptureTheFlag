const express = require('express');
const router = express.Router();
const knex = require('knex')(require('../Configuration/knexfile')['development']);
const eventcontroller = require('../Controllers/eventController');
const answercontroller = require('../Controllers/answerController');
const eventregistrationcontroller = require('../Controllers/eventRegistrationController');
const lc = require('../Controllers/leaderBoardController');

router.get('/upcoming', eventcontroller.upcomingEvents);
router.get('/live', eventcontroller.liveEvents);
router.get('/register', eventcontroller.registeredEventforUser);
router.get('/history', eventcontroller.historyEventofUser);
router.get('/submit', answercontroller.submitAnswer);
router.get('/event-registration', eventregistrationcontroller.isUserRegisterforEvents);
router.get('/openStatus', eventregistrationcontroller.onOpenEventPage);
router.get('/eventRegistration',eventcontroller.registerUserinEvents);
router.get('/get-riddles', answercontroller.getRiddles);
router.get('/get-firstTimeRiddleNumber',answercontroller.getRiddleNumberFirsTime);
router.get('/leaderboard',lc.getLeaderBoard);
router.get('/event-details',eventcontroller.eventDetails)
module.exports = router;

