const knex = require('../Configuration/knexfile');

exports.createTeam = ( async (req,res) => {
    try {
        const {team_name, player1_name, player1_eid, player2_name, player2_eid, player3_name, player3_eid,leader_email,wa_number,event_id} = req.body
        // const team = await
        const data = await knex('teams')
                    .insert({
                        team_name, 
                        player1_name, 
                        player1_eid, 
                        player2_name, 
                        player2_eid, 
                        player3_name, 
                        player3_eid,
                        leader_email,
                        wa_number,
                        event_id
                    });
        return res.status(200).json({success:true,message:"Team Registered"});
    } catch (error) {
        console.log(error);
        return res.status(503).json({success:false,message:"Internal server Error"});
    }
})