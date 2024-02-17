const express = require('express');
const router = express.Router();
const knex = require('knex')(require('../Configuration/knexfile')['development']);
const eventcontroller = require('../Controllers/eventController');
const answercontroller = require('../Controllers/answerController');
const eventregistrationcontroller = require('../Controllers/eventRegistrationController');
const qc = require('../Controllers/queryController');
const lc = require('../Controllers/leaderBoardController');
const teamController = require('../Controllers/teamController')

router.get('/all', eventcontroller.upcomingEvents);
router.get('/live', eventcontroller.liveEvents);
router.get('/register', eventcontroller.registeredEventforUser);
router.get('/history', eventcontroller.historyEventofUser);
router.get('/submit', answercontroller.submitAnswer);
router.get('/event-registration', eventregistrationcontroller.isUserRegisterforEvents);
router.get('/eventRegistration',eventcontroller.registerUserinEvents);
router.get('/leaderboard',lc.getLeaderBoard);


router.get('/event-details',eventcontroller.eventDetails)
router.get('/submission',qc.submissionRiddle);
router.get('/start-contest',qc.startEvent);
router.post('/register-team',teamController.createTeam)

router.get('/curr-status-riddle',eventcontroller.getCurrentriddleStatus);

module.exports = router;

