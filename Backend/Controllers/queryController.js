const knex = require("knex")(
  require("../Configuration/knexfile")["development"]
);
exports.submissionRiddle = async (req, res) => {
  try {
    const end_time = req.query.et;
    const eid = req.query.eid;
    const tid = req.query.tid;
    const data = await knex("user_event_participation")
      .where("event_id", "=", eid)
      .andWhere("team_id", "=", tid)
      .update({
        end_time: end_time,
      })
      .increment("Number_correct_answer", 1)
      .returning("Number_correct_answer");
    if (data.length == 0) {
      return res
        .status(200)
        .json({ success: true, message: "Next Riddle", next: 0 });
    } else
      return res
        .status(200)
        .json({ success: true, message: "Next Riddle", next: data[0] });
  } catch (error) {
    console.log(error);
    return res
      .status(500)
      .json({ success: false, message: "unknown Error!", next: null });
  }
};

exports.startEvent = async (req, res) => {
  try {
    const rid = req.query.rid;
    const eid = req.query.eid;
    const start_time = req.query.st;
    const team = await knex('teams')
                .where("player1_eid",rid)
                .orWhere("player2_eid",rid)
                .orWhere("player3_eid",rid)
                .returning('team_id');
    console.log(team)
    const tid = team[0].team_id;    
        await knex("user_event_participation")
          .whereNull("start_time")
          .orWhere("start_time", "=", 0)
          .andWhere("event_id", "=", eid)
          .andWhere("team_id", "=", tid)
          .update({
            start_time: start_time,
            end_time: start_time,
          });

        const data = await knex("user_event_participation")
          .where("event_id", "=", eid)
          .andWhere("team_id", "=", tid)
          .returning("*");
        console.log("data", data);
        let next = 0;
        let seq;
        let rList = [];

        if (data.length != 0) {
          seq = [...data[0].sequence];
          next = data[0].Number_correct_answer;
        } else {
          seq = [];
        }
        console.log("seq", seq);
        for (let i = 0; i < seq.length; i++) {
          const level = await knex("questions")
            .where("event_id", "=", eid)
            .andWhere("level", "=", i + 1)
            .orderBy(["level", "question_id"])
            .returning("*");

          rList.push(level[seq[i]]);
        }
        const jsonData = {
          success: true,
          message: "Next Riddle",
          next: data[0],
          rList: rList,
        };
        return res.status(200).json(jsonData);
      
    }
  catch (error) {
    console.log(error);
    return res
      .status(500)
      .json({ success: false, message: "unknown Error!", next: null });
  
}
}
