const knex = require("knex")(
  require("../Configuration/knexfile")["development"]
);
const redis = require("ioredis");
const client = new redis('rediss://red-cn5kj0acn0vc73d75p7g:FDuY82rDmivduYhcp9YwW6PdRC2WOAZz@oregon-redis.render.com:6379');

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
    const tid = req.query.tid;
    const eid = req.query.eid;
    const start_time = req.query.st;
    const key = 'tid='+tid+'&eid='+eid;

    client.get(key, async function (err, result) {
      if (err) console.log("redis got fucked", err);
      if (result) {
        console.log("from Redis");
        return res.status(200).json(JSON.parse(result));
      } else {
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
        client.set(key, JSON.stringify(jsonData),'EX',18000, (err, reply) => {
          if (err) {
            console.error("Error setting data in Redis:", err);
          } else {
            console.log("set!!!", reply);
          }
        });
        return res.status(200).json(jsonData);
      }
    });
  } catch (error) {
    console.log(error);
    return res
      .status(500)
      .json({ success: false, message: "unknown Error!", next: null });
  }
};
