/**
 * @param { import("knex").Knex } knex
 * @returns { Promise<void> }
 */
exports.up = function(knex) {
    return knex.schema.alterTable('user_event_participation', function(table) {
      table.bigInteger('new_start_time');
      table.bigInteger('new_end_time');
    })
    .then(() => {
      return knex.schema.alterTable('user_event_participation', function(table) {
        table.dropColumn('start_time');
        table.renameColumn('new_start_time', 'start_time');
        table.dropColumn('end_time');
        table.renameColumn('new_end_time', 'end_time');
      });
    });
  };
  
  /**
   * @param { import("knex").Knex } knex
   * @returns { Promise<void> }
   */
  exports.down = function(knex) {
    return knex.schema.alterTable('user_event_participation', function(table) {
      table.timestamp('new_start_time').defaultTo(null);
      table.timestamp('new_end_time').defaultTo(null);
    })
    .then(() => {
      return knex.schema.alterTable('user_event_participation', function(table) {
        table.dropColumn('start_time');
        table.renameColumn('new_start_time', 'start_time');
        table.dropColumn('end_time');
        table.renameColumn('new_end_time', 'end_time');
      });
    });
  };
  