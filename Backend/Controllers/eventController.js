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


exports.addEvents = ( async(req,res)=>{
    try {
        const {title , location, description, start_time, end_time, owner_id, No_of_questions, posterImage} = req.body;
       const data = await knex('events').insert({
            title:title,
            description:description,
            location:location,
            start_time:new Date(start_time),
            end_time:new Date(end_time),
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


