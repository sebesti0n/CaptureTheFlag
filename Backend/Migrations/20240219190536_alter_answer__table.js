/**
 * @param { import("knex").Knex } knex
 * @returns { Promise<void> }
 */
exports.up = function (knex) {
  return knex.schema.alterTable("answers_history", function (table) {
    table.dropColumn("timestamp");
    table.dropColumn("answer_text");
    table.integer("hint1").defaultTo(0);
    table.integer("hint2").defaultTo(0);
    table.integer("hint3").defaultTo(0);
    table.dropColumn("user_id");
    table.bigInteger("startTime").defaultTo(0);
    table.bigInteger("endTime").defaultTo(0);
    table
      .integer("teamId")
      .references("team_id")
      .inTable("teams")
      .notNullable();
    table
      .integer("event_id")
      .references("event_id")
      .inTable("events")
      .notNullable();
  });
};

/**
 * @param { import("knex").Knex } knex
 * @returns { Promise<void> }
 */
exports.down = function (knex) {
  return knex.schema.alterTable("answers_history", function (table) {
    table.dropColumns([
      "hint1",
      "hint2",
      "hint3",
      "startTime",
      "endTime",
      "teamId",
      "questionId",
      "event_id",
    ]);
    table
      .integer("user_id")
      .unsigned()
      .references("user_id")
      .inTable("users")
      .notNullable();
    table
      .integer("question_id")
      .unsigned()
      .references("question_id")
      .inTable("questions")
      .notNullable();
    table.text("answer_text").notNullable();
    table.timestamp("timestamp").defaultTo(knex.fn.now());
  });
};
