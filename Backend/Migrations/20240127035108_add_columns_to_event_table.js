/**
 * @param { import("knex").Knex } knex
 * @returns { Promise<void> }
 */
exports.up = function(knex) {
    return knex.schema.alterTable('events', function (table) {
        table.string('start_ms',15).defaultTo(""); 
        table.string('end_ms',15).defaultTo("");
    });
};

/**
 * @param { import("knex").Knex } knex
 * @returns { Promise<void> }
 */
exports.down = function(knex) {
    return knex.schema.alterTable('events', function (table) {
        table.dropColumns(['start_ms','end_ms']); 
    });
};
