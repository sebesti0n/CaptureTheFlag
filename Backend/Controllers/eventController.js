const knex = require('knex')(require('../Configuration/knexfile')['development']);

exports.upcomingEvents = (async (req, res) => {
    try {
        const currentDate = new Date();
        const currTime = currentDate.getTime()
        console.log(currTime);
        const events = await knex('events')
            .where('start_ms', '>=', currTime)
            .orWhere('end_ms', '>=', currTime).returning('*');

        console.log(events);
        res.status(200).json({ success: true, message: "ok", event: events });
    } catch (err) {
        console.log(err);
        res.status(500).json({ success: false, message: "unknown Error!", event: null });
    }
});




exports.liveEvents = (async (req, res) => {
    try {
        const currentDate = new Date();
        const currTime = currentDate.getTime()
        console.log(currTime);
        const events = await knex('events')
            .select('*')
            .where('start_ms', '<=', currTime)
            .andWhere('end_ms', '>=', currTime);
        res.status(200).json({ success: true, message: "ok", event: events });
    } catch (error) {
        console.log(error)
        res.status(500).json({ success: false, message: "unknown Error!", event: null });
    }

});



exports.registeredEventforUser = (async (req, res) => {
    const uid = req.query.uid
    try {
        const event = await knex('events')
            .innerJoin('user_event_participation', 'events.event_id', '=', 'user_event_participation.event_id')
            .where('user_event_participation.user_id', '=', uid)
            .andWhere('user_event_participation.is_registered', '=', false)
            .select('events.event_id', 'events.title', 'events.location', 'events.description', 'events.start_time', 'events.end_time', 'events.owner_id', 'events.No_of_questions', 'events.posterImage', 'events.organisation')
        console.log(event);
        res.status(200).json({ success: true, message: "ok", event: event });

    } catch (error) {
        console.log(error);
        res.status(505).json({ success: false, message: "something went wrong", event: null });
    }
});

exports.historyEventofUser = (async (req, res) => {
    try {
        const uid = req.query.uid
        const currentDate = new Date();
        const currTime = currentDate.getTime()
        console.log(currTime);
        const event = await knex('events')
            .innerJoin('user_event_participation', 'events.event_id', '=', 'user_event_participation.event_id')
            .where('user_event_participation.user_id', '=', uid)
            .andWhere('user_event_participation.is_registered', '=', false)
            .andWhere('events.end_ms', '<', currTime)
            .select('events.event_id', 'events.title', 'events.location', 'events.description', 'events.start_time', 'events.end_time', 'events.owner_id', 'events.No_of_questions', 'events.posterImage', 'events.organisation')
        console.log(event);
        res.status(200).json({ success: true, message: "ok", event: event });
    } catch (error) {
        console.log(error);
        res.status(505).json({ success: false, message: "something went wrong", event: null });

    }
});



exports.registerUserinEvents = (async (req, res) => {
    try {
        const { team_id, event_id, registration_time, is_registered, start_time, end_time, Number_correct_answers, Rank } = req.body;
        const data = await knex('user_event_participation').insert({
            team_id: team_id,
            event_id: event_id,
            registration_time: registration_time,
            is_registered: is_registered,
            start_time: start_time,
            end_time: end_time,
            Number_correct_answers: Number_correct_answers,
            Rank: Rank
        }).returning('*');
        res.status(200).json({ success: true, message: "ok", event: data });
    } catch (error) {
        console.log(error);
        res.status(500).json({ success: false, message: "unknown Error!", event: null });
    }
});


exports.eventDetails = (async (req,res)=>{
    try {
        const eid = req.query.eid;
        const tid = req.query.tid;
        const events = await knex('events')
        .select('*')
        .where('event_id','=',eid);
        const data = await knex('user_event_participation')
            .select('is_registered')
            .where('event_id', eid)
            .andWhere('team_id', tid)
        let is_register=false;
        if(data.length>0)is_register=true;
        res.status(200).json({success: true,message:"ok",isRegister:is_register,event:events});
    } catch (error) {
        res.status(500).json({success: false, message:"unknown Error!",isRegister:is_register, event:null});
    }
});

