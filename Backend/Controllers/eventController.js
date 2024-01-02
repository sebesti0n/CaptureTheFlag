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
    }finally{
        knex.destroy();
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
    finally{
        knex.destroy();
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
    } finally{
        knex.destroy();
    }
});




exports.isRegisterforUserforEvents = ( async(req,res) => {
    try {
        const eid =req.query.eid;
        const uid = req.query.uid;
        const is_registered = await knex('user_event_participation')
        .select('is_registered')
        .where('event_id',eid)
        .andWhere('user_id',uid)
        
        if((await is_registered).length > 0){
            if(is_registered[0]==true)
            res.status(200).json({is_registered:1});
            else {
            res.status(200).json({is_registered:0});
            }
        }
        else{
            res.status(200).json({is_registered:-1});
        }
    } catch (error) {
        res.status(403).json({is_registered:-2});
          
    } finally{
        knex.destroy();
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
    }finally{
        knex.destroy();
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
    
}finally{
    knex.destroy();
}
});