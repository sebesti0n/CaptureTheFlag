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
    const regId = req.query.rid;
    const eid = req.query.eid;
    const start_time = req.query.st;
    const team = await knex("teams")
      .where("player1_eid", regId)
      .orWhere("player2_eid", regId)
      .orWhere("player3_eid", regId)
      .returning("team_id");
    console.log(team);
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
        .returning(
          "question_id",
          "event_id",
          "question",
          "answer",
          "unique_code",
          "storyline",
          "level",
          "Hint1",
          "Hint2",
          "Hint3",
          "Latitude",
          "Longitude",
          "Range",
          "imageLink"
        );

      rList.push(level[seq[i]]);
    }
    const isRiddleavailable = await knex("answers_history")
      .where("question_id", "=", rList[0].question_id)
      .andWhere("event_id", "=", eid)
      .andWhere("teamId", "=", tid)
      .count("*");

    const count = isRiddleavailable[0].count;
    if (count == 0) {
      await knex("answers_history").insert({
        question_id: rList[0].question_id,
        event_id: eid,
        teamId: tid,
        startTime: start_time,
      });
    }
    const jsonData = {
      success: true,
      message: "Next Riddle",
      next: data[0],
      rList: rList,
    };
    return res.status(200).json(jsonData);
  } catch (error) {
    console.log(error);
    return res
      .status(500)
      .json({ success: false, message: "unknown Error!", next: null });
  }
};

exports.onSubmit = async (req, res) => {
  try {
    const { eid, tid, currRid, nextRid, unqCode, answer } = req.body;
    console.log({
      eid,
      tid,
      currRid,
      nextRid,
      unqCode,
      answer,
    });
    const currentTime = Date.now();

    await knex.transaction(async (trx) => {
      const pointsArray = await trx("questions")
        .where({
          question_id: currRid,
          unique_code: unqCode,
          answer: answer,
        })
        .select("point");
      console.log("pointsArray", pointsArray);
      if (pointsArray.size == 0) {
        return res
          .status(200)
          .json({ success: true, message: "Wrong Answer", next: -1 });
      }
      const point = pointsArray[0].point;
      const currentRiddleStartMs = await trx("answers_history")
        .where({
          endTime: 0,
          teamId: tid,
          event_id: eid,
          question_id: currRid,
        })
        .update({ endTime: currentTime })
        .returning("startTime");
      console.log("currentRiddleStartMs", currentRiddleStartMs);

      const earnedPoint = Math.max(
        250,
        Math.floor(
          point -
            ((Date.now() - currentRiddleStartMs[0].startTime) / 600000) * 10
        )
      );
      if (nextRid != -1) {
        await trx("answers_history").insert({
          question_id: nextRid,
          event_id: eid,
          teamId: tid,
          startTime: currentTime,
        });
      }

      const nextIndex = await trx("user_event_participation")
        .where({
          team_id: tid,
          event_id: eid,
        })
        .increment("Number_correct_answer")
        .update({ end_time: currentTime })
        .returning("Number_correct_answer");

      console.log("nextIndex", nextIndex);

      await trx("pointsTable")
        .insert({
          eventId: eid,
          teamId: tid,
          TotalPoints: earnedPoint,
        })
        .onConflict(["eventId", "teamId"])
        .merge({
          TotalPoints: knex.raw("?? + ?", [
            "pointsTable.TotalPoints",
            earnedPoint,
          ]),
        });

      return res.status(200).json({
        success: true,
        message: "Next Riddle",
        next: nextIndex[0].Number_correct_answer,
      });
    });
  } catch (error) {
    console.log(error);
    return res
      .status(500)
      .json({ success: false, message: "Unknown Error", next: null });
  }
};
