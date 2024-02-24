const knex = require("knex")(
  require("../Configuration/knexfile")["development"]
);

exports.createTeam = async (req, res) => {
  try {
    const {
      team_name,
      player1_name,
      player1_eid,
      player2_name,
      player2_eid,
      player3_name,
      player3_eid,
      leader_email,
      wa_number,
      event_id,
    } = req.body;
    console.log({
      team_name,
      player1_name,
      player1_eid,
      player2_name,
      player2_eid,
      player3_name,
      player3_eid,
      leader_email,
      wa_number,
      event_id,
    });
    const data1 = await knex("teams")
      .insert({
        team_name: team_name,
        player1_name: player1_name,
        player1_eid: player1_eid,
        player2_name: player2_name,
        player2_eid: player2_eid,
        player3_name: player3_name,
        player3_eid: player3_eid,
        leader_email: leader_email,
        wa_number: wa_number,
        event_id: event_id,
      })
      .returning("*");
      const tid = data1[0].team_id;
    const data = await knex("events")
      .where("event_id", "=", event_id)
      .returning("levels", "node_at_each_level");
    console.log("data", data);
    let seq = [];
    let lvl = data[0].levels;
    let node_at_each_level = data[0].node_at_each_level;
    for (let i = 0; i < lvl; i++) {
      seq.push(Math.floor(Math.random() * node_at_each_level));
    }

    await knex("user_event_participation").insert({
      team_id: tid,
      event_id:event_id,
      is_registered: true,
      sequence: seq,
    });
    console.log(seq);

    return res.status(200).json({ success: true, message: "Team Registered" });
  } catch (error) {
    console.log(error);
    return res
      .status(503)
      .json({ success: false, message: "Internal server Error" });
  }
};
