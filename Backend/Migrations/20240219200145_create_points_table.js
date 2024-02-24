/**
 * @param { import("knex").Knex } knex
 * @returns { Promise<void> }
 */
exports.up = function (knex) {
  return knex.schema.createTable("pointsTable", function (table) {
    table.increments("point_id").primary();
    table
      .integer("eventId")
      .unsigned()
      .references("event_id")
      .inTable("events")
      .notNullable();
    table
      .integer("teamId")
      .unsigned()
      .references("team_id")
      .inTable("teams")
      .notNullable();
    table.integer("TotalPoints").defaultTo(0);
  });
};

/**
 * @param { import("knex").Knex } knex
 * @returns { Promise<void> }
 */
exports.down = function (knex) {
  return knex.schema.dropTable("pointsTable");
};
