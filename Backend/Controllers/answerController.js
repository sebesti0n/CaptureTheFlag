const db = require('knex')(require('../Configuration/knexfile')['development']);


exports.submitAnswer = (async (req,res)=>{
    const answer = req.body;
    try {
        await db('answers_history')
        .insert(answer);
        const data = await db('answers_history').returning('*');

        res.status(200).json({success:true,message:"ok",answers:data});
        
    } catch (error) {
        console.log(error);
        res.status(403).json({success:false,message:"server unavailable"});
    }finally{
        db.destroy();
    }
});
