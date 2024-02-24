const knex = require('knex')(require('../Configuration/knexfile')['development']);

exports.onEventPageOpen = ( async (req, res) => {
    try {
        const eid = req.query.eid;
        const tid = req.query.tid;
        const data = await knex('user_event_participation')
            .select('is_registered')
            .where('event_id', eid)
            .andWhere('team_id', tid)
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
        const tid = req.query.tid;
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
            .andWhere('team_id', tid)
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
        const tid = req.query.tid;
        console.log('eid and tid - ',eid,tid);
        const data =await knex('events')
                    .where('event_id','=',eid)
                    .returning('levels','node_at_each_level');
        console.log('data', data);
        let seq = [];
        let lvl = data[0].levels;
        let node_at_each_level = data[0].node_at_each_level;
        for( let i =0; i < lvl ; i++ ){
            seq.push(Math.floor(Math.random() * node_at_each_level));
        }

            await knex('user_event_participation')
                .insert({
                    team_id: tid,
                    event_id: eid,
                    is_registered: true,
                    sequence:seq
                })
                console.log(seq);
        return res.status(200).json({success:true,message:"ok"})

    } catch (error) {
        console.log(error)
        res.status(503).json({success:false,message:"Internal Server Error"});
    }
});