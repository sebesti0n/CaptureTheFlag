const client=require('../Configuration/pSqlConfig');
exports.execute = async (query)=>{
    try {
        await client.connect();     // gets connection
        await client.query(query);  // sends queries
        return true;
    } catch (error) {
        console.error(error.stack);
        return false;
    }finally{
        await client.end();
    }
}