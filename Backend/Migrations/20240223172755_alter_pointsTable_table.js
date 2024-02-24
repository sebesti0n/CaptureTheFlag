exports.up = function(knex) {
    return knex.schema.alterTable('pointsTable', function(table) {
      table.unique(['eventId', 'teamId']);
    });
  };
  
  exports.down = function(knex) {
    return knex.schema.alterTable('pointsTable', function(table) {
      table.dropUnique(['eventId', 'teamId']);
    });
  };
  