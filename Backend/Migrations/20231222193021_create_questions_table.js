/**
 * @param { import("knex").Knex } knex
 * @returns { Promise<void> }
 */
exports.up = function(knex) {
    return knex.schema.createTable('questions', function (table) {
        table.increments('question_id').primary();
        table.integer('event_id').references('event_id').inTable('events').notNullable();
        table.text('question').notNullable();
        table.text('answer').notNullable();
        table.text('unique_code').notNullable();
      });
};

/**
 * @param { import("knex").Knex } knex
 * @returns { Promise<void> }
 */
exports.down = function(knex) {
    return knex.schema.dropTable('questions');
};
