const db = require('knex')(require('../Configuration/knexfile')['development']);


exports.getLeaderBoard = ( async (req, res) => {
    const eid = req.query.eid;
    try {
        const data = await db('user_event_participation')
                            .where('event_id','=',eid)
                            .orderByRaw('ABS(start_time-end_time) DESC').returning('user_id','start_time','end_time');
        return res.status(200).json({success:true, message:"ok", list:data });
    } catch (error) {
        console.log(error);
        res.status(503).jsonC({success:false,message:"server unavailable",list:null});
        
        
    }
});