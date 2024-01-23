const knex = require('knex')(require('../Configuration/knexfile')['development']);


exports.upcomingEvents = (async (req,res)=>{
    try{
        const currTime = new Date();
        const events = await knex('events')
        .select('*')
        .where('start_time','>',currTime.toISOString());
        res.status(200).json({success: true,message:"ok",event:events});
    }catch (err) {
        res.status(500).json({success: false,message:"unknown Error!",event:null});
    }
});




exports.liveEvents = ( async(req,res)=>{
    try {
        const currTime = new Date();
        const events = await knex('events')
        .select('*')
        .where('start_time','<=',currTime.toISOString())
        .andWhere('end_time','>=',currTime.toISOString());
        res.status(200).json({success: true,message:"ok",event:events});
    } catch (error) {
        res.status(500).json({success: false, message:"unknown Error!", event:null});
    }
    
});




exports.registerUserinEvents = ( async(req,res) => {
    try {
        const {user_id, event_id, registration_time, is_registered, start_time, end_time, Number_correct_answers, Rank} = req.body;
        const data = await knex('user_event_participation').insert({
            user_id:user_id, 
            event_id:event_id, 
            registration_time:registration_time, 
            is_registered:is_registered, 
            start_time:start_time, 
            end_time:end_time,
            Number_correct_answers:Number_correct_answers, 
            Rank:Rank
        }).returning('*');
        res.status(200).json({success: true,message:"ok",event:data});
    } catch (error) {
        console.log(error);
        res.status(500).json({success: false, message:"unknown Error!", event:null});
    } 
});




exports.isUserRegisterforEvents = ( async(req,res) => {
    try {
        const eid =req.query.eid;
        const uid = req.query.uid;
        const is_register = await knex('user_event_participation')
        .select('is_registered')
        .where('event_id',eid)
        .andWhere('user_id',uid)
        console.log(is_register);
        const eventDetails = await knex('events')
            .select('*')
            .where('event_id',eid)
        const isLive = false
        if(eventDetails.length>0){
        const startTime = eventDetails.start_time
        const endTime = eventDetails.end_time
        const currTime = new Date()
        if(startTime<=currTime.toISOString&&endTime>=currTime.toISOString){
            isLive=true;
        }
    }
        if(is_register.length > 0){
            const flg = is_register[0].is_registered
        
            if(flg){
                await knex('user_event_participation')
                        .update({is_registered:false})
                        .where('event_id', eid)
                .andWhere('user_id', uid);
                res.status(200).json({is_registered:0});
            
        }else {
            await knex('user_event_participation')
                        .update({is_registered:true})
                        .where('event_id', eid)
                .andWhere('user_id', uid);
            res.status(200).json({is_registered:1});
            }
        }
        else{
           await knex('user_event_participation')
            .insert({
                user_id:uid, 
                event_id:eid, 
                is_registered:true
            })
            res.status(200).json({is_registered:1});
        }
    } catch (error) {
        console.log(error)
        res.status(403).json({is_registered:-2});
          
    } 
});


exports.registeredEventforUser = (async(req, res) => {
    const uid = req.query.uid
    try {
        const event = await knex('events')
                .innerJoin('user_event_participation','events.event_id','=','user_event_participation.event_id')
                .where('user_event_participation.user_id','=',uid)
                .andWhere('user_event_participation.is_registered','=',false)
                .select('events.event_id','events.title','events.location','events.description','events.start_time','events.end_time','events.owner_id','events.No_of_questions','events.posterImage','events.organisation')
                console.log(event);
                res.status(200).json({success:true,message:"ok",event:event});
        
    } catch (error) {
        console.log(error);
        res.status(505).json({success:false,message:"something went wrong",event:null});
    }
});

exports.historyEventofUser = ( async(req, res) => {
try {
    const uid = req.query.uid
    const currTime = new Date()
    const event = await knex('events')
                .innerJoin('user_event_participation','events.event_id','=','user_event_participation.event_id')
                .where('user_event_participation.user_id','=',uid)
                .andWhere('user_event_participation.is_registered','=',false)
                .andWhere('events.end_time','<',currTime.toISOString)
                .select('events.event_id','events.title','events.location','events.description','events.start_time','events.end_time','events.owner_id','events.No_of_questions','events.posterImage','events.organisation')
                console.log(event);
                res.status(200).json({success:true,message:"ok",event:event});
} catch (error) {
        console.log(error);
        res.status(505).json({success:false,message:"something went wrong",event:null});
    
}
});

exports.onOpenEventPage = (async (req,res)=>{
    try{
    const eid =req.query.eid;
    const uid = req.query.uid;
    const eventDetails = await knex('events')
            .select('*')
            .where('event_id',eid)
    const isLive = false
    if(eventDetails.length>0){
        const startTime = eventDetails.start_time
        const endTime = eventDetails.end_time
        const currTime = new Date()
        if(startTime<=currTime.toISOString&&endTime>=currTime.toISOString){
            isLive=true;
        }
    }
    const data = await knex('user_event_participation')
        .select('is_registered')
        .where('event_id',eid)
        .andWhere('user_id',uid)
        if(data.length>0){
            const flg = data[0].is_registered;
            if(flg&&isLive){
                res.status(200).json({is_registered:1});
            }
            else if(flg&&!isLive){
                res.status(200).json({is_registered:2});
            }
            else{
                res.status(200).json({is_registered:0});
            }
        }else{
            res.status(200).json({is_registered:0});
        }
        // console.log(data[0].is_registered);
        // res.json(data);
    } catch(err){
        console.log(err);
        res.status(505).json({success:false,message:"something went wrong",event:null});
    }


});