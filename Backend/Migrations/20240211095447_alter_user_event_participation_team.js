/**
 * @param { import("knex").Knex } knex
 * @returns { Promise<void> }
 */
exports.up = function(knex) {
  return  knex.schema.alterTable('user_event_participation', function(table) {
    table.dropForeign(['user_id']);
    table.dropColumn('user_id');
    table.integer('team_id').unsigned().references('team_id').inTable('teams').notNullable();
  })
};

/**
 * @param { import("knex").Knex } knex
 * @returns { Promise<void> }
 */
exports.down = function(knex) {
    return knex.schema.alterTable('user_event_participation', function(table) {
        table.dropForeign(['team_id']);
        table.dropColumn('team_id');
        table.integer('user_id').unsigned().references('user_id').inTable('users').notNullable();
      })
};
