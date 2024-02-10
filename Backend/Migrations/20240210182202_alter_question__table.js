/**
 * @param { import("knex").Knex } knex
 * @returns { Promise<void> }
 */
exports.up = function(knex) {
  return knex.schema.alterTable('questions', function(table) {
    table.text('storyline').defaultTo("No Data")
    table.integer('level').defaultTo(1);
  })
};

/**
 * @param { import("knex").Knex } knex
 * @returns { Promise<void> }
 */
exports.down = function(knex) {
    return knex.schema.alterTable('questions', function(table) {
    table.dropColumns(['storyline','level']);
    } )
};
