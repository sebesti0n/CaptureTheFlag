/**
 * @param { import("knex").Knex } knex
 * @returns { Promise<void> }
 */
exports.up = async function(knex) {
  return await knex.schema.createTable('user_event_participation',function (table) {
    table.increments('participation_id').primary();
    table.integer('user_id').unsigned().references('user_id').inTable('users').notNullable();
    table.integer('event_id').unsigned().references('event_id').inTable('events').notNullable();
    table.timestamp('registration_time').defaultTo(knex.fn.now());
    table.boolean('is_registered').notNullable();
    table.timestamp('start_time').defaultTo(null);
    table.timestamp('end_time').defaultTo(null);
    table.integer('Number_correct_answer').defaultTo(0);
    table.integer('Rank').defaultTo(-1);
  });
};

/**
 * @param { import("knex").Knex } knex
 * @returns { Promise<void> }
 */
exports.down = async function(knex) {
    return await knex.schema.dropTable('user_event_participation');
};
