const db = require("knex")(require("../Configuration/knexfile")["development"]);

exports.getLeaderBoard = async (req, res) => {
  const eid = req.query.eid;
  try {
    const data = await db("user_event_participation")
      .innerJoin(
        "teams",
        "teams.team_id",
        "=",
        "user_event_participation.team_id"
      )
      .where("user_event_participation.event_id", "=", eid)
      .orderByRaw(
        "ABS(user_event_participation.start_time-user_event_participation.end_time) DESC"
      )
      .returning(
        "user_event_participation.team_id",
        "users.name",
        "user_event_participation.start_time",
        "user_event_participation.end_time"
      );
    return res.status(200).json({ success: true, message: "ok", list: data });
  } catch (error) {
    console.log(error);
    res
      .status(503)
      .jsonC({ success: false, message: "server unavailable", list: null });
  }
};
