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

exports.adminEvents = (async (req,res)=>{
    try {
        const oid = req.query.owner
        const events = await knex('events')
        .select('*')
        .where('owner_id','=',oid);
        res.status(200).json({success: true,message:"ok",event:events});
    } catch (error) {
        res.status(500).json({success: false, message:"unknown Error!", event:null});
    }
});

exports.adminEvent = (async (req,res)=>{
    try {
        const eid = req.query.eid
        const currTime = new Date();
        const events = await knex('events')
        .select('*')
        .where('event_id','=',eid);
        res.status(200).json({success: true,message:"ok",event:events});
    } catch (error) {
        res.status(500).json({success: false, message:"unknown Error!", event:null});
    }
});

function getIstTimestamp(dateTimeString){
const timestamp = Date.parse(dateTimeString);
const time = timestamp / 1000;
const date = new Date(time * 1000);
const utcTime = date.getTime();
const istTime = utcTime + (5.5 * 60 * 60 * 1000);
const istDate = new Date(istTime);
const formattedIST = istDate.toLocaleString('en-US', {
    timeZone: 'Asia/Kolkata',
    hour12: false, 
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
});

return formattedIST;

}

exports.addEvents = ( async(req,res)=>{
    try {
        const {title , location, description,organisation, start_time, end_time, owner_id, No_of_questions, posterImage} = req.body;
        const startTimeDate = await getIstTimestamp(start_time);
        const endTimeDate = await getIstTimestamp(end_time);

        const data = await knex('events').insert({
            title:title,
            description:description,
            location:location,
            organisation:organisation,
            start_time:startTimeDate,
            end_time:endTimeDate,
            owner_id:owner_id,
            No_of_questions:No_of_questions,
            posterImage:posterImage
        }).returning('*');
        res.status(200).json({success: true,message:"ok",event:data});
    } catch (error) {
        console.log(error);
        res.status(500).json({success: false, message:"unknown Error!", event:null});
    }
});


exports.addQuestion = ( async(req,res) => {
    try {
        const{question_id,event_id,question,answer,unique_code}=req.body;
        const data = await knex('questions').insert({
            question_id:question_id,
            event_id:event_id,
            question:question,
            answer:answer,
            unique_code:unique_code
        }).returning('*');
        res.status(200).json({success: true,message:"ok",question:data});
    } catch (error) {
        console.log(error);
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


