/**
 * @param { import("knex").Knex } knex
 * @returns { Promise<void> }
 */
exports.up = function(knex) {
    return knex.schema.alterTable('users', function(table){      
    table.string('FirstName').notNullable();
    table.string('LastName').notNullable();
    table.string('MobileNo', 10).notNullable();
    table.string('CollegeName').notNullable();
    table.string('password').notNullable();
    table.string('cnfpassword').notNullable();
    table.dropColumn('name'); 
})
};

/**
 * @param { import("knex").Knex } knex
 * @returns { Promise<void> }
 */
exports.down = function(knex) {
    return knex.schema.alterTable('users', function (table) {
        table.dropColumn('FirstName');
    table.dropColumn('LastName');
    table.dropColumn('MobileNo', 10);
    table.dropColumn('CollegeName');
    table.dropColumn('password');
    table.dropColumn('cnfpassword');
    });
};
