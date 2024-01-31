const knex = require('knex')(require('../Configuration/knexfile')['development']);

exports.onEventPageOpen = ( async (req, res) => {
    try {
        const eid = req.query.eid;
        const uid = req.query.uid;
        const data = await knex('user_event_participation')
            .select('is_registered')
            .where('event_id', eid)
            .andWhere('user_id', uid)
        if(data.length>0 && data[0].is_registered){
            return res.status(200).json({buttonText:"Unregister"});
        }
        else{
            return res.status(200).json({buttonText:"Register"});
        }

    } catch (error){
        console.log(error)
        res.status(503).json({ buttonText:"Internal Server Error" });
    }
});

exports.onOpenEventPage = (async (req, res) => {
    try {
        const eid = req.query.eid;
        const uid = req.query.uid;
        const eventDetails = await knex('events')
            .select('*')
            .where('event_id', eid)
        var isLive = false
        if (eventDetails.length > 0) {
            const start_ms = eventDetails[0].start_ms
            const end_ms = eventDetails[0].end_ms
            const currentDate = new Date();
            const currTime = currentDate.getTime()
            console.log(currTime);
            if (start_ms <= currTime && end_ms >= currTime) {
                isLive = true;
            }
        }
        const data = await knex('user_event_participation')
            .select('is_registered')
            .where('event_id', eid)
            .andWhere('user_id', uid)
        if (data.length > 0) {
            const flg = data[0].is_registered;
            if (flg && isLive) {
            return res.status(200).json({buttonText:"Start"});
            }
            else if (flg && !isLive) {            
                return res.status(200).json({buttonText:"Unregister"});

            }
            else {
                return res.status(200).json({buttonText:"Unregister"});
            }
        } else {
            return res.status(200).json({buttonText:"Unregister"});
        }
        // console.log(data[0].is_registered);
        // res.json(data);
    } catch (err) {
        console.log(err);
        return res.status(200).json({buttonText:"Error"});
    }


});


exports.isUserRegisterforEvents = (async (req, res) => {
    try {
        const eid = req.query.eid;
        const uid = req.query.uid;
        const is_register = await knex('user_event_participation')
            .select('is_registered')
            .where('event_id', eid)
            .andWhere('user_id', uid)
        console.log(is_register);
        const eventDetails = await knex('events')
            .select('*')
            .where('event_id', eid)
            console.log(eventDetails)
        var isLive = false
        if (eventDetails.length > 0) {
            const start_ms = eventDetails[0].start_ms
            const end_ms = eventDetails[0].end_ms
            console.log(start_ms,end_ms);
            const currentDate = new Date();
            const currTime = currentDate.getTime()
            console.log(currTime);
            if (start_ms <= currTime && end_ms >= currTime) {
                isLive = true;
            }
        }
        if (is_register.length > 0) {
            const flg = is_register[0].is_registered
            if(isLive&&flg){
                return res.status(200).json({buttonText:"Start"})
            }
            else if(isLive&&!flg){
                await knex('user_event_participation')
                    .update({ is_registered: true })
                    .where('event_id', eid)
                    .andWhere('user_id', uid);
                return res.status(200).json({buttonText:"Start"})
            }
            else if (flg&&!isLive) {
                await knex('user_event_participation')
                    .update({ is_registered: false })
                    .where('event_id', eid)
                    .andWhere('user_id', uid);
                return res.status(200).json({buttonText:"Register"});

            } else {
                await knex('user_event_participation')
                    .update({ is_registered: true })
                    .where('event_id', eid)
                    .andWhere('user_id', uid);
                return res.status(200).json({buttonText:"Unregister"});
            }
        }
        else {
            await knex('user_event_participation')
                .insert({
                    user_id: uid,
                    event_id: eid,
                    is_registered: true
                })
            if(isLive){
                return res.status(200).json({buttonText:"Start"})
            }
            else return res.status(200).json({buttonText:"Unregister"});
        }
    } catch (error) {
        console.log(error)
                res.status(503).json({buttonText:"Error"});

    }
});