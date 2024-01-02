const db = require('knex')(require('../Configuration/knexfile')['development']);


exports.submitAnswer = (async (req,res)=>{
    const uid=req.query.uid
    try {
        const currentDate = new Date();
        const ISTOffset = 330 * 60 * 1000;
        const ISTTime = new Date(currentDate.getTime() + ISTOffset).toISOString();
        console.log('Current time in IST:', ISTTime);
        const data = await db('user_event_participation')
        .where({user_id:uid})
        .update({end_time:currentDate})
        .increment('Number_correct_answer',1).returning('*');

        res.status(200).json({success:true,message:"ok",answers:data});
        
    } catch (error) {
        console.log(error);
        res.status(403).json({success:false,message:"server unavailable"});
    }
});
