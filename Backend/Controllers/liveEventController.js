const knex = require('knex')(require('../Configuration/knexfile')['development']);

exports.addAnswers = (async (req,res)=>{
    try {
        const {user_id,question_id,answer_text} = req.body;
        const data = await knex('answer_history').insert({
        user_id:user_id,
        question_id:question_id,
        answer_text:answer_text
        }).returning('*');
        res.status(200).json({success: true,message:"ok",event:data});
    } catch (error) {
        console.log(error);
        res.status(500).json({success: false, message:"unknown Error!", event:null});
    }  
});


