/**
 * @param { import("knex").Knex } knex
 * @returns { Promise<void> }
 */
exports.up = function(knex) {
    return knex.schema.createTable('events', function (table) {
        table.increments('event_id').primary();
        table.string('title', 100).notNullable();
        table.string('location', 100).notNullable();
        table.text('description').notNullable();
        table.timestamp('start_time').notNullable();
        table.timestamp('end_time').notNullable();
        table.integer('owner_id').references('user_id').inTable('users').notNullable();
        table.integer('No_of_questions').defaultTo(0);
      });
};

/**
 * @param { import("knex").Knex } knex
 * @returns { Promise<void> }
 */
exports.down = function(knex) {
      return knex.schema.dropTable('events');
};
