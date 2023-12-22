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
        const {title , location, description, start_time, end_time, owner_id, No_of_questions} = req.body;
       const data = await knex('events').insert({
            title:title,
            description:description,
            location:location,
            start_time:new Date(start_time),
            end_time:new Date(end_time),
            owner_id:owner_id,
            No_of_questions:No_of_questions
        }).returning('*');
        res.status(200).json({success: true,message:"ok",event:data});
    } catch (error) {
        console.log(error);
        res.status(500).json({success: false, message:"unknown Error!", event:null});
    }
});


