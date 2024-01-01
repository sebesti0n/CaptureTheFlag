const knex = require('knex')(require('../Configuration/knexfile')['development']);



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




exports.addRiddles = ( async(req,res) => {
    try {
        const rList=req.body;

        // const data = await knex('questions').insert({
        //     question_id:question_id,
        //     event_id:event_id,
        //     question:question,
        //     answer:answer,
        //     unique_code:unique_code
        // });
        console.log(rList)
        await knex('questions').insert(rList)
        const data = await knex('questions').returning('*');
        console.log("inserted Successfully");

        res.status(200).json({success: true,message:"ok",question:data});
    } catch (error) {
        console.log(error);
        res.status(500).json({success: false, message:"unknown Error!", event:null});  
    }
});


