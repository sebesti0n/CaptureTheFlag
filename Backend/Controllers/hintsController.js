const knex = require("knex")(require("../Configuration/knexfile")["development"]);

exports.updateHint = async (req, res) => {
  try {
    const { teamId, eventId, currentRiddleId, hintType } = req.query;
    const hints = await knex("answers_history")
      .where({
        question_id: currentRiddleId,
        teamId,
        event_id: eventId,
        endTime: 0,
      })
      .select("question_id", "hint1", "hint2", "hint3", "startTime");

    if (hints.length === 0) {
      return res.status(200).json({
        success: true,
        message: "Hints are not available",
        hint: null,
      });
    }

    const currMs = Date.now();
    const startMs = hints[0].startTime;
    let hintUnlocked = false;

    if (hintType >= 1 && hintType <= 3) {
      const hintNumber = `hint${hintType}`;
      const hintLockTime = hintType * 1800000; // 180000 ms = 3 minutes
      console.log(parseInt(startMs)+hintLockTime);

      if ((parseInt(startMs)+hintLockTime) > currMs) {
        return res.status(200).json({
          success: true,
          message: "Locked",
          unlockedIn: currMs - startMs,
          hint1: false,
          hint2: false,
          hint3: false,
        });
      }

      if (hints[0][hintNumber] === 0) {
        await knex.transaction(async (trx) => {
          await trx("pointsTable").insert({
            eventId,
            teamId,
            TotalPoints: -50,
          }).onConflict(["eventId", "teamId"]).merge({
            TotalPoints: knex.raw("?? + ?", ["pointsTable.TotalPoints", -50]),
          });

          await trx("answers_history")
            .where({
              question_id: currentRiddleId,
              teamId,
              event_id: eventId,
              endTime: 0,
            })
            .update({
              [hintNumber]: 1,
            });

          hintUnlocked = true;
        });
      } else {
        hintUnlocked = true;
      }

      if (hintUnlocked) {
        const response = {
          success: true,
          message: "Unlocked",
          unlockedIn: 0,
          hint1: hintType == 1,
          hint2: hintType == 2,
          hint3: hintType == 3,
        };
        return res.status(200).json(response);
      }
    }

    return res.status(400).json({
      success: false,
      message: "Invalid hint type",
      hint1: null,
      hint2: null,
      hint3: null
    });
  } catch (error) {
    console.error("Error:", error);
    return res.status(500).json({
      success: false,
      message: "Internal Server Error",
      hint1: null,
      hint2: null,
      hint3: null
    });
  }
};
