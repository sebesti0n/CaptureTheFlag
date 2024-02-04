const db = require('knex')(require('../Configuration/knexfile')['development']);


exports.submitAnswer = (async (req,res)=>{
    const uid=req.query.uid;
    const eid = req.query.eid;
    try {
        const currentDate = new Date();
        const ISTOffset = 330 * 60 * 1000;
        const ISTTime = new Date(currentDate.getTime() + ISTOffset).toISOString();
        console.log('Current time in IST:', ISTTime);
        const data = await db('user_event_participation')
        .where('user_id',uid)
        .andWhere('event_id',eid)
        .update({end_time:currentDate})
        .increment('Number_correct_answer',1).returning('Number_correct_answer');
        if(data.length>0)
        res.status(200).json({next:data[0].Number_correct_answer});
        else
        res.status(200).json({next:-1});

        
    } catch (error) {
        console.log(error);
        res.status(403).json({next:-1});
    }
});



exports.getRiddles = (async (req,res) => {
    const eid = req.query.eid;
    try {
        const data = await db('questions')
        .select('*')
        .where('event_id',eid)
        
        res.status(200).json({success:true,message:"ok",riddles:data});
    } catch (error) {
        console.log(error);
        res.status(403).jsonC({success:false,message:"server unavailable",riddles:null});
   
    }
})

exports.getRiddleNumberFirsTime = ( async ( req , res ) => {
    const uid = req.query.uid;
    const eid = req.query.eid;
    try{
        const data = await db('user_event_participation')
        .where('user_id',uid)
        .andWhere('event_id',eid)
        .returning('Number_correct_answer');
        if(data.length>0)
        res.status(200).json({next:data[0].Number_correct_answer});
        else
        res.status(200).json({next:-1});
    }
    catch(error){
        console.log(error);
        res.status(503).json({next:-1});
    }
});
